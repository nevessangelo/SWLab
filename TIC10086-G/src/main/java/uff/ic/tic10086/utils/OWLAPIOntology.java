package uff.ic.tic10086.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.AutoIRIMapper;

public class OWLAPIOntology {

    private static OWLOntologyManager MANAGER = null;
    private static final String DIRECTORY = "./src/main/resources/dat/owl";

    public static final String DBPEDIA_LOCAL_NAME = "dbpedia";
    public static final String DBPEDIA_BASE_URI = "http://dbpedia.org/ontology/";
    public static final String DBPEDIA_URL_STRING = "http://downloads.dbpedia.org/2015-10/dbpedia_2015-10.owl";

    private static OWLOntologyManager getOntologyManager() {
        if (MANAGER == null) {
            MANAGER = OWLManager.createOWLOntologyManager();
            MANAGER.getIRIMappers().add(new AutoIRIMapper(new File(DIRECTORY), true));
        }
        return MANAGER;
    }

    public static OWLOntology getDBpedia() throws IOException, MalformedURLException, CompressorException, OWLOntologyCreationException, OWLOntologyStorageException {
        return getOntology(DBPEDIA_URL_STRING, DBPEDIA_LOCAL_NAME);
    }

    public static OWLOntology getOntology(String urlString, String localName) throws IOException, MalformedURLException, CompressorException, OWLOntologyCreationException, OWLOntologyStorageException {
        return loadOntology(urlString, localName);
    }

    private static OWLOntology loadOntology(String urlString, String localName) throws IOException, MalformedURLException, CompressorException, OWLOntologyCreationException, OWLOntologyStorageException {
        OWLOntology ontology;
        try {
            OWLOntologyManager manager = getOntologyManager();
            ontology = manager.loadOntologyFromOntologyDocument(new File(DIRECTORY + "/" + localName));
        } catch (Exception ex) {
            ontology = downloadOntology(urlString, localName);
        }
        return ontology;
    }

    private static OWLOntology downloadOntology(String urlString, String localName) throws MalformedURLException, IOException, CompressorException, OWLOntologyCreationException, OWLOntologyStorageException {
        OWLOntology ontology;
        URL url = new URL(urlString);
        try (BufferedInputStream bis = new BufferedInputStream(url.openStream());) {

            ontology = saveOntology(bis, localName);
        } catch (Exception ex) {
            try (
                    BufferedInputStream bis = new BufferedInputStream(url.openStream());
                    CompressorInputStream input = new CompressorStreamFactory().createCompressorInputStream(bis);) {

                ontology = saveOntology(input, localName);
            }
        }
        return ontology;
    }

    private static OWLOntology saveOntology(final InputStream bis, String localName) throws OWLOntologyCreationException, OWLOntologyStorageException {
        OWLOntologyManager manager = getOntologyManager();
        IRI iri = IRI.create(new File(DIRECTORY + "/" + localName + ".owl"));
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(bis);
        manager.saveOntology(ontology, new OWLXMLDocumentFormat(), iri);
        return ontology;
    }
}
