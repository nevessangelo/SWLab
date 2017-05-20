package uff.ic.lleme.entityrelatedness;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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

public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        MusicEntityPairs musicEntityPairs = new MusicEntityPairs();
        MovieEntityPairs movieEntityPairs = new MovieEntityPairs();
        MovieClassMapping movieClassMapping = new MovieClassMapping();
        MusicClassMapping musicClassMapping = new MusicClassMapping();

        MovieEntityMappings movieEntityMapping = new MovieEntityMappings();
        MoviePropertyRelevanceScore moviePropertyRelevanceScore = new MoviePropertyRelevanceScore();
        MovieRankedPaths movieRankedPaths = new MovieRankedPaths();
        MovieScores movieScores = new MovieScores();

        MusicEntityMappings musicEntityMapping = new MusicEntityMappings();
        MusicPropertyRelevanceScore musicPropertyRelevanceScore = new MusicPropertyRelevanceScore();
        MusicRankedPaths musicRankedPaths = new MusicRankedPaths();
        MusicScores musicScores = new MusicScores();

        String alignns = "http://knowledgeweb.semanticweb.org/heterogeneity/alignment#";
        String gtns = "http://swlab.ic.uff.br/resource/";
        String erns = "http://swlab.ic.uff.br/ontology/EntityRelatednessTestDataset.rdf#";

        {
            Model ontology = ModelFactory.createDefaultModel();
            ontology.setNsPrefix("", erns);

            Resource pathElementClass = ontology.createResource(erns + "PathElement", RDFS.Class);
            Property score = ontology.createProperty(erns + "score");
            score.addProperty(RDFS.domain, pathElementClass);
            score.addProperty(RDFS.range, XSD.xdouble);
            Resource entityClass = ontology.createResource(erns + "Entity", RDFS.Class);
            entityClass.addProperty(RDFS.subClassOf, pathElementClass);
            Resource propertyClass = ontology.createResource(erns + "Property", RDFS.Class);
            propertyClass.addProperty(RDFS.subClassOf, pathElementClass);

            Resource entityPairClass = ontology.createResource(erns + "EntityPair", RDFS.Class);
            Property first = ontology.createProperty(erns + "first");
            Property second = ontology.createProperty(erns + "second");
            first.addProperty(RDFS.domain, entityPairClass);
            first.addProperty(RDFS.range, entityClass);
            second.addProperty(RDFS.domain, entityPairClass);
            second.addProperty(RDFS.range, entityClass);

            Property hasPath = ontology.createProperty(erns + "hasPath");
            hasPath.addProperty(RDFS.domain, entityPairClass);
            hasPath.addProperty(RDFS.range, ontology.createResource(erns + "Path", RDFS.Class));

            Resource pathClass = ontology.createResource(erns + "Path", RDFS.Class);
            score.addProperty(RDFS.domain, pathClass);
            Property rank = ontology.createProperty(erns + "rank");
            rank.addProperty(RDFS.domain, pathClass);
            rank.addProperty(RDFS.range, XSD.xlong);
            Property expression = ontology.createProperty(erns + "expression");
            expression.addProperty(RDFS.domain, pathClass);
            expression.addProperty(RDFS.range, XSD.xstring);

            Property hasListOfPathElements = ontology.createProperty(erns + "hasListOfpathElements");
            hasListOfPathElements.addProperty(RDFS.domain, pathClass);
            hasListOfPathElements.addProperty(RDFS.range, ontology.createResource(erns + "ListOfPathElements", RDFS.Class));

            Resource listOfPathElementsClass = ontology.createResource(erns + "ListOfPathElements", RDFS.Class);
            listOfPathElementsClass.addProperty(RDFS.subClassOf, RDF.List);
            Property firstPathElement = ontology.createProperty(erns + "firstPathElement");
            firstPathElement.addProperty(RDFS.subPropertyOf, RDF.first);
            firstPathElement.addProperty(RDFS.domain, listOfPathElementsClass);
            firstPathElement.addProperty(RDFS.range, pathElementClass);
            Property restOfPathElements = ontology.createProperty(erns + "restOfPathElements");
            restOfPathElements.addProperty(RDFS.subPropertyOf, RDF.rest);
            restOfPathElements.addProperty(RDFS.domain, listOfPathElementsClass);
            restOfPathElements.addProperty(RDFS.range, listOfPathElementsClass);

            (new File("./data/EntityRelatednessTestDataset/RDF/Ontology")).mkdirs();
            OutputStream out0 = new FileOutputStream(new File("./data/EntityRelatednessTestDataset/RDF/Ontology/EntityRelatednessTestDataset.rdf"));
            RDFDataMgr.write(out0, ontology, Lang.RDFXML);
        }

        {
            Model dataset = ModelFactory.createDefaultModel();
            dataset.setNsPrefix("", gtns);
            dataset.setNsPrefix("align", alignns);

            Resource alignmentClass = dataset.createResource(alignns + "Alignment");
            Resource alignment = dataset.createResource(alignmentClass);

            for (Map.Entry<String, ArrayList<Pair>> entry : movieEntityMapping.entrySet())
                for (Pair pair : entry.getValue()) {
                    Resource cell = dataset.createResource(erns + pair.label, dataset.createResource(alignns + "Cell"));
                    cell.addProperty(dataset.createProperty(alignns + "entity1"), pair.entity1);
                    cell.addProperty(dataset.createProperty(alignns + "entity2"), pair.entity2);
                    alignment.addProperty(dataset.createProperty(alignns + "map"), cell.as(RDFNode.class));
                }

            for (Map.Entry<String, ArrayList<Pair>> entry : musicEntityMapping.entrySet())
                for (Pair pair : entry.getValue()) {
                    Resource cell = dataset.createResource(erns + pair.label, dataset.createResource(alignns + "Cell"));
                    cell.addProperty(dataset.createProperty(alignns + "entity1"), pair.entity1);
                    cell.addProperty(dataset.createProperty(alignns + "entity2"), pair.entity2);
                    alignment.addProperty(dataset.createProperty(alignns + "map"), cell.as(RDFNode.class));
                }

            OutputStream out = new FileOutputStream(new File("./data/EntityRelatednessTestDataset/RDF/EntityRelatednessTestDataset.ttl"));
            RDFDataMgr.write(out, dataset, Lang.TURTLE);

            OutputStream out2 = new FileOutputStream(new File("./data/EntityRelatednessTestDataset/RDF/EntityRelatednessTestDataset.rdf"));
            RDFDataMgr.write(out2, dataset, Lang.RDFXML);

            OutputStream out3 = new FileOutputStream(new File("./data/EntityRelatednessTestDataset/RDF/EntityRelatednessTestDataset.json"));
            RDFDataMgr.write(out3, dataset, Lang.JSONLD);

            DatasetAccessor accessor = DatasetAccessorFactory.createHTTP("http://swlab.ic.uff.br/fuseki/EntityRelatednessTestDataset/data");
            accessor.putModel(dataset);
        }
        System.out.println("Fim.");
    }
}
