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
import java.util.UUID;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;
import org.apache.log4j.PropertyConfigurator;
import uff.ic.lleme.entityrelatednesstestdata.v3.model.DB;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.MovieClassMapping;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.MovieEntityMappings;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.MovieEntityPairs;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.MoviePropertyRelevanceScore;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.MovieRankedPaths;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.MovieScores;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.MusicClassMapping;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.MusicEntityMappings;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.MusicEntityPairs;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.MusicPropertyRelevanceScore;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.MusicRankedPaths;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.MusicScores;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.Pair;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.Score;
import uff.ic.lleme.entityrelatednesstestdata.v3.vocabulary.EREL;
import uff.ic.swlab.commons.util.Host;

public class Main {

    private static Model ontology = ModelFactory.createDefaultModel();
    private static Model dataset = ModelFactory.createDefaultModel();

    public static void main(String[] args) throws IOException, GeneralSecurityException, Exception {
        PropertyConfigurator.configure("./resources/conf/log4j.properties");
        Config.configure("./resources/conf/entityrelatednesstestdata.properties");

        prepareDB();

        createOntology();
        createDataset();

        exportOntology(ontology);
        exportDataset(dataset);
    }

    private static void prepareDB() {
        {
            MovieClassMapping MOVIE_CLASS_MAPPING = new MovieClassMapping();
            MusicClassMapping MUSIC_CLASS_MAPPING = new MusicClassMapping();

            MOVIE_CLASS_MAPPING.addAll(MUSIC_CLASS_MAPPING);
            MovieClassMapping categories = MOVIE_CLASS_MAPPING;

            MOVIE_CLASS_MAPPING = null;
            MUSIC_CLASS_MAPPING = null;

            for (Pair mapping : categories)
                try {
                    DB.Category category = DB.Categories.addCategory(mapping.label);
                    try {
                        category.addSameAs(mapping.entity1);
                    } catch (Exception e) {
                        System.out.println(String.format("Category error: invalid sameAs resource. (category -> %1s, resource -> %1s)", mapping.label, mapping.entity1));
                    }
                    try {
                        category.addSameAs(mapping.entity2);
                    } catch (Exception e) {
                        System.out.println(String.format("Category error: invalid sameAs resource. (category -> %1s, resource -> %1s)", mapping.label, mapping.entity2));
                    }
                } catch (Exception e) {
                    System.out.println(String.format("Category error: invalid label. (property -> %1s)", mapping.label));
                }
            System.out.println("");
        }

        {
            MovieEntityMappings MOVIE_ENTITY_MAPPINGS = new MovieEntityMappings();
            MusicEntityMappings MUSIC_ENTITY_MAPPINGS = new MusicEntityMappings();

            MOVIE_ENTITY_MAPPINGS.putAll(MUSIC_ENTITY_MAPPINGS);
            MovieEntityMappings mappings = MOVIE_ENTITY_MAPPINGS;

            MOVIE_ENTITY_MAPPINGS = null;
            MUSIC_ENTITY_MAPPINGS = null;

            MovieScores MOVIE_SCORES = new MovieScores();
            MusicScores MUSIC_SCORES = new MusicScores();

            MOVIE_SCORES.putAll(MUSIC_SCORES);
            MovieScores scores = MOVIE_SCORES;

            MOVIE_SCORES = null;
            MUSIC_SCORES = null;

            for (Map.Entry<String, ArrayList<Pair>> subset : mappings.entrySet())
                for (Pair mapping : subset.getValue())
                    try {
                        DB.Entity entity = DB.Entities.addEntity(mapping.label, mapping.type);
                        try {
                            entity.addSameAs(mapping.entity1);
                        } catch (Exception e) {
                            System.out.println(String.format("Entity error: invalid sameAs resource. (file -> %1s, label -> %1s, resource -> %1s)", subset.getKey(), mapping.label, mapping.entity1));
                        }
                        try {
                            entity.addSameAs(mapping.entity2);
                        } catch (Exception e) {
                            System.out.println(String.format("Entity error: invalid sameAs resource. (file -> %1s, label -> %1s, resource -> %1s)", subset.getKey(), mapping.label, mapping.entity2));
                        }
                    } catch (Exception e) {
                        System.out.println(String.format("Entity error: invalid label or category. (file -> %1s, label -> %1s, category -> %1s)", subset.getKey(), mapping.label, mapping.type));
                    }
        }

        {
            MoviePropertyRelevanceScore MOVIE_PROPERTY_RELEVANCE_SCORE = new MoviePropertyRelevanceScore();
            MusicPropertyRelevanceScore MUSIC_PROPERTY_RELEVANCE_SCORE = new MusicPropertyRelevanceScore();

            MOVIE_PROPERTY_RELEVANCE_SCORE.putAll(MUSIC_PROPERTY_RELEVANCE_SCORE);
            MoviePropertyRelevanceScore properties = MOVIE_PROPERTY_RELEVANCE_SCORE;

            MOVIE_PROPERTY_RELEVANCE_SCORE = null;
            MUSIC_PROPERTY_RELEVANCE_SCORE = null;

            for (Map.Entry<String, Double> property : properties.entrySet())
                try {
                    DB.Properties.addProperty(property.getKey(), property.getValue());
                } catch (Exception e) {
                    System.out.println(String.format("Property error: invalid label or score. (label -> %1s, score -> %1s)", property.getKey(), property.getValue()));
                }
        }

        {
            MovieEntityPairs MOVIE_ENTITY_PAIRS = new MovieEntityPairs();
            MusicEntityPairs MUSIC_ENTITY_PAIRS = new MusicEntityPairs();

            MOVIE_ENTITY_PAIRS.addAll(MUSIC_ENTITY_PAIRS);
            MovieEntityPairs pairs = MOVIE_ENTITY_PAIRS;

            MOVIE_ENTITY_PAIRS = null;
            MUSIC_ENTITY_PAIRS = null;

            MovieRankedPaths MOVIE_RANKED_PATHS = new MovieRankedPaths();
            MusicRankedPaths MUSIC_RANKED_PATHS = new MusicRankedPaths();

            MOVIE_RANKED_PATHS.putAll(MUSIC_RANKED_PATHS);
            MovieRankedPaths ranks = MOVIE_RANKED_PATHS;

            MOVIE_RANKED_PATHS = null;
            MUSIC_RANKED_PATHS = null;

            for (Pair pair : pairs)
                try {
                    DB.EntityPairs.addEntityPair(pair.entity1, pair.entity2);
                    for (Score rank : ranks.getRank(pair.entity1, pair.entity2))
                        try {
                            new DB.Path(pair.entity1, pair.entity2, Integer.parseInt(rank.label), rank.score, rank.description);
                        } catch (Exception e) {
                            System.out.println(e.toString());
                            System.out.println(String.format("Path error: invalid attributes. (rankPosition -> %1s, score -> %1s, expression -> %1s)", rank.label, rank.score, rank.description));
                        }
                } catch (Exception e) {
                    System.out.println(String.format("EntityPair error: invalid entity. (entity1 -> %1s, entity2 -> %1s)", pair.entity1, pair.entity2));
                }
        }

    }

    private static void createOntology() throws FileNotFoundException, IOException, GeneralSecurityException, Exception {

        ontology.setNsPrefix("", EREL.NS);

        Resource entity = ontology.createResource(EREL.Entity.getURI(), RDFS.Class)
                .addProperty(RDFS.label, "Entity")
                .addProperty(RDFS.comment, "A knowledge base B is a set of RDF triples. "
                        + "We say that an entity of B is a URI that occurs as a subject or "
                        + "object of a triple in B.");

        Resource category = ontology.createResource(EREL.Category.getURI(), RDFS.Class)
                .addProperty(RDFS.label, "Category")
                .addProperty(RDFS.comment, "The category of an entity.");
        Resource type = ontology.createProperty(EREL.type.getURI())
                .addProperty(RDFS.subPropertyOf, RDF.type)
                .addProperty(RDFS.domain, entity)
                .addProperty(RDFS.range, category)
                .addProperty(RDFS.label, "type")
                .addProperty(RDFS.comment, "The category of an entity.");

        Resource entityPair = ontology.createResource(EREL.EntityPair.getURI(), RDFS.Class)
                .addProperty(RDFS.label, "EntityPair")
                .addProperty(RDFS.comment, "A pair of entities for which one want to find"
                        + "connectivity profiles.");
        Resource entity1 = ontology.createProperty(EREL.entity1.getURI())
                .addProperty(RDFS.domain, entityPair)
                .addProperty(RDFS.range, entity)
                .addProperty(RDFS.label, "entity1")
                .addProperty(RDFS.comment, "The first entity of a pair of entities.");
        Resource entity2 = ontology.createProperty(EREL.entity2.getURI())
                .addProperty(RDFS.domain, entityPair)
                .addProperty(RDFS.range, entity)
                .addProperty(RDFS.label, "entity2")
                .addProperty(RDFS.comment, "The second entity of a pair of entities.");

        Resource path = ontology.createResource(EREL.Path.getURI(), RDFS.Class)
                .addProperty(RDFS.label, "Path")
                .addProperty(RDFS.comment, "A path π of a knowledge base B is an undirected path "
                        + "in the graph GB induced by B. The notions of start node and end node "
                        + "of a path are defined as usual. Note that, by considering an undirected "
                        + "path, we allow the edges of the RDF graph to be reversely traversed.");
        Resource rank = ontology.createProperty(EREL.rankPosition.getURI())
                .addProperty(RDFS.domain, path)
                .addProperty(RDFS.range, XSD.xlong)
                .addProperty(RDFS.label, "rankPosition")
                .addProperty(RDFS.comment, "The rank position of a path with respect to a pair of entities.");
        Resource score = ontology.createProperty(EREL.score.getURI())
                .addProperty(RDFS.domain, path)
                .addProperty(RDFS.range, XSD.xdouble)
                .addProperty(RDFS.label, "score")
                .addProperty(RDFS.comment, "The rank score of a path with respect to a pair of entities.");
        Resource expression = ontology.createProperty(EREL.expression.getURI())
                .addProperty(RDFS.domain, path)
                .addProperty(RDFS.range, XSD.xstring)
                .addProperty(RDFS.label, "expression")
                .addProperty(RDFS.comment, "The label of a path.");
        Resource hasPath = ontology.createProperty(EREL.hasPath.getURI())
                .addProperty(RDFS.domain, entityPair)
                .addProperty(RDFS.range, path)
                .addProperty(RDFS.label, "hasPath")
                .addProperty(RDFS.comment, "The relationship between a pair of entities and a path."
                        + "The set of all paths of a pair of entities make up the rank list of relevant "
                        + "paths between the pair of entities.");
        Resource pathElement = ontology.createResource(EREL.PathElement.getURI(), RDFS.Class)
                .addProperty(RDFS.label, "PathElement")
                .addProperty(RDFS.comment, "The elements (entities and properties) of a path.");
        Resource position = ontology.createProperty(EREL.position.getURI())
                .addProperty(RDFS.domain, pathElement)
                .addProperty(RDFS.range, XSD.xint)
                .addProperty(RDFS.label, "position")
                .addProperty(RDFS.comment, "The position of the element in the path.");
        score.addProperty(RDFS.domain, pathElement);

        Resource listOfPathElements = ontology.createResource(EREL.ListOfPathElements.getURI(), RDFS.Class)
                .addProperty(RDFS.subClassOf, RDF.List)
                .addProperty(RDFS.label, "ListOfPathElements")
                .addProperty(RDFS.comment, "The container of the elements of a path.");
        Resource first = ontology.createProperty(EREL.first.getURI())
                .addProperty(RDFS.subPropertyOf, RDF.first)
                .addProperty(RDFS.domain, listOfPathElements)
                .addProperty(RDFS.range, pathElement)
                .addProperty(RDFS.label, "first")
                .addProperty(RDFS.comment, "The first element of a list of path elements.");
        Resource rest = ontology.createProperty(EREL.rest.getURI())
                .addProperty(RDFS.subPropertyOf, RDF.rest)
                .addProperty(RDFS.domain, listOfPathElements)
                .addProperty(RDFS.range, listOfPathElements)
                .addProperty(RDFS.label, "rest")
                .addProperty(RDFS.comment, "The remaining elements of a list of path elelements.");
        Resource hasListOfPathElements = ontology.createProperty(EREL.hasListOfPathElements.getURI())
                .addProperty(RDFS.domain, path)
                .addProperty(RDFS.range, listOfPathElements)
                .addProperty(RDFS.label, "hasListOfPathElements")
                .addProperty(RDFS.comment, "The relationship between a path and a list of path elements.");

        Resource propertyElement = ontology.createResource(EREL.PropertyElement.getURI(), RDFS.Class)
                .addProperty(RDFS.subClassOf, pathElement)
                .addProperty(RDFS.label, "PropertyElement")
                .addProperty(RDFS.comment, "A property of a path.");
        Resource property = ontology.createProperty(EREL.property.getURI())
                .addProperty(RDFS.domain, propertyElement)
                .addProperty(RDFS.range, RDF.Property)
                .addProperty(RDFS.label, "property")
                .addProperty(RDFS.comment, "A reference to an rdf:Property.");
        Resource entityElement = ontology.createResource(EREL.EntityElement.getURI(), RDFS.Class)
                .addProperty(RDFS.subClassOf, pathElement)
                .addProperty(RDFS.label, "EntityElement")
                .addProperty(RDFS.comment, "An entity of a path.");
        Resource entity_ = ontology.createProperty(EREL.entity.getURI())
                .addProperty(RDFS.domain, entityElement)
                .addProperty(RDFS.range, entity)
                .addProperty(RDFS.label, "entity")
                .addProperty(RDFS.comment, "A reference to an erel:Entity.");

    }

    private static void createDataset() throws FileNotFoundException, IOException, Exception {
        UrlValidator validator = new UrlValidator();
        Integer counter = 0;

        dataset.setNsPrefix("", Config.DATA_NS);
        dataset.setNsPrefix("erel", EREL.NS);

        // Categories
        for (DB.Category c : DB.Categories.listCategories()) {
            Resource category = ontology.createResource(c.getUri(), RDFS.Class)
                    .addProperty(RDFS.subClassOf, EREL.Category)
                    .addProperty(RDFS.label, c.getLabel());

            for (DB.Resource r : c.listSameAS())
                category.addProperty(OWL.sameAs, ontology.createResource(r.getURI()));
        }

        //Entities
        for (DB.Entity e : DB.Entities.listEntities()) {
            Resource entity = dataset.createResource(e.getUri(), EREL.Entity)
                    .addProperty(EREL.type, e.getCategory().getUri());

            for (DB.Resource r : e.listSameAS())
                entity.addProperty(OWL.sameAs, dataset.createResource(r.getURI()));
        }

        //Entity pairs
        for (DB.EntityPair ep : DB.EntityPairs.listEntityPairs()) {
            Resource entityPair = dataset.createResource(ep.getUri(), EREL.EntityPair)
                    .addProperty(EREL.entity1, dataset.createResource(ep.getEntity1().getUri(), EREL.Entity))
                    .addProperty(EREL.entity2, dataset.createResource(ep.getEntity2().getUri(), EREL.Entity));

            // Paths
            for (DB.Path pt : ep.listPaths()) {
                entityPair.addProperty(EREL.hasPath, dataset.createResource(pt.getUri(), EREL.Path)
                        .addProperty(EREL.rankPosition, dataset.createTypedLiteral(pt.getRankPosition()))
                        .addProperty(EREL.score, dataset.createTypedLiteral(pt.getScore()))
                        .addProperty(EREL.expression, pt.getExpression()));

                // Path elements
                int count = 0;
                String uri0 = null;
                for (DB.PathElement e : pt.listElements()) {
                    String uri1 = Config.DATA_NS + "id-" + UUID.randomUUID().toString();
                    if (e instanceof DB.EntityElement)
                        dataset.createResource(uri1, EREL.ListOfPathElements)
                                .addProperty(EREL.first, dataset.createResource(e.getUri(), EREL.EntityElement))
                                .addProperty(EREL.entity, ((DB.EntityElement) e).getEntity().getUri())
                                .addProperty(EREL.position, dataset.createTypedLiteral(count++))
                                .addProperty(EREL.score, dataset.createTypedLiteral(e.getScore()));
                    else if (e instanceof DB.PropertyElement)
                        dataset.createResource(uri1, EREL.ListOfPathElements)
                                .addProperty(EREL.first, dataset.createResource(e.getUri(), EREL.PropertyElement))
                                .addProperty(EREL.property, ((DB.PropertyElement) e).getProperty().getUri())
                                .addProperty(EREL.position, dataset.createTypedLiteral(count++))
                                .addProperty(EREL.score, dataset.createTypedLiteral(e.getScore()));
                    else
                        dataset.createResource(uri1, EREL.ListOfPathElements)
                                .addProperty(EREL.first, dataset.createResource(e.getUri(), EREL.PathElement))
                                .addProperty(EREL.position, dataset.createTypedLiteral(count++))
                                .addProperty(EREL.score, dataset.createTypedLiteral(e.getScore()));

                    if (uri0 == null)
                        dataset.createResource(pt.getUri(), EREL.Path)
                                .addProperty(EREL.hasListOfPathElements, dataset.createResource(uri1, EREL.ListOfPathElements));
                    else
                        dataset.createResource(uri0, EREL.ListOfPathElements)
                                .addProperty(EREL.rest, dataset.createResource(uri1, EREL.ListOfPathElements));

                    uri0 = uri1;
                }
                if (count > 0)
                    dataset.createResource(uri0)
                            .addProperty(EREL.rest, RDF.nil);
            }
        }
    }

    private static void exportOntology(Model ontology) throws IOException, Exception {
        (new File(Config.LOCAL_ONTOLOGY_NAME)).getParentFile().mkdirs();

        try (OutputStream out = new FileOutputStream(Config.LOCAL_ONTOLOGY_NAME)) {
            RDFDataMgr.write(out, ontology, Lang.RDFXML);
        }

        try (InputStream in = new FileInputStream(Config.LOCAL_ONTOLOGY_NAME)) {
            Host.uploadViaFTP(Config.HOST_ADDR, Config.USERNAME, Config.PASSWORD, Config.REMOTE_ONTOLOGY_NAME, in);
            Host.mkDirsViaFTP(Config.HOST_ADDR, Config.USERNAME, Config.PASSWORD, Config.REMOTE_ONTOLOGY_NAME, in);
        }
    }

    private static void exportDataset(Model dataset) throws Exception, IOException {
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
