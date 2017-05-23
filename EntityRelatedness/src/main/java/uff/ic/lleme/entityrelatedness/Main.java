package uff.ic.lleme.entityrelatedness;

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
import javax.net.ssl.KeyManager;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.net.util.KeyManagerUtils;
import org.apache.commons.net.util.TrustManagerUtils;
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

    private static String ontologyNS = "http://swlab.ic.uff.br/ontologyEntityRelatednessTestDataset.rdf#";
    private static String dataNS = "http://swlab.ic.uff.br/resource/";
    private static String voidNS = "http://swlab.ic.uff.br/void.ttl#";
    private static String alignNS = "http://knowledgeweb.semanticweb.org/heterogeneity/alignment#";

    private static String localDir = "./data/EntityRelatednessTestDataset";
    private static String datasetName = "EntityRelatednessTestData";
    private static String fusekiURL = "http://swlab.ic.uff.br/fuseki";

    private static MusicScores musicScores = new MusicScores();
    private static MusicRankedPaths musicRankedPaths = new MusicRankedPaths();
    private static MusicPropertyRelevanceScore musicPropertyRelevanceScore = new MusicPropertyRelevanceScore();
    private static MusicEntityMappings musicEntityMapping = new MusicEntityMappings();
    private static MovieScores movieScores = new MovieScores();
    private static MovieRankedPaths movieRankedPaths = new MovieRankedPaths();
    private static MoviePropertyRelevanceScore moviePropertyRelevanceScore = new MoviePropertyRelevanceScore();
    private static MovieEntityMappings movieEntityMapping = new MovieEntityMappings();
    private static MusicClassMapping musicClassMapping = new MusicClassMapping();
    private static MovieClassMapping movieClassMapping = new MovieClassMapping();
    private static MovieEntityPairs movieEntityPairs = new MovieEntityPairs();
    private static MusicEntityPairs musicEntityPairs = new MusicEntityPairs();

    public static void main(String[] args) throws FileNotFoundException, IOException, GeneralSecurityException {
        createOntology();
        createDataset();
        createVoID();
        System.out.println("Fim.");
    }

    private static void createOntology() throws FileNotFoundException, IOException, GeneralSecurityException {
        Model ontology = ModelFactory.createDefaultModel();
        ontology.setNsPrefix("", ontologyNS);

        Resource pathElementClass = ontology.createResource(ontologyNS + "PathElement", RDFS.Class);
        Property score = ontology.createProperty(ontologyNS + "score");
        score.addProperty(RDFS.domain, pathElementClass);
        score.addProperty(RDFS.range, XSD.xdouble);
        Resource entityClass = ontology.createResource(ontologyNS + "Entity", RDFS.Class);
        entityClass.addProperty(RDFS.subClassOf, pathElementClass);
        Resource propertyClass = ontology.createResource(ontologyNS + "Property", RDFS.Class);
        propertyClass.addProperty(RDFS.subClassOf, pathElementClass);

        Resource entityPairClass = ontology.createResource(ontologyNS + "EntityPair", RDFS.Class);
        Property first = ontology.createProperty(ontologyNS + "first");
        Property second = ontology.createProperty(ontologyNS + "second");
        first.addProperty(RDFS.domain, entityPairClass);
        first.addProperty(RDFS.range, entityClass);
        second.addProperty(RDFS.domain, entityPairClass);
        second.addProperty(RDFS.range, entityClass);

        Property hasPath = ontology.createProperty(ontologyNS + "hasPath");
        hasPath.addProperty(RDFS.domain, entityPairClass);
        hasPath.addProperty(RDFS.range, ontology.createResource(ontologyNS + "Path", RDFS.Class));

        Resource pathClass = ontology.createResource(ontologyNS + "Path", RDFS.Class);
        score.addProperty(RDFS.domain, pathClass);
        Property rank = ontology.createProperty(ontologyNS + "rank");
        rank.addProperty(RDFS.domain, pathClass);
        rank.addProperty(RDFS.range, XSD.xlong);
        Property expression = ontology.createProperty(ontologyNS + "expression");
        expression.addProperty(RDFS.domain, pathClass);
        expression.addProperty(RDFS.range, XSD.xstring);

        Property hasListOfPathElements = ontology.createProperty(ontologyNS + "hasListOfpathElements");
        hasListOfPathElements.addProperty(RDFS.domain, pathClass);
        hasListOfPathElements.addProperty(RDFS.range, ontology.createResource(ontologyNS + "ListOfPathElements", RDFS.Class));

        Resource listOfPathElementsClass = ontology.createResource(ontologyNS + "ListOfPathElements", RDFS.Class);
        listOfPathElementsClass.addProperty(RDFS.subClassOf, RDF.List);
        Property firstPathElement = ontology.createProperty(ontologyNS + "firstPathElement");
        firstPathElement.addProperty(RDFS.subPropertyOf, RDF.first);
        firstPathElement.addProperty(RDFS.domain, listOfPathElementsClass);
        firstPathElement.addProperty(RDFS.range, pathElementClass);
        Property restOfPathElements = ontology.createProperty(ontologyNS + "restOfPathElements");
        restOfPathElements.addProperty(RDFS.subPropertyOf, RDF.rest);
        restOfPathElements.addProperty(RDFS.domain, listOfPathElementsClass);
        restOfPathElements.addProperty(RDFS.range, listOfPathElementsClass);

        (new File(localDir + "/RDF/Ontology")).mkdirs();
        OutputStream out = new FileOutputStream(new File(localDir + "/RDF/Ontology/" + datasetName + ".rdf"));
        RDFDataMgr.write(out, ontology, Lang.RDFXML);

        File storeFile = new File("/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/security/cacerts");
        KeyManager keyManager = KeyManagerUtils.createClientKeyManager(storeFile, "changeit");

        FTPSClient ftpsClient = new FTPSClient(false);

        ftpsClient.setTrustManager(TrustManagerUtils.getAcceptAllTrustManager());
        ftpsClient.setKeyManager(keyManager);

        ftpsClient.connect("swlab.ic.uff.br");
        ftpsClient.login("swlab", "fluzao00");
        //ftpClient.execAUTH("TLS");
        ftpsClient.execPBSZ(0);  // Set protection buffer size
        ftpsClient.execPROT("P"); // Set data channel protection to private
        ftpsClient.enterLocalPassiveMode();
        int reply = ftpsClient.getReplyCode();
        if (FTPReply.isPositiveCompletion(reply))
            try (InputStream in = new FileInputStream(localDir + "/RDF/Ontology/" + datasetName + ".rdf")) {
                ftpsClient.storeFile(datasetName + ".rdf", in);
                System.out.println("salvo");
            } catch (Exception e) {
                System.out.println(e.toString());
            } finally {
            }
        ftpsClient.logout();
        ftpsClient.disconnect();
    }

    private static void createDataset() throws FileNotFoundException {
        Model dataset = ModelFactory.createDefaultModel();
        dataset.setNsPrefix("", dataNS);
        dataset.setNsPrefix("align", alignNS);

        Resource alignmentClass = dataset.createResource(alignNS + "Alignment");
        Resource alignment = dataset.createResource(alignmentClass);

        for (Map.Entry<String, ArrayList<Pair>> entry : movieEntityMapping.entrySet())
            for (Pair pair : entry.getValue()) {
                Resource cell = dataset.createResource(ontologyNS + pair.label, dataset.createResource(alignNS + "Cell"));
                cell.addProperty(dataset.createProperty(alignNS + "entity1"), pair.entity1);
                cell.addProperty(dataset.createProperty(alignNS + "entity2"), pair.entity2);
                cell.addProperty(dataset.createProperty(alignNS + "relation"), "=");
                alignment.addProperty(dataset.createProperty(alignNS + "map"), cell.as(RDFNode.class));
            }

        for (Map.Entry<String, ArrayList<Pair>> entry : musicEntityMapping.entrySet())
            for (Pair pair : entry.getValue()) {
                Resource cell = dataset.createResource(ontologyNS + pair.label, dataset.createResource(alignNS + "Cell"));
                cell.addProperty(dataset.createProperty(alignNS + "entity1"), pair.entity1);
                cell.addProperty(dataset.createProperty(alignNS + "entity2"), pair.entity2);
                cell.addProperty(dataset.createProperty(alignNS + "relation"), "=");
                alignment.addProperty(dataset.createProperty(alignNS + "map"), cell.as(RDFNode.class));
            }

        (new File(localDir + "/RDF")).mkdirs();
        OutputStream out = new FileOutputStream(new File(localDir + "/RDF/" + datasetName + ".ttl"));
        RDFDataMgr.write(out, dataset, Lang.TURTLE);

        OutputStream out2 = new FileOutputStream(new File(localDir + "/RDF/" + datasetName + ".rdf"));
        RDFDataMgr.write(out2, dataset, Lang.RDFXML);

        OutputStream out3 = new FileOutputStream(new File(localDir + "/RDF/" + datasetName + ".json"));
        RDFDataMgr.write(out3, dataset, Lang.JSONLD);

        DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(fusekiURL + "/" + datasetName + "/data");
        accessor.putModel(dataset);
    }

    private static void createVoID() throws FileNotFoundException {
        Model voidVocab = ModelFactory.createDefaultModel();
        voidVocab.setNsPrefix("", dataNS);
        voidVocab.setNsPrefix("align", alignNS);
        voidVocab.read("http://vocab.deri.ie/void#");

        Model voidData = ModelFactory.createDefaultModel();
        voidData.createResource(ontologyNS + "EntityRelatednessTestData", VOID.Dataset)
                .addProperty(DCTerms.description, "The entity relatedness problem refers to the question of "
                        + "computing the relationship paths that better describe the connectivity between a "
                        + "given entity pair. This dataset supports the evaluation of approaches that address "
                        + "the entity relatedness problem. It covers two familiar domains, music and movies, and "
                        + "uses data available in IMDb and last.fm, which are popular reference datasets in these "
                        + "domains. The dataset contains 20 entity pairs from each of these domains and, for each "
                        + "entity pair, a ranked list with 50 relationship paths. It also contains entity ratings "
                        + "and property relevance scores for the entities and properties used in the paths.");

        (new File(localDir + "/RDF")).mkdirs();
        OutputStream out = new FileOutputStream(new File(localDir + "/RDF/void.ttl"));
        RDFDataMgr.write(out, voidData, Lang.TURTLE);

    }

}
