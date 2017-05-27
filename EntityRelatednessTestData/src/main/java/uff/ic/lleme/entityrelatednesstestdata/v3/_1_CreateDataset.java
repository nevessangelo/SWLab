package uff.ic.lleme.entityrelatednesstestdata.v3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;
import org.apache.log4j.PropertyConfigurator;
import uff.ic.lleme.entityrelatednesstestdata.v3.model.MovieClassMapping;
import uff.ic.lleme.entityrelatednesstestdata.v3.model.MovieEntityMappings;
import uff.ic.lleme.entityrelatednesstestdata.v3.model.MovieEntityPairs;
import uff.ic.lleme.entityrelatednesstestdata.v3.model.MoviePropertyRelevanceScore;
import uff.ic.lleme.entityrelatednesstestdata.v3.model.MovieRankedPaths;
import uff.ic.lleme.entityrelatednesstestdata.v3.model.MovieScores;
import uff.ic.lleme.entityrelatednesstestdata.v3.model.MusicClassMapping;
import uff.ic.lleme.entityrelatednesstestdata.v3.model.MusicEntityMappings;
import uff.ic.lleme.entityrelatednesstestdata.v3.model.MusicEntityPairs;
import uff.ic.lleme.entityrelatednesstestdata.v3.model.MusicPropertyRelevanceScore;
import uff.ic.lleme.entityrelatednesstestdata.v3.model.MusicRankedPaths;
import uff.ic.lleme.entityrelatednesstestdata.v3.model.MusicScores;
import uff.ic.lleme.entityrelatednesstestdata.v3.model.Pair;
import uff.ic.lleme.entityrelatednesstestdata.v3.model.Score;
import uff.ic.lleme.entityrelatednesstestdata.v3.vocabulary.EREL;
import uff.ic.swlab.commons.util.Host;

public class _1_CreateDataset {

    private static final MusicScores MUSIC_SCORES = new MusicScores();
    private static final MusicRankedPaths MUSIC_RANKED_SCORES = new MusicRankedPaths();
    private static final MusicPropertyRelevanceScore MUSIC_PROPERTY_RELEVANCE_SCORE = new MusicPropertyRelevanceScore();
    private static final MusicEntityMappings MUSIC_ENTITY_MAPPINGS = new MusicEntityMappings();
    private static final MusicClassMapping MUSIC_CLASS_MAPPING = new MusicClassMapping();
    private static final MusicEntityPairs MUSIC_ENTITY_PAIRS = new MusicEntityPairs();

    private static final MovieScores MOVIE_SCORES = new MovieScores();
    private static final MovieRankedPaths MOVIE_RANKED_PATHS = new MovieRankedPaths();
    private static final MoviePropertyRelevanceScore MOVIE_PROPERTY_RELEVANCE_SCORE = new MoviePropertyRelevanceScore();
    private static final MovieEntityMappings MOVIE_ENTITY_MAPPINGS = new MovieEntityMappings();
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

        Resource pathElement = ontology.createResource(Config.ONTOLOGY_NS + "PathElement", RDFS.Class);
        Property score = ontology.createProperty(Config.ONTOLOGY_NS + "score");
        score.addProperty(RDFS.domain, pathElement);
        score.addProperty(RDFS.range, XSD.xdouble);
        Resource entity = ontology.createResource(Config.ONTOLOGY_NS + "Entity", RDFS.Class);
        entity.addProperty(RDFS.subClassOf, pathElement);
        Resource propertyClass = ontology.createResource(Config.ONTOLOGY_NS + "Property", RDFS.Class);
        propertyClass.addProperty(RDFS.subClassOf, pathElement);
        Property category = ontology.createProperty(Config.ONTOLOGY_NS + "category");
        category.addProperty(RDFS.domain, entity);
        category.addProperty(RDFS.range, XSD.xstring);
        score.addProperty(RDFS.domain, entity);

        Resource entityPair = ontology.createResource(Config.ONTOLOGY_NS + "EntityPair", RDFS.Class);
        Property first = ontology.createProperty(Config.ONTOLOGY_NS + "first");
        Property second = ontology.createProperty(Config.ONTOLOGY_NS + "second");
        first.addProperty(RDFS.domain, entityPair);
        first.addProperty(RDFS.range, entity);
        second.addProperty(RDFS.domain, entityPair);
        second.addProperty(RDFS.range, entity);

        Property hasPath = ontology.createProperty(Config.ONTOLOGY_NS + "hasPath");
        hasPath.addProperty(RDFS.domain, entityPair);
        hasPath.addProperty(RDFS.range, ontology.createResource(Config.ONTOLOGY_NS + "Path", RDFS.Class));

        Resource path = ontology.createResource(Config.ONTOLOGY_NS + "Path", RDFS.Class);
        score.addProperty(RDFS.domain, path);
        Property rank = ontology.createProperty(Config.ONTOLOGY_NS + "rank");
        rank.addProperty(RDFS.domain, path);
        rank.addProperty(RDFS.range, XSD.xlong);
        Property expression = ontology.createProperty(Config.ONTOLOGY_NS + "expression");
        expression.addProperty(RDFS.domain, path);
        expression.addProperty(RDFS.range, XSD.xstring);

        Property hasListOfPathElements = ontology.createProperty(Config.ONTOLOGY_NS + "hasListOfpathElements");
        hasListOfPathElements.addProperty(RDFS.domain, path);
        hasListOfPathElements.addProperty(RDFS.range, ontology.createResource(Config.ONTOLOGY_NS + "ListOfPathElements", RDFS.Class));

        Resource listOfPathElements = ontology.createResource(Config.ONTOLOGY_NS + "ListOfPathElements", RDFS.Class);
        listOfPathElements.addProperty(RDFS.subClassOf, RDF.List);
        Property firstPathElement = ontology.createProperty(Config.ONTOLOGY_NS + "firstPathElement");
        firstPathElement.addProperty(RDFS.subPropertyOf, RDF.first);
        firstPathElement.addProperty(RDFS.domain, listOfPathElements);
        firstPathElement.addProperty(RDFS.range, pathElement);
        Property restOfPathElements = ontology.createProperty(Config.ONTOLOGY_NS + "restOfPathElements");
        restOfPathElements.addProperty(RDFS.subPropertyOf, RDF.rest);
        restOfPathElements.addProperty(RDFS.domain, listOfPathElements);
        restOfPathElements.addProperty(RDFS.range, listOfPathElements);

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
        UrlValidator validator = new UrlValidator();
        Integer counter = 0;

        Model dataset = ModelFactory.createDefaultModel();
        dataset.setNsPrefix("", Config.DATA_NS);
        dataset.setNsPrefix("align", Config.ALIGN_NS);
        dataset.setNsPrefix("erel", Config.ONTOLOGY_NS);

        List<Set<Map.Entry<String, ArrayList<Pair>>>> sets = new ArrayList<>();
        sets.add(MOVIE_ENTITY_MAPPINGS.entrySet());
        sets.add(MUSIC_ENTITY_MAPPINGS.entrySet());

        for (Set<Map.Entry<String, ArrayList<Pair>>> mappings : sets)
            for (Map.Entry<String, ArrayList<Pair>> mapping : mappings)
                for (Pair ep : mapping.getValue()) {

                    String entityURI = Config.DATA_NS + ep.label.trim().replaceAll("  ", " ");
                    String entity1URI = ep.entity1.trim().replaceAll("  ", " ").replaceFirst("^ttp://", "http://");
                    String entity2URI = ep.entity2.trim().replaceAll("  ", " ").replaceFirst("^ttp://", "http://");

                    if (!validator.isValid(entityURI)) {
                        String encodedURI = Config.DATA_NS + URLEncoder.encode(ep.label.trim().replaceAll("  ", " "), "UTF-8");
                        System.out.println("Mappings//// Warning! BadFormedURI <" + entityURI + ">. Replaced with <" + encodedURI + ">.");
                        entityURI = encodedURI;
                    }
                    if (!validator.isValid(entity1URI))
                        System.out.println("Mappings//// BadFormedURI (entity1): <" + entity1URI + "> (Entity: " + entityURI + ")");
                    if (!validator.isValid(entity2URI))
                        System.out.println("Mappings//// BadFormedURI (entity2): <" + entity2URI + "> (Entity: " + entityURI + ")");

                    if (validator.isValid(entityURI) && validator.isValid(entity1URI) && validator.isValid(entity2URI)) {
                        Resource entity = dataset.createResource(entityURI, EREL.Entity)
                                .addProperty(EREL.category, ep.type)
                                .addProperty(OWL.sameAs, dataset.createResource(ep.entity1))
                                .addProperty(OWL.sameAs, dataset.createResource(ep.entity2));
                        dataset.createResource(ep.entity1)
                                .addProperty(OWL.sameAs, entity)
                                .addProperty(OWL.sameAs, dataset.createResource(ep.entity2));
                        dataset.createResource(ep.entity2)
                                .addProperty(OWL.sameAs, entity)
                                .addProperty(OWL.sameAs, dataset.createResource(ep.entity1));
                    }

                    List<List<Pair>> lists = new ArrayList<>();
                    lists.add(MUSIC_ENTITY_PAIRS);
                    lists.add(MOVIE_ENTITY_PAIRS);

                    MOVIE_RANKED_PATHS.putAll(MUSIC_RANKED_SCORES);
                    MovieRankedPaths paths = MOVIE_RANKED_PATHS;

                    List<Score> rank = paths.getRank(ep.entity1.trim(), ep.entity2.trim());
                    for (Score s : rank) {

                    }

                }

        List<List<Pair>> lists = new ArrayList<>();
        lists.add(MUSIC_ENTITY_PAIRS);
        lists.add(MOVIE_ENTITY_PAIRS);

        MOVIE_RANKED_PATHS.putAll(MUSIC_RANKED_SCORES);
        MovieRankedPaths paths = MOVIE_RANKED_PATHS;

        for (List<Pair> pairs : lists)
            for (Pair ep : pairs) {
                String entity1URI = ep.entity1.trim().replaceAll("  ", " ").replaceFirst("^ttp://", "http://");
                String entity2URI = ep.entity2.trim().replaceAll("  ", " ").replaceFirst("^ttp://", "http://");

                if (!validator.isValid(entity1URI)) {
                    String encodedURI = Config.DATA_NS + URLEncoder.encode(ep.label.trim().replaceAll("  ", " "), "UTF-8");
                    System.out.println("Pairs//// Warning! BadFormedURI <" + entity1URI + ">. Replaced with <" + encodedURI + ">.");
                    entity1URI = encodedURI;
                }

                if (!validator.isValid(entity2URI)) {
                    String encodedURI = Config.DATA_NS + URLEncoder.encode(ep.label.trim().replaceAll("  ", " "), "UTF-8");
                    System.out.println("Pairs//// Warning! BadFormedURI <" + entity2URI + ">. Replaced with <" + encodedURI + ">.");
                    entity2URI = encodedURI;
                }
                if (validator.isValid(entity1URI) && validator.isValid(entity2URI)) {
                    Resource entitypair = dataset.createResource(Config.DATA_NS + counter++, EREL.EntityPair)
                            .addProperty(EREL.first, dataset.createResource(entity1URI, EREL.Entity))
                            .addProperty(EREL.second, dataset.createResource(entity2URI, EREL.Entity));

                    List<Score> rank = paths.getRank(entity1URI, entity2URI);
                    for (Score s : rank) {

                    }

                }
            };

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
