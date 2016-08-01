package ic.uff.tic10086.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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

    private static final String RDF_DIR = "./src/main/resources/dat/rdf";
    public static final Lang IMPORT_LANG = Lang.RDFXML;
    public static final Lang EXPORT_LANG = Lang.TTL;

    public static final String SCHEMA_ORG_FILENAME = "schemaOrg";
    public static final String SCHEMA_ORG_BASE_URI = "http://schema.org/";
    public static final String SCHEMA_ORG_URL = "https://github.com/schemaorg/schemaorg/raw/sdo-makemake/data/schema.rdfa";

    public static final String DBPEDIA_FILENAME = "dbpedia";
    public static final String DBPEDIA_BASE_URI = "http://dbpedia.org/ontology/";
    public static final String DBPEDIA_URL = "http://downloads.dbpedia.org/2015-10/dbpedia_2015-10.owl";

    public static Model getSchemaOrg() throws UnsupportedEncodingException, FileNotFoundException {
        return loadSchemaOrg(SCHEMA_ORG_FILENAME);
    }

    private static Model loadSchemaOrg(String localName) throws UnsupportedEncodingException, FileNotFoundException {
        Model model = ModelFactory.createDefaultModel();
        try {
            RDFDataMgr.read(model, new FileInputStream(RDF_DIR + "/" + localName + "." + EXPORT_LANG.getFileExtensions().get(0)), EXPORT_LANG);
        } catch (FileNotFoundException ex) {
            model = downloadSchemaOrg(localName);
        }
        return model;
    }

    private static Model downloadSchemaOrg(String localName) throws UnsupportedEncodingException, FileNotFoundException {
        Model model = ModelFactory.createDefaultModel();
        try {
            String url = URLEncoder.encode(SCHEMA_ORG_URL, "UTF-8");
            model.read("http://rdf-translator.appspot.com/convert/rdfa/xml/" + url);
            OutputStream out = new FileOutputStream(new File(RDF_DIR + "/" + localName + "." + EXPORT_LANG.getFileExtensions().get(0)));
            RDFDataMgr.write(out, model, EXPORT_LANG);
        } finally {
        }
        return model;
    }

    public static Model getDBpedia() throws IOException, MalformedURLException, CompressorException {
        return getSchema(DBPEDIA_URL, DBPEDIA_BASE_URI, DBPEDIA_FILENAME, IMPORT_LANG);
    }

    public static Model getSchema(String urlString, String base, String filename, Lang importLang) throws IOException, MalformedURLException, CompressorException {
        return loadSchema(urlString, base, filename, importLang);
    }

    private static Model loadSchema(String urlString, String base, String filename, Lang importLang) throws IOException, MalformedURLException, CompressorException {
        Model model = ModelFactory.createDefaultModel();
        try {
            RDFDataMgr.read(model, new FileInputStream(RDF_DIR + filename + "." + importLang.getFileExtensions().get(0)), DBPEDIA_BASE_URI, EXPORT_LANG);
        } catch (FileNotFoundException ex) {
            model = downloadSchema(urlString, base, filename);
        }
        return model;
    }

    private static Model downloadSchema(String urlString, String base, String localName) throws MalformedURLException, IOException, CompressorException {
        Model model = ModelFactory.createDefaultModel();
        URL url = new URL(urlString);
        try (BufferedInputStream bis = new BufferedInputStream(url.openStream());) {
            model.read(bis, base);
            RDFDataMgr.write(new FileOutputStream(new File(RDF_DIR + "/" + localName + "." + EXPORT_LANG.getFileExtensions().get(0))), model, EXPORT_LANG);
        } catch (Exception e) {
            try (
                    BufferedInputStream bis = new BufferedInputStream(url.openStream());
                    CompressorInputStream input = new CompressorStreamFactory().createCompressorInputStream(bis);) {
                model.read(input, base);
                OutputStream out = new FileOutputStream(new File(RDF_DIR + "/" + localName + "." + EXPORT_LANG.getFileExtensions().get(0)));
                RDFDataMgr.write(out, model, EXPORT_LANG);
            }
        }
        return model;
    }

}
