package ic.uff.tic10086.exercicios;

import com.opencsv.CSVReader;
import ic.uff.tic10086.utils.JenaSchema;
import ic.uff.tic10086.utils.OWLAPIOntology;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.parameters.Imports;

public class TurismoECultura extends MyDataset {

    public static final String DATASET_URL = "http://dadosabertos.rio.rj.gov.br/apiCultura/apresentacao/csv/turismoECultura_.csv";

    public static final String DBPEDIA_NS = "http://dbpedia.org/ontology/";
    public static final String SCHEMA_ORG_NS = "http://schema.org/";

    public static void main(String[] args) {
        try {
            init();

            FILENAME = "turismoECultura";
            DEFAULT_NS = "http://" + DOMAIN + DEREF_PORT + "/resource/";
            TDB_ASSEMBLER_FILE = CONF_DIR + "/turismoECultura.ttl";
            FUSEKI_UPDATE_URL = "http://" + DOMAIN + SPARQL_PORT + "/fuseki/turismoECultura.temp/update";
            FUSEKI_DATA_URL = "http://" + DOMAIN + SPARQL_PORT + "/fuseki/turismoECultura.temp/data";

            Dataset dataset = TDBFactory.assembleDataset(TDB_ASSEMBLER_FILE);
            try {
                convertToRDF(dataset);
                exportResources(dataset);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dataset.close();
            }
            saveAsDBpediaOntology();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void convertToRDF(Dataset dataset) throws MalformedURLException, FileNotFoundException, IOException, UnsupportedEncodingException {
        dataset.begin(ReadWrite.WRITE);
        Model schema = JenaSchema.getSchemaOrg();
        Model model = dataset.getDefaultModel();
        model.getNsPrefixMap().clear();
        model.removeAll();
        model.setNsPrefix("sch", SCHEMA_ORG_NS);
        model.setNsPrefix("", DEFAULT_NS);

        URL url = new URL(DATASET_URL);
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

                    uri1 = DEFAULT_NS + "id-" + UUID.randomUUID();
                    uri2 = DEFAULT_NS + "id-" + UUID.randomUUID();
                    uri3 = DEFAULT_NS + "id-" + UUID.randomUUID();

                    model.createResource(uri1, schema.getResource(detectSchemaOrgClass(name)))
                            .addProperty(schema.getProperty(SCHEMA_ORG_NS + "name"), name)
                            .addProperty(schema.getProperty(SCHEMA_ORG_NS + "telephone"), telephone)
                            .addProperty(schema.getProperty(SCHEMA_ORG_NS + "address"), model.createResource(uri2, schema.getResource(SCHEMA_ORG_NS + "PostalAddress"))
                                    .addProperty(schema.getProperty(SCHEMA_ORG_NS + "addressLocality"), "Rio de Janeiro")
                                    .addProperty(schema.getProperty(SCHEMA_ORG_NS + "addressRegion"), "RJ")
                                    .addProperty(schema.getProperty(SCHEMA_ORG_NS + "addressCountry"), "Brasil")
                                    .addProperty(schema.getProperty(SCHEMA_ORG_NS + "streetAddress"), street + (number == null || number.equals("") ? "" : ", " + number) + ", " + neighborhood))
                            .addProperty(schema.getProperty(SCHEMA_ORG_NS + "geo"), model.createResource(uri3, schema.getResource(SCHEMA_ORG_NS + "GeoCoordinates"))
                                    .addProperty(schema.getProperty(SCHEMA_ORG_NS + "latitude"), latitude)
                                    .addProperty(schema.getProperty(SCHEMA_ORG_NS + "longitude"), longitude));
                } catch (Exception e) {
                }
        }
        dataset.commit();
        dataset.end();
    }

    private static String detectSchemaOrgClass(String name) {
        String uri;
        if (name.startsWith("Teatro "))
            uri = SCHEMA_ORG_NS + "PerformingArtsTheater";
        else if (name.startsWith("Praia "))
            uri = SCHEMA_ORG_NS + "Beach";
        else if (name.startsWith("Museu "))
            uri = SCHEMA_ORG_NS + "Museum";
        else if (name.startsWith("Igreja "))
            uri = SCHEMA_ORG_NS + "CatholicChurch";
        else if (name.startsWith("Mosteiro "))
            uri = SCHEMA_ORG_NS + "CatholicChurch";
        else if (name.startsWith("Catedral "))
            uri = SCHEMA_ORG_NS + "CatholicChurch";
        else if (name.startsWith("Biblioteca "))
            uri = SCHEMA_ORG_NS + "Library";
        else if (name.startsWith("Centro Cultural "))
            uri = SCHEMA_ORG_NS + "EventVenue";
        else if (name.startsWith("Casa "))
            uri = SCHEMA_ORG_NS + "EventVenue";
        else if (name.startsWith("Espaço "))
            uri = SCHEMA_ORG_NS + "EventVenue";
        else if (name.startsWith("Parque "))
            uri = SCHEMA_ORG_NS + "Park";
        else if (name.startsWith("Praça "))
            uri = SCHEMA_ORG_NS + "Park";
        else if (name.startsWith("Sala "))
            uri = SCHEMA_ORG_NS + "MusicVenue";
        else if (name.startsWith("Estádio "))
            uri = SCHEMA_ORG_NS + "StadiumOrArena";
        else {
            uri = SCHEMA_ORG_NS + "Place";
            System.out.println(name);
        }
        return uri;
    }

    private static void saveAsDBpediaOntology() throws IOException, MalformedURLException, CompressorException, OWLOntologyCreationException, OWLOntologyStorageException {
        OWLOntology ontology = OWLAPIOntology.getDBpedia();
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLDataFactory df = manager.getOWLDataFactory();
        PrefixDocumentFormat pm = (PrefixDocumentFormat) manager.getOntologyFormat(ontology);
        if (pm != null) {
            pm.setDefaultPrefix(DEFAULT_NS);
            pm.setPrefix("dbo", DBPEDIA_NS);
        }

        URL url = new URL(DATASET_URL);
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

                    uri1 = ":id-" + UUID.randomUUID().toString();
                    OWLIndividual p = df.getOWLNamedIndividual(uri1, pm);
                    OWLClass cplace = df.getOWLClass("dbo:Place", pm);
                    ontology.addAxiom(df.getOWLClassAssertionAxiom(cplace, p));
                    OWLDataProperty pname = df.getOWLDataProperty("dbo:name", pm);
                    ontology.addAxiom(df.getOWLDataPropertyAssertionAxiom(pname, p, name));
                    OWLDataProperty paddress = df.getOWLDataProperty("dbo:address", pm);
                    ontology.addAxiom(df.getOWLDataPropertyAssertionAxiom(paddress, p, street + (number == null || number.equals("") ? "" : ", " + number) + ", " + neighborhood));
                    OWLDataProperty pcoordinates = df.getOWLDataProperty("dbo:coordinates", pm);
                    ontology.addAxiom(df.getOWLDataPropertyAssertionAxiom(pcoordinates, p, "(" + latitude + "," + longitude + ")"));

                    uri2 = ":id-" + UUID.randomUUID().toString();
                    OWLIndividual c = df.getOWLNamedIndividual(uri2, pm);
                    OWLClass ccity = df.getOWLClass("dbo:City", pm);
                    ontology.addAxiom(df.getOWLClassAssertionAxiom(ccity, c));
                    ontology.addAxiom(df.getOWLDataPropertyAssertionAxiom(pname, c, "Rio de Janeiro"));
                    OWLObjectProperty plocationCity = df.getOWLObjectProperty("dbo:locationCity", pm);
                    ontology.addAxiom(df.getOWLObjectPropertyAssertionAxiom(plocationCity, p, c));

                    uri3 = ":id-" + UUID.randomUUID().toString();
                    OWLIndividual cc = df.getOWLNamedIndividual(uri3, pm);
                    OWLClass ccountry = df.getOWLClass("dbo:Country", pm);
                    ontology.addAxiom(df.getOWLClassAssertionAxiom(ccountry, cc));
                    ontology.addAxiom(df.getOWLDataPropertyAssertionAxiom(pname, cc, "Brasil"));
                    OWLObjectProperty plocationCountry = df.getOWLObjectProperty("dbo:locationCountry", pm);
                    ontology.addAxiom(df.getOWLObjectPropertyAssertionAxiom(plocationCountry, p, cc));
                } catch (Exception e) {
                }
        }
        try {
            OWLOntology ontologyToSave = manager.createOntology(ontology.aboxAxioms(Imports.INCLUDED));
            manager.setOntologyDocumentIRI(ontologyToSave, IRI.create(DEFAULT_NS + "TurismoECultura.owl"));

            OWLXMLDocumentFormat owl = new OWLXMLDocumentFormat();
            owl.copyPrefixesFrom(pm);
            IRI iri = IRI.create(new File(OWL_DIR + "/" + FILENAME + ".owl"));
            manager.saveOntology(ontologyToSave, owl, iri);
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
}
