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
import org.apache.log4j.PropertyConfigurator;
import uff.ic.swlab.commons.util.Host;

public class Main {

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
        PropertyConfigurator.configure("./resources/conf/log4j.properties");
        MyConfig.configure("./resources/conf/entityrelatednesstestdata.properties");

        createVoID();
        createOntology();
        createDataset();

        System.out.println("Fim.");
    }

    private static void createVoID() throws FileNotFoundException, IOException {
        Model voidVocab = ModelFactory.createDefaultModel();
        voidVocab.setNsPrefix("", MyConfig.DATA_NS);
        voidVocab.setNsPrefix("align", MyConfig.ALIGN_NS);
        voidVocab.read("http://vocab.deri.ie/void#");

        Model voidData = ModelFactory.createDefaultModel();
        voidData.createResource(MyConfig.ONTOLOGY_NS + "EntityRelatednessTestData", VOID.Dataset)
                .addProperty(DCTerms.description, "The entity relatedness problem refers to the question of "
                        + "computing the relationship paths that better describe the connectivity between a "
                        + "given entity pair. This dataset supports the evaluation of approaches that address "
                        + "the entity relatedness problem. It covers two familiar domains, music and movies, and "
                        + "uses data available in IMDb and last.fm, which are popular reference datasets in these "
                        + "domains. The dataset contains 20 entity pairs from each of these domains and, for each "
                        + "entity pair, a ranked list with 50 relationship paths. It also contains entity ratings "
                        + "and property relevance scores for the entities and properties used in the paths.");

        (new File(MyConfig.RDF_ROOT)).mkdirs();

        try (OutputStream out = new FileOutputStream(MyConfig.LOCAL_VOID_NAME);) {
            RDFDataMgr.write(out, voidData, Lang.TURTLE);
        } finally {
        }

        try (InputStream in = new FileInputStream(MyConfig.LOCAL_VOID_NAME)) {
            Host.uploadViaFTP(MyConfig.HOST_ADDR, MyConfig.USERNAME, MyConfig.PASSWORD, MyConfig.REMOTE_VOID_NAME, in);
        } finally {
        }

    }

    private static void createOntology() throws FileNotFoundException, IOException, GeneralSecurityException {
        Model ontology = ModelFactory.createDefaultModel();
        ontology.setNsPrefix("", MyConfig.ONTOLOGY_NS);

        Resource pathElementClass = ontology.createResource(MyConfig.ONTOLOGY_NS + "PathElement", RDFS.Class);
        Property score = ontology.createProperty(MyConfig.ONTOLOGY_NS + "score");
        score.addProperty(RDFS.domain, pathElementClass);
        score.addProperty(RDFS.range, XSD.xdouble);
        Resource entityClass = ontology.createResource(MyConfig.ONTOLOGY_NS + "Entity", RDFS.Class);
        entityClass.addProperty(RDFS.subClassOf, pathElementClass);
        Resource propertyClass = ontology.createResource(MyConfig.ONTOLOGY_NS + "Property", RDFS.Class);
        propertyClass.addProperty(RDFS.subClassOf, pathElementClass);

        Resource entityPairClass = ontology.createResource(MyConfig.ONTOLOGY_NS + "EntityPair", RDFS.Class);
        Property first = ontology.createProperty(MyConfig.ONTOLOGY_NS + "first");
        Property second = ontology.createProperty(MyConfig.ONTOLOGY_NS + "second");
        first.addProperty(RDFS.domain, entityPairClass);
        first.addProperty(RDFS.range, entityClass);
        second.addProperty(RDFS.domain, entityPairClass);
        second.addProperty(RDFS.range, entityClass);

        Property hasPath = ontology.createProperty(MyConfig.ONTOLOGY_NS + "hasPath");
        hasPath.addProperty(RDFS.domain, entityPairClass);
        hasPath.addProperty(RDFS.range, ontology.createResource(MyConfig.ONTOLOGY_NS + "Path", RDFS.Class));

        Resource pathClass = ontology.createResource(MyConfig.ONTOLOGY_NS + "Path", RDFS.Class);
        score.addProperty(RDFS.domain, pathClass);
        Property rank = ontology.createProperty(MyConfig.ONTOLOGY_NS + "rank");
        rank.addProperty(RDFS.domain, pathClass);
        rank.addProperty(RDFS.range, XSD.xlong);
        Property expression = ontology.createProperty(MyConfig.ONTOLOGY_NS + "expression");
        expression.addProperty(RDFS.domain, pathClass);
        expression.addProperty(RDFS.range, XSD.xstring);

        Property hasListOfPathElements = ontology.createProperty(MyConfig.ONTOLOGY_NS + "hasListOfpathElements");
        hasListOfPathElements.addProperty(RDFS.domain, pathClass);
        hasListOfPathElements.addProperty(RDFS.range, ontology.createResource(MyConfig.ONTOLOGY_NS + "ListOfPathElements", RDFS.Class));

        Resource listOfPathElementsClass = ontology.createResource(MyConfig.ONTOLOGY_NS + "ListOfPathElements", RDFS.Class);
        listOfPathElementsClass.addProperty(RDFS.subClassOf, RDF.List);
        Property firstPathElement = ontology.createProperty(MyConfig.ONTOLOGY_NS + "firstPathElement");
        firstPathElement.addProperty(RDFS.subPropertyOf, RDF.first);
        firstPathElement.addProperty(RDFS.domain, listOfPathElementsClass);
        firstPathElement.addProperty(RDFS.range, pathElementClass);
        Property restOfPathElements = ontology.createProperty(MyConfig.ONTOLOGY_NS + "restOfPathElements");
        restOfPathElements.addProperty(RDFS.subPropertyOf, RDF.rest);
        restOfPathElements.addProperty(RDFS.domain, listOfPathElementsClass);
        restOfPathElements.addProperty(RDFS.range, listOfPathElementsClass);

        (new File(MyConfig.RDF_ROOT + "/ontology")).mkdirs();

        try (OutputStream out = new FileOutputStream(MyConfig.LOCAL_ONTOLOGY_NAME)) {
            RDFDataMgr.write(out, ontology, Lang.RDFXML);
        } finally {
        }

        try (InputStream in = new FileInputStream(MyConfig.LOCAL_ONTOLOGY_NAME)) {
            Host.uploadViaFTP(MyConfig.HOST_ADDR, MyConfig.USERNAME, MyConfig.PASSWORD, MyConfig.REMOTE_ONTOLOGY_NAME, in);
        } finally {
        }

    }

    private static void createDataset() throws FileNotFoundException, IOException {
        Model dataset = ModelFactory.createDefaultModel();
        dataset.setNsPrefix("", MyConfig.DATA_NS);
        dataset.setNsPrefix("align", MyConfig.ALIGN_NS);

        Resource alignmentClass = dataset.createResource(MyConfig.ALIGN_NS + "Alignment");
        Resource alignment = dataset.createResource(alignmentClass);

        for (Map.Entry<String, ArrayList<Pair>> entry : MOVIE_ENTITY_MAPPING.entrySet())
            for (Pair pair : entry.getValue()) {
                Resource cell = dataset.createResource(MyConfig.ONTOLOGY_NS + pair.label, dataset.createResource(MyConfig.ALIGN_NS + "Cell"));
                cell.addProperty(dataset.createProperty(MyConfig.ALIGN_NS + "entity1"), pair.entity1);
                cell.addProperty(dataset.createProperty(MyConfig.ALIGN_NS + "entity2"), pair.entity2);
                cell.addProperty(dataset.createProperty(MyConfig.ALIGN_NS + "relation"), "=");
                alignment.addProperty(dataset.createProperty(MyConfig.ALIGN_NS + "map"), cell.as(RDFNode.class));
            }

        for (Map.Entry<String, ArrayList<Pair>> entry : MUSIC_ENTITY_MAPPINGS.entrySet())
            for (Pair pair : entry.getValue()) {
                Resource cell = dataset.createResource(MyConfig.ONTOLOGY_NS + pair.label, dataset.createResource(MyConfig.ALIGN_NS + "Cell"));
                cell.addProperty(dataset.createProperty(MyConfig.ALIGN_NS + "entity1"), pair.entity1);
                cell.addProperty(dataset.createProperty(MyConfig.ALIGN_NS + "entity2"), pair.entity2);
                cell.addProperty(dataset.createProperty(MyConfig.ALIGN_NS + "relation"), "=");
                alignment.addProperty(dataset.createProperty(MyConfig.ALIGN_NS + "map"), cell.as(RDFNode.class));
            }

        (new File(MyConfig.RDF_ROOT)).mkdirs();

        try (OutputStream out = new FileOutputStream(MyConfig.TURTLE_SERIALIZATION_NAME);) {
            RDFDataMgr.write(out, dataset, Lang.TURTLE);
        } finally {
        }

        try (OutputStream out = new FileOutputStream(MyConfig.XML_SERIALIZATION_NAME);) {
            RDFDataMgr.write(out, dataset, Lang.TURTLE);
        } finally {
        }

        try (OutputStream out = new FileOutputStream(MyConfig.JSON_SERIALIZATION_NAME);) {
            RDFDataMgr.write(out, dataset, Lang.JSONLD);
        } finally {
        }

        DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(MyConfig.DATASET_URL);
        accessor.putModel(dataset);
    }

}
