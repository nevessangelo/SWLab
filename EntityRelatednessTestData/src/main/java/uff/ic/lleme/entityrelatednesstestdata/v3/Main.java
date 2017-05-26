package uff.ic.lleme.entityrelatednesstestdata.v3;

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
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
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

    public static void main(String[] args) throws FileNotFoundException, IOException, GeneralSecurityException, Exception {
        PropertyConfigurator.configure("./resources/conf/log4j.properties");
        Config.configure("./resources/conf/entityrelatednesstestdata.properties");

        createOntology();
        createDataset();

        System.out.println("Fim.");
    }

    private static void createOntology() throws FileNotFoundException, IOException, GeneralSecurityException, Exception {
        Model ontology = ModelFactory.createDefaultModel();
        ontology.setNsPrefix("", Config.ONTOLOGY_NS);

        Resource pathElementClass = ontology.createResource(Config.ONTOLOGY_NS + "PathElement", RDFS.Class);
        Property score = ontology.createProperty(Config.ONTOLOGY_NS + "score");
        score.addProperty(RDFS.domain, pathElementClass);
        score.addProperty(RDFS.range, XSD.xdouble);
        Resource entityClass = ontology.createResource(Config.ONTOLOGY_NS + "Entity", RDFS.Class);
        entityClass.addProperty(RDFS.subClassOf, pathElementClass);
        Resource propertyClass = ontology.createResource(Config.ONTOLOGY_NS + "Property", RDFS.Class);
        propertyClass.addProperty(RDFS.subClassOf, pathElementClass);

        Resource entityPairClass = ontology.createResource(Config.ONTOLOGY_NS + "EntityPair", RDFS.Class);
        Property first = ontology.createProperty(Config.ONTOLOGY_NS + "first");
        Property second = ontology.createProperty(Config.ONTOLOGY_NS + "second");
        first.addProperty(RDFS.domain, entityPairClass);
        first.addProperty(RDFS.range, entityClass);
        second.addProperty(RDFS.domain, entityPairClass);
        second.addProperty(RDFS.range, entityClass);

        Property hasPath = ontology.createProperty(Config.ONTOLOGY_NS + "hasPath");
        hasPath.addProperty(RDFS.domain, entityPairClass);
        hasPath.addProperty(RDFS.range, ontology.createResource(Config.ONTOLOGY_NS + "Path", RDFS.Class));

        Resource pathClass = ontology.createResource(Config.ONTOLOGY_NS + "Path", RDFS.Class);
        score.addProperty(RDFS.domain, pathClass);
        Property rank = ontology.createProperty(Config.ONTOLOGY_NS + "rank");
        rank.addProperty(RDFS.domain, pathClass);
        rank.addProperty(RDFS.range, XSD.xlong);
        Property expression = ontology.createProperty(Config.ONTOLOGY_NS + "expression");
        expression.addProperty(RDFS.domain, pathClass);
        expression.addProperty(RDFS.range, XSD.xstring);

        Property hasListOfPathElements = ontology.createProperty(Config.ONTOLOGY_NS + "hasListOfpathElements");
        hasListOfPathElements.addProperty(RDFS.domain, pathClass);
        hasListOfPathElements.addProperty(RDFS.range, ontology.createResource(Config.ONTOLOGY_NS + "ListOfPathElements", RDFS.Class));

        Resource listOfPathElementsClass = ontology.createResource(Config.ONTOLOGY_NS + "ListOfPathElements", RDFS.Class);
        listOfPathElementsClass.addProperty(RDFS.subClassOf, RDF.List);
        Property firstPathElement = ontology.createProperty(Config.ONTOLOGY_NS + "firstPathElement");
        firstPathElement.addProperty(RDFS.subPropertyOf, RDF.first);
        firstPathElement.addProperty(RDFS.domain, listOfPathElementsClass);
        firstPathElement.addProperty(RDFS.range, pathElementClass);
        Property restOfPathElements = ontology.createProperty(Config.ONTOLOGY_NS + "restOfPathElements");
        restOfPathElements.addProperty(RDFS.subPropertyOf, RDF.rest);
        restOfPathElements.addProperty(RDFS.domain, listOfPathElementsClass);
        restOfPathElements.addProperty(RDFS.range, listOfPathElementsClass);

        (new File(Config.LOCAL_ONTOLOGY_NAME)).getParentFile().mkdirs();

        try (OutputStream out = new FileOutputStream(Config.LOCAL_ONTOLOGY_NAME)) {
            RDFDataMgr.write(out, ontology, Lang.RDFXML);
        }

        try (InputStream in = new FileInputStream(Config.LOCAL_ONTOLOGY_NAME)) {
            Host.uploadViaFTP(Config.HOST_ADDR, Config.USERNAME, Config.PASSWORD, Config.REMOTE_ONTOLOGY_NAME, in);
            Host.mkDirsViaFTP(Config.HOST_ADDR, Config.USERNAME, Config.PASSWORD, Config.REMOTE_ONTOLOGY_NAME, in);
        }

    }

    private static void createDataset() throws FileNotFoundException, IOException, Exception {
        Model dataset = ModelFactory.createDefaultModel();
        dataset.setNsPrefix("", Config.DATA_NS);
        dataset.setNsPrefix("align", Config.ALIGN_NS);
        dataset.setNsPrefix("erel", Config.ONTOLOGY_NS);

        Resource alignmentClass = dataset.createResource(Config.ALIGN_NS + "Alignment");
        Resource alignment = dataset.createResource(alignmentClass);

        for (Map.Entry<String, ArrayList<Pair>> entry : MOVIE_ENTITY_MAPPING.entrySet())
            for (Pair pair : entry.getValue()) {
                Resource cell = dataset.createResource(Config.ONTOLOGY_NS + pair.label, dataset.createResource(Config.ALIGN_NS + "Cell"));
                cell.addProperty(dataset.createProperty(Config.ALIGN_NS + "entity1"), pair.entity1);
                cell.addProperty(dataset.createProperty(Config.ALIGN_NS + "entity2"), pair.entity2);
                cell.addProperty(dataset.createProperty(Config.ALIGN_NS + "relation"), "=");
                alignment.addProperty(dataset.createProperty(Config.ALIGN_NS + "map"), cell.as(RDFNode.class));
            }

        for (Map.Entry<String, ArrayList<Pair>> entry : MUSIC_ENTITY_MAPPINGS.entrySet())
            for (Pair pair : entry.getValue()) {
                Resource cell = dataset.createResource(Config.ONTOLOGY_NS + pair.label, dataset.createResource(Config.ALIGN_NS + "Cell"));
                cell.addProperty(dataset.createProperty(Config.ALIGN_NS + "entity1"), pair.entity1);
                cell.addProperty(dataset.createProperty(Config.ALIGN_NS + "entity2"), pair.entity2);
                cell.addProperty(dataset.createProperty(Config.ALIGN_NS + "relation"), "=");
                alignment.addProperty(dataset.createProperty(Config.ALIGN_NS + "map"), cell.as(RDFNode.class));
            }

        (new File(Config.XML_DUMP_NAME)).getParentFile().mkdirs();

        try (OutputStream out = new FileOutputStream(Config.XML_DUMP_NAME);) {
            RDFDataMgr.write(out, dataset, Lang.RDFXML);
        }

        try (OutputStream out = new FileOutputStream(Config.TURTLE_DUMP_NAME);) {
            RDFDataMgr.write(out, dataset, Lang.TURTLE);
        }

        try (OutputStream out = new FileOutputStream(Config.JSON_DUMP_NAME);) {
            RDFDataMgr.write(out, dataset, Lang.RDFJSON);
        }

        try (OutputStream out = new FileOutputStream(Config.NTRIPLES_DUMP_NAME);) {
            RDFDataMgr.write(out, dataset, Lang.NTRIPLES);
        }

        try (InputStream in = new FileInputStream(Config.XML_DUMP_NAME)) {
            Host.uploadViaFTP(Config.HOST_ADDR, Config.USERNAME, Config.PASSWORD, Config.XML_REMOTE_DUMP_NAME, in);
        }

        try (InputStream in = new FileInputStream(Config.TURTLE_DUMP_NAME)) {
            Host.uploadViaFTP(Config.HOST_ADDR, Config.USERNAME, Config.PASSWORD, Config.TURTLE_REMOTE_DUMP_NAME, in);
        }

        try (InputStream in = new FileInputStream(Config.JSON_DUMP_NAME)) {
            Host.uploadViaFTP(Config.HOST_ADDR, Config.USERNAME, Config.PASSWORD, Config.JSON_REMOTE_DUMP_NAME, in);
        }

        try (InputStream in = new FileInputStream(Config.NTRIPLES_DUMP_NAME)) {
            Host.uploadViaFTP(Config.HOST_ADDR, Config.USERNAME, Config.PASSWORD, Config.NTRIPLES_REMOTE_DUMP_NAME, in);
        }

        DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(Config.DATASET_URL);
        accessor.putModel(dataset);
    }

}
