package ic.uff.tic10086.exercicios.datasets;

import com.opencsv.CSVReader;
import ic.uff.tic10086.utils.JenaSchema;
import ic.uff.tic10086.utils.OWLAPIOntology;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

public class TurismoECultura {

    public static final Lang EXPORT_LANG = Lang.TTL;
    private static final String DIRECTORY = "./src/main/resources/dat/rdf";

    public static final String LOCAL_NAME = "turismoECultura";
    public static final String BASE_URI = "http://localhost:8080/";

    public static final String DBPEDIA_NS = "http://dbpedia.org/ontology/";

    public static final String SCHEMA_NS = "http://schema.org/";
    public static final String SCHEMA_URL_STRING = "http://dadosabertos.rio.rj.gov.br/apiCultura/apresentacao/csv/turismoECultura_.csv";

    public static void main(String[] args) throws FileNotFoundException, IOException, MalformedURLException, CompressorException, OWLOntologyCreationException, OWLOntologyStorageException {
        init();
        convertToDBpedia();
        //convertToSchema();
    }

    public static void convertToDBpedia() throws IOException, MalformedURLException, CompressorException, OWLOntologyCreationException, OWLOntologyStorageException {
        OWLOntology ontology = OWLAPIOntology.getOntology("http://downloads.dbpedia.org/2014/dbpedia_2014.owl.bz2");
        OWLOntologyManager manager = ontology.getOWLOntologyManager();

        System.out.println("Ontology Loaded...");
        System.out.println("Ontology : " + ontology.getOntologyID());
        System.out.println("Format      : " + manager.getOntologyFormat(ontology));
        System.out.println("Axioms count: " + ontology.getAxiomCount());

        OWLDataFactory df = manager.getOWLDataFactory();
        URL url = new URL(SCHEMA_URL_STRING);
        try (
                InputStreamReader in = new InputStreamReader(url.openStream(), "Windows-1252");
                BufferedReader buff = new BufferedReader(in);
                CSVReader reader = new CSVReader(buff, ',', '"', 1);) {

            String[] nextLine;
            String uri1, uri2, uri3, name, street, number, neighborhood, telephone, latitude, longitude;
            while ((nextLine = reader.readNext()) != null)
                try {
                    name = nextLine[0];
                    street = nextLine[1];
                    number = nextLine[2];
                    neighborhood = nextLine[3];
                    telephone = nextLine[4];
                    latitude = nextLine[5];
                    longitude = nextLine[6];

                    uri1 = BASE_URI + UUID.randomUUID();
                    uri2 = BASE_URI + UUID.randomUUID();
                    uri3 = BASE_URI + UUID.randomUUID();

                    OWLIndividual p = df.getOWLNamedIndividual(IRI.create(uri1));
                    OWLClass place = df.getOWLClass(DBPEDIA_NS + "Place");
                    ontology.addAxiom(df.getOWLClassAssertionAxiom(place, p));

                } catch (Exception e) {

                }
        }
        try {
            IRI iri = IRI.create(new File(DIRECTORY + "/saved_pizza.owl"));
            manager.saveOntology(ontology, new OWLXMLDocumentFormat(), iri);
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    public static void convertToSchema() throws MalformedURLException, FileNotFoundException, IOException {
        Model schema = JenaSchema.getSchemaOrg();
        Model model = ModelFactory.createDefaultModel();
        model.setNsPrefix("schema", SCHEMA_NS);
        model.setNsPrefix("", BASE_URI);

        URL url = new URL(SCHEMA_URL_STRING);
        try (
                InputStreamReader in = new InputStreamReader(url.openStream(), "Windows-1252");
                BufferedReader buff = new BufferedReader(in);
                CSVReader reader = new CSVReader(buff, ',', '"', 1);) {

            String[] nextLine;
            String uri1, uri2, uri3, name, street, number, neighborhood, telephone, latitude, longitude;
            while ((nextLine = reader.readNext()) != null)
                try {
                    name = nextLine[0];
                    street = nextLine[1];
                    number = nextLine[2];
                    neighborhood = nextLine[3];
                    telephone = nextLine[4];
                    latitude = nextLine[5];
                    longitude = nextLine[6];

                    uri1 = BASE_URI + UUID.randomUUID();
                    uri2 = BASE_URI + UUID.randomUUID();
                    uri3 = BASE_URI + UUID.randomUUID();

                    model.createResource(uri1, schema.getResource(detectSchemaOrgClass(name)))
                            .addProperty(schema.getProperty(SCHEMA_NS + "name"), name)
                            .addProperty(schema.getProperty(SCHEMA_NS + "address"), model.createResource(uri2, schema.getResource(SCHEMA_NS + "PostalAddress"))
                                    .addProperty(schema.getProperty(SCHEMA_NS + "addressLocality"), "Rio de Janeiro")
                                    .addProperty(schema.getProperty(SCHEMA_NS + "addressRegion"), "RJ ")
                                    .addProperty(schema.getProperty(SCHEMA_NS + "streetAddress"), street + ", " + number + ", " + neighborhood))
                            .addProperty(schema.getProperty(SCHEMA_NS + "geo"), model.createResource(uri3, schema.getResource(SCHEMA_NS + "GeoCoordinates"))
                                    .addProperty(schema.getProperty(SCHEMA_NS + "latitude"), latitude)
                                    .addProperty(schema.getProperty(SCHEMA_NS + "longitude"), longitude)
                                    .addProperty(schema.getProperty(SCHEMA_NS + "telephone"), telephone)
                            );
                } catch (Exception e) {

                }
        }

        RDFDataMgr.write(new FileOutputStream(new File(DIRECTORY + "/" + LOCAL_NAME + "." + EXPORT_LANG.getFileExtensions().get(0))), model, EXPORT_LANG);
    }

    public static void init() {
        org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
    }

    private static String detectSchemaOrgClass(String name) {
        String uri;
        if (name.startsWith("Teatro "))
            uri = SCHEMA_NS + "PerformingArtsTheater";
        else if (name.startsWith("Praia "))
            uri = SCHEMA_NS + "Beach";
        else if (name.startsWith("Museu "))
            uri = SCHEMA_NS + "Museum";
        else if (name.startsWith("Igreja "))
            uri = SCHEMA_NS + "CatholicChurch";
        else if (name.startsWith("Mosteiro "))
            uri = SCHEMA_NS + "CatholicChurch";
        else if (name.startsWith("Catedral "))
            uri = SCHEMA_NS + "CatholicChurch";
        else if (name.startsWith("Biblioteca "))
            uri = SCHEMA_NS + "Library";
        else if (name.startsWith("Centro Cultural "))
            uri = SCHEMA_NS + "EventVenue";
        else if (name.startsWith("Casa "))
            uri = SCHEMA_NS + "EventVenue";
        else if (name.startsWith("Espaço "))
            uri = SCHEMA_NS + "EventVenue";
        else if (name.startsWith("Parque "))
            uri = SCHEMA_NS + "Park";
        else if (name.startsWith("Praça "))
            uri = SCHEMA_NS + "Park";
        else if (name.startsWith("Sala "))
            uri = SCHEMA_NS + "MusicVenue";
        else if (name.startsWith("Estádio "))
            uri = SCHEMA_NS + "StadiumOrArena";
        else {
            System.out.println(name);
            uri = SCHEMA_NS + "Place";
        }
        return uri;
    }
}
