package uff.ic.lleme.entityrelatednesstestdata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Map;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.VOID;
import org.apache.jena.vocabulary.XSD;

public class Main {

    private static final String ONTOLOGY_NS = "http://swlab.ic.uff.br/ontologyEntityRelatednessTestDataset.rdf#";
    private static final String DATA_NS = "http://swlab.ic.uff.br/resource/";
    private static final String VOID_NS = "http://swlab.ic.uff.br/void.ttl#";
    private static final String ALIGN_NS = "http://knowledgeweb.semanticweb.org/heterogeneity/alignment#";

    private static final String DATA_ROOT = "./data/EntityRelatednessTestDataset";
    private static final String RDF_ROOT = "./data/RDF";
    private static final String HOST_ADDR = "swlab.ic.uff.br";
    private static final String FUSEKI_URL = "http://" + HOST_ADDR + "/fuseki";

    private static final String USERNAME = "";
    private static final String PASSWORD = "";

    private static final String DATASET_NAME = "EntityRelatednessTestData";
    private static final String JSON_SERIALIZATION_NAME = RDF_ROOT + "/" + DATASET_NAME + ".json";
    private static final String XML_SERIALIZATION_NAME = RDF_ROOT + "/" + DATASET_NAME + ".rdf";
    private static final String TURTLE_SERIALIZATION_NAME = RDF_ROOT + "/" + DATASET_NAME + ".ttl";
    private static final String LOCAL_VOID_NAME = RDF_ROOT + "/void.ttl";
    private static final String LOCAL_ONTOLOGY_NAME = RDF_ROOT + "/ontology/" + DATASET_NAME + ".rdf";

    private static final String REMOTE_VOID_NAME = "/void.ttl";
    private static final String REMOTE_ONTOLOGY_NAME = "/ontology/" + DATASET_NAME + ".rdf";
    private static final String DATASET_URL = FUSEKI_URL + "/" + DATASET_NAME + "/data";

    private static final MusicScores MUSIC_SCORES = new MusicScores();
    private static final MusicRankedPaths MUSIC_RANKED_SCORES = new MusicRankedPaths();
    private static final MusicPropertyRelevanceScore MUSIC_PROPERTY_RELEVANCE_SCORE = new MusicPropertyRelevanceScore();
    private static final MusicEntityMappings MUSIC_ENTITY_MAPPINGS = new MusicEntityMappings();
    private static final MusicClassMapping MUSIC_CLASS_MAPPING = new MusicClassMapping();
    private static final MusicEntityPairs MUSIC_ENTITY_PAIRS = new MusicEntityPairs();

    private static final MovieScores MOVIE_SCORES = new MovieScores();
    private static final MovieRankedPaths MOVIE_RANKED_PATHS = new MovieRankedPaths();
    private static final MoviePropertyRelevanceScore MOVIE_PROPERTY_RELEVANCE_SCORE = new MoviePropertyRelevanceScore();
    private static final MovieEntityMappings MOVIE_ENTITY_MAPPING = new MovieEntityMappings();
    private static final MovieClassMapping MOVIE_CLASS_MAPPING = new MovieClassMapping();
    private static final MovieEntityPairs MOVIE_ENTITY_PAIRS = new MovieEntityPairs();

    public static void main(String[] args) throws FileNotFoundException, IOException, GeneralSecurityException {
        createVoID();
        createOntology();
        createDataset();
        System.out.println("Fim.");
    }

    private static void createVoID() throws FileNotFoundException, IOException {
        Model voidVocab = ModelFactory.createDefaultModel();
        voidVocab.setNsPrefix("", DATA_NS);
        voidVocab.setNsPrefix("align", ALIGN_NS);
        voidVocab.read("http://vocab.deri.ie/void#");

        Model voidData = ModelFactory.createDefaultModel();
        voidData.createResource(ONTOLOGY_NS + "EntityRelatednessTestData", VOID.Dataset)
                .addProperty(DCTerms.description, "The entity relatedness problem refers to the question of "
                        + "computing the relationship paths that better describe the connectivity between a "
                        + "given entity pair. This dataset supports the evaluation of approaches that address "
                        + "the entity relatedness problem. It covers two familiar domains, music and movies, and "
                        + "uses data available in IMDb and last.fm, which are popular reference datasets in these "
                        + "domains. The dataset contains 20 entity pairs from each of these domains and, for each "
                        + "entity pair, a ranked list with 50 relationship paths. It also contains entity ratings "
                        + "and property relevance scores for the entities and properties used in the paths.");

        (new File(RDF_ROOT)).mkdirs();

        try (OutputStream out = new FileOutputStream(LOCAL_VOID_NAME);) {
            RDFDataMgr.write(out, voidData, Lang.TURTLE);
        } finally {
        }

        try (InputStream in = new FileInputStream(LOCAL_VOID_NAME)) {
            HostProxy.upload(HOST_ADDR, USERNAME, PASSWORD, REMOTE_VOID_NAME, in);
        } finally {
        }

    }

    private static void createOntology() throws FileNotFoundException, IOException, GeneralSecurityException {
        Model ontology = ModelFactory.createDefaultModel();
        ontology.setNsPrefix("", ONTOLOGY_NS);

        Resource pathElementClass = ontology.createResource(ONTOLOGY_NS + "PathElement", RDFS.Class);
        Property score = ontology.createProperty(ONTOLOGY_NS + "score");
        score.addProperty(RDFS.domain, pathElementClass);
        score.addProperty(RDFS.range, XSD.xdouble);
        Resource entityClass = ontology.createResource(ONTOLOGY_NS + "Entity", RDFS.Class);
        entityClass.addProperty(RDFS.subClassOf, pathElementClass);
        Resource propertyClass = ontology.createResource(ONTOLOGY_NS + "Property", RDFS.Class);
        propertyClass.addProperty(RDFS.subClassOf, pathElementClass);

        Resource entityPairClass = ontology.createResource(ONTOLOGY_NS + "EntityPair", RDFS.Class);
        Property first = ontology.createProperty(ONTOLOGY_NS + "first");
        Property second = ontology.createProperty(ONTOLOGY_NS + "second");
        first.addProperty(RDFS.domain, entityPairClass);
        first.addProperty(RDFS.range, entityClass);
        second.addProperty(RDFS.domain, entityPairClass);
        second.addProperty(RDFS.range, entityClass);

        Property hasPath = ontology.createProperty(ONTOLOGY_NS + "hasPath");
        hasPath.addProperty(RDFS.domain, entityPairClass);
        hasPath.addProperty(RDFS.range, ontology.createResource(ONTOLOGY_NS + "Path", RDFS.Class));

        Resource pathClass = ontology.createResource(ONTOLOGY_NS + "Path", RDFS.Class);
        score.addProperty(RDFS.domain, pathClass);
        Property rank = ontology.createProperty(ONTOLOGY_NS + "rank");
        rank.addProperty(RDFS.domain, pathClass);
        rank.addProperty(RDFS.range, XSD.xlong);
        Property expression = ontology.createProperty(ONTOLOGY_NS + "expression");
        expression.addProperty(RDFS.domain, pathClass);
        expression.addProperty(RDFS.range, XSD.xstring);

        Property hasListOfPathElements = ontology.createProperty(ONTOLOGY_NS + "hasListOfpathElements");
        hasListOfPathElements.addProperty(RDFS.domain, pathClass);
        hasListOfPathElements.addProperty(RDFS.range, ontology.createResource(ONTOLOGY_NS + "ListOfPathElements", RDFS.Class));

        Resource listOfPathElementsClass = ontology.createResource(ONTOLOGY_NS + "ListOfPathElements", RDFS.Class);
        listOfPathElementsClass.addProperty(RDFS.subClassOf, RDF.List);
        Property firstPathElement = ontology.createProperty(ONTOLOGY_NS + "firstPathElement");
        firstPathElement.addProperty(RDFS.subPropertyOf, RDF.first);
        firstPathElement.addProperty(RDFS.domain, listOfPathElementsClass);
        firstPathElement.addProperty(RDFS.range, pathElementClass);
        Property restOfPathElements = ontology.createProperty(ONTOLOGY_NS + "restOfPathElements");
        restOfPathElements.addProperty(RDFS.subPropertyOf, RDF.rest);
        restOfPathElements.addProperty(RDFS.domain, listOfPathElementsClass);
        restOfPathElements.addProperty(RDFS.range, listOfPathElementsClass);

        (new File(RDF_ROOT + "/ontology")).mkdirs();

        try (OutputStream out = new FileOutputStream(LOCAL_ONTOLOGY_NAME)) {
            RDFDataMgr.write(out, ontology, Lang.RDFXML);
        } finally {
        }

        try (InputStream in = new FileInputStream(LOCAL_ONTOLOGY_NAME)) {
            HostProxy.upload(HOST_ADDR, USERNAME, PASSWORD, REMOTE_ONTOLOGY_NAME, in);
        } finally {
        }

    }

    private static void createDataset() throws FileNotFoundException, IOException {
        Model dataset = ModelFactory.createDefaultModel();
        dataset.setNsPrefix("", DATA_NS);
        dataset.setNsPrefix("align", ALIGN_NS);

        Resource alignmentClass = dataset.createResource(ALIGN_NS + "Alignment");
        Resource alignment = dataset.createResource(alignmentClass);

        for (Map.Entry<String, ArrayList<Pair>> entry : MOVIE_ENTITY_MAPPING.entrySet())
            for (Pair pair : entry.getValue()) {
                Resource cell = dataset.createResource(ONTOLOGY_NS + pair.label, dataset.createResource(ALIGN_NS + "Cell"));
                cell.addProperty(dataset.createProperty(ALIGN_NS + "entity1"), pair.entity1);
                cell.addProperty(dataset.createProperty(ALIGN_NS + "entity2"), pair.entity2);
                cell.addProperty(dataset.createProperty(ALIGN_NS + "relation"), "=");
                alignment.addProperty(dataset.createProperty(ALIGN_NS + "map"), cell.as(RDFNode.class));
            }

        for (Map.Entry<String, ArrayList<Pair>> entry : MUSIC_ENTITY_MAPPINGS.entrySet())
            for (Pair pair : entry.getValue()) {
                Resource cell = dataset.createResource(ONTOLOGY_NS + pair.label, dataset.createResource(ALIGN_NS + "Cell"));
                cell.addProperty(dataset.createProperty(ALIGN_NS + "entity1"), pair.entity1);
                cell.addProperty(dataset.createProperty(ALIGN_NS + "entity2"), pair.entity2);
                cell.addProperty(dataset.createProperty(ALIGN_NS + "relation"), "=");
                alignment.addProperty(dataset.createProperty(ALIGN_NS + "map"), cell.as(RDFNode.class));
            }

        (new File(RDF_ROOT)).mkdirs();

        try (OutputStream out = new FileOutputStream(TURTLE_SERIALIZATION_NAME);) {
            RDFDataMgr.write(out, dataset, Lang.TURTLE);
        } finally {
        }

        try (OutputStream out = new FileOutputStream(XML_SERIALIZATION_NAME);) {
            RDFDataMgr.write(out, dataset, Lang.TURTLE);
        } finally {
        }

        try (OutputStream out = new FileOutputStream(JSON_SERIALIZATION_NAME);) {
            RDFDataMgr.write(out, dataset, Lang.JSONLD);
        } finally {
        }

        DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(DATASET_URL);
        accessor.putModel(dataset);
    }

}
