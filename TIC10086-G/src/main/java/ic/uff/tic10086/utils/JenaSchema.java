package ic.uff.tic10086.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

public class JenaSchema {

    public static final Lang EXPORT_LANG = Lang.RDFXML;
    private static final String DIRECTORY = "./src/main/resources/dat/rdf";

    public static final String SCHEMA_LOCAL_NAME = "schema";
    public static final String SCHEMA_BASE_URI = "http://schema.org/";
    public static final String SCHEMA_URL_STRING = "https://github.com/schemaorg/schemaorg/raw/sdo-makemake/data/schema.rdfa";

    public static final String DBPEDIA_LOCAL_NAME = "dbpedia";
    public static final String DBPEDIA_BASE_URI = "http://dbpedia.org/ontology/";
    public static final String DBPEDIA_URL_STRING = "http://downloads.dbpedia.org/2014/dbpedia_2014.owl.bz2";

    public static void main(String[] args) throws IOException, MalformedURLException, CompressorException {
        //getSchemaOntology();
        //getDBpediaOntology();
        getSchema("http://protege.stanford.edu/ontologies/pizza/pizza.owl", "http://www.co-ode.org/ontologies/pizza/pizza.owl", "pizza");
    }

    public static Model getSchemaOrg() throws UnsupportedEncodingException, FileNotFoundException {
        return loadSchemaOrg(SCHEMA_LOCAL_NAME);
    }

    private static Model loadSchemaOrg(String localName) throws UnsupportedEncodingException, FileNotFoundException {
        Model model = ModelFactory.createDefaultModel();
        try {
            RDFDataMgr.read(model, new FileInputStream(DIRECTORY + "/" + localName + "." + EXPORT_LANG.getFileExtensions().get(0)), EXPORT_LANG);
        } catch (FileNotFoundException ex) {
            model = downloadSchemaOrg(localName);
        }
        return model;
    }

    private static Model downloadSchemaOrg(String localName) throws UnsupportedEncodingException, FileNotFoundException {
        Model model = ModelFactory.createDefaultModel();
        try {
            String url = URLEncoder.encode(SCHEMA_URL_STRING, "UTF-8");
            model.read("http://rdf-translator.appspot.com/convert/rdfa/xml/" + url);
            RDFDataMgr.write(new FileOutputStream(new File(DIRECTORY + "/" + localName + "." + EXPORT_LANG.getFileExtensions().get(0))), model, EXPORT_LANG);
        } finally {
        }
        return model;
    }

    public static Model getDBpedia() throws IOException, MalformedURLException, CompressorException {
        return getSchema(DBPEDIA_URL_STRING, DBPEDIA_BASE_URI, DBPEDIA_LOCAL_NAME);
    }

    public static Model getSchema(String urlString, String base, String localName) throws IOException, MalformedURLException, CompressorException {
        return loadSchema(urlString, base, localName);
    }

    private static Model loadSchema(String urlString, String base, String localName) throws IOException, MalformedURLException, CompressorException {
        Model model = ModelFactory.createDefaultModel();
        try {
            RDFDataMgr.read(model, new FileInputStream(DIRECTORY + localName + "." + EXPORT_LANG.getFileExtensions().get(0)), DBPEDIA_BASE_URI, EXPORT_LANG);
        } catch (FileNotFoundException ex) {
            model = downloadSchema(urlString, base, localName);
        }
        return model;
    }

    private static Model downloadSchema(String urlString, String base, String localName) throws MalformedURLException, IOException, CompressorException {
        Model model = ModelFactory.createDefaultModel();
        URL url = new URL(urlString);
        try (BufferedInputStream bis = new BufferedInputStream(url.openStream());) {
            model.read(bis, base);
            RDFDataMgr.write(new FileOutputStream(new File(DIRECTORY + "/" + localName + "." + EXPORT_LANG.getFileExtensions().get(0))), model, EXPORT_LANG);
        } catch (Exception ex) {
            try (
                    BufferedInputStream bis = new BufferedInputStream(url.openStream());
                    CompressorInputStream input = new CompressorStreamFactory().createCompressorInputStream(bis);) {
                model.read(input, base);
                RDFDataMgr.write(new FileOutputStream(new File(DIRECTORY + "/" + localName + "." + EXPORT_LANG.getFileExtensions().get(0))), model, EXPORT_LANG);
            }
        }
        return model;
    }

}
