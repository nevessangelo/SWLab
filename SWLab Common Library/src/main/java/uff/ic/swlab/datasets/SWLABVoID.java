package uff.ic.swlab.datasets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.VOID;
import org.apache.log4j.PropertyConfigurator;
import uff.ic.swlab.commons.util.Host;

public class SWLABVoID {

    private final String voidNamespace = "http://swlab.ic.uff.br/void.ttl#";
    private final Model model = ModelFactory.createDefaultModel();

    public String getUri() {
        return voidNamespace;
    }

    public Model getModel() {
        return model;
    }

    public SWLABVoID() throws ParseException {
        model.setNsPrefix("void", VOID.NS);
        model.setNsPrefix("dcterms", DCTerms.NS);
        model.setNsPrefix("foaf", FOAF.NS);
        model.setNsPrefix("", voidNamespace);

        Resource dsDesc = model.createResource(voidNamespace, VOID.DatasetDescription);
        dsDesc.addProperty(DCTerms.title, "Description of the available datasets at swlab.ic.uff.br")
                .addProperty(DCTerms.creator, "http://profile.lleme.net/foaf.rdf#me")
                .addProperty(FOAF.topic, createEntityRelatednessTestDataV3());
    }

    private Resource createEntityRelatednessTestDataV3() throws ParseException {

        String datasetURI = voidNamespace + "EntityRelatednessTestData_v3";
        String title = "Entity Relatedness Test Data (V3)";
        String description = "The entity relatedness problem refers to the question of "
                + "computing the relationship paths that better describe the connectivity between a "
                + "given entity pair. This dataset supports the evaluation of approaches that address "
                + "the entity relatedness problem. It covers two familiar domains, music and movies, and "
                + "uses data available in IMDb and last.fm, which are popular reference datasets in these "
                + "domains. The dataset contains 20 entity pairs from each of these domains and, for each "
                + "entity pair, a ranked list with 50 relationship paths. It also contains entity ratings "
                + "and property relevance scores for the entities and properties used in the paths.";

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Calendar created = Calendar.getInstance();
        created.setTime(format.parse("19/05/2017"));

        Calendar issued = Calendar.getInstance();
        issued.setTime(format.parse("29/05/2017"));

        Calendar modified = Calendar.getInstance();
        modified.setTime(format.parse("29/05/2017"));

        return model.createResource(datasetURI, VOID.Dataset)
                .addProperty(DCTerms.title, title)
                .addProperty(DCTerms.description, description)
                .addProperty(FOAF.homepage, model.createResource("https://swlab.ic.uff.br/dataset/EntityRelatednessTestData_v3"))
                .addProperty(RDFS.seeAlso, model.createResource("https://doi.org/10.6084/m9.figshare.5007983.v1"))
                .addProperty(FOAF.page, model.createResource("https://swlab.ic.uff.br/ontology/EntityRelatednessTestData_v1"))
                //.addProperty(RDFS.seeAlso, model.createResource("https://datahub.io/dataset/EntityRelatednessTestData_v3"))
                .addProperty(DCTerms.creator, model.createResource("mailto:jthvasago@gmail.com"))
                .addProperty(DCTerms.creator, model.createResource("mailto:casanova@inf.puc-rio.br"))
                .addProperty(DCTerms.publisher, model.createResource("http://profile.lleme.net/foaf.rdf#me"))
                .addProperty(DCTerms.contributor, model.createResource("mailto:bernardo@ccead.puc-rio.br"))
                .addProperty(DCTerms.contributor, model.createResource("mailto:giseli.lopes@gmail.com"))
                .addProperty(DCTerms.source, model.createResource("http://dbpedia.org/resource/DBpedia"))
                .addProperty(DCTerms.source, model.createResource("http://lastfm.rdfize.com/meta.n3#Dataset"))
                .addProperty(DCTerms.source, model.createResource("http://www.imdb.com"))
                .addProperty(DCTerms.license, model.createResource("https://creativecommons.org/licenses"))
                .addProperty(DCTerms.created, model.createTypedLiteral(created))
                .addProperty(DCTerms.issued, model.createTypedLiteral(issued))
                .addProperty(DCTerms.modified, model.createTypedLiteral(modified))
                .addProperty(DCTerms.subject, model.createResource("http://dbpedia.org/resource/Category:Information_retrieval_techniques"))
                .addProperty(DCTerms.subject, model.createResource("http://dbpedia.org/resource/Web-based_technologies"))
                .addProperty(VOID.exampleResource, model.createResource("http://swlab.ic.uff.br/resource/Michael_Jackson"))
                .addProperty(VOID.exampleResource, model.createResource("http://swlab.ic.uff.br/resource/Metallica"))
                //.addProperty(VOID.rootResource, model.createResource("http://swlab.ic.uff.br/resource/Metallica"))
                .addProperty(VOID.feature, model.createResource("http://www.w3.org/ns/formats/RDF_XML"))
                .addProperty(VOID.feature, model.createResource("http://www.w3.org/ns/formats/Turtle"))
                .addProperty(VOID.feature, model.createResource(" http://www.w3.org/ns/formats/RDF_JSON"))
                .addProperty(VOID.feature, model.createResource("http://www.w3.org/ns/formats/N-Triples"))
                .addProperty(VOID.dataDump, model.createResource("https://ndownloader.figshare.com/articles/5007983/versions/1"))
                .addProperty(VOID.dataDump, model.createResource("http://swlab.ic.uff.br/dump/EntityRelatednessTestData_v3.rdf"))
                .addProperty(VOID.dataDump, model.createResource("http://swlab.ic.uff.br/dump/EntityRelatednessTestData_v3.ttl"))
                .addProperty(VOID.dataDump, model.createResource("http://swlab.ic.uff.br/dump/EntityRelatednessTestData_v3.json"))
                .addProperty(VOID.dataDump, model.createResource("http://swlab.ic.uff.br/dump/EntityRelatednessTestData_v3.nt"))
                .addProperty(VOID.dataDump, model.createResource("http://swlab.ic.uff.br/fuseki/EntityRelatednessTestData_v3/"))
                .addProperty(VOID.sparqlEndpoint, model.createResource("http://swlab.ic.uff.br/fuseki/dataset.html?tab=query&ds=/EntityRelatednessTestData_v3"))
                .addProperty(VOID.vocabulary, model.createResource("http://swlab.ic.uff.br/ontology/EntityRelatednessTestData_v1.rdf#"))
                .addProperty(VOID.uriSpace, "http://swlab.ic.uff.br/resource/")
                .addProperty(VOID.uriRegexPattern, "^http://swlab\\.ic\\.uff\\.br/dataset/(.+)\\.ttl$")
                .addProperty(VOID.uriRegexPattern, "^http://swlab\\.ic\\.uff\\.br/dataset/(.+)\\.rdf$")
                .addProperty(VOID.uriRegexPattern, "^http://swlab\\.ic\\.uff\\.br/dataset/(.+)\\.json$")
                .addProperty(VOID.uriRegexPattern, "^http://swlab\\.ic\\.uff\\.br/dataset/(.+)\\.n3$")
                .addProperty(VOID.triples, model.createTypedLiteral(127541))
                .addProperty(VOID.subset, model.createResource(voidNamespace + "LinksToDBpedia_v3", VOID.Linkset)
                        .addProperty(VOID.objectsTarget, model.createResource("http://dbpedia.org/resource/DBpedia"))
                        .addProperty(VOID.subjectsTarget, model.createResource(datasetURI))
                        .addProperty(VOID.linkPredicate, OWL.sameAs)
                        .addProperty(VOID.triples, model.createTypedLiteral(736)))
                .addProperty(VOID.subset, model.createResource(voidNamespace + "LinksToLastFM_v3", VOID.Linkset)
                        .addProperty(VOID.objectsTarget, model.createResource("http://lastfm.rdfize.com/meta.n3#Dataset"))
                        .addProperty(VOID.subjectsTarget, model.createResource(datasetURI))
                        .addProperty(VOID.linkPredicate, OWL.sameAs)
                        .addProperty(VOID.triples, model.createTypedLiteral(216)))
                .addProperty(VOID.subset, model.createResource(voidNamespace + "LinksToIMDB_v3", VOID.Linkset)
                        .addProperty(VOID.objectsTarget, model.createResource("http://www.imdb.com"))
                        .addProperty(VOID.subjectsTarget, model.createResource(datasetURI))
                        .addProperty(VOID.linkPredicate, OWL.sameAs)
                        .addProperty(VOID.triples, model.createTypedLiteral(514)));
    }

    private static final String ROOT = "./resources/data";
    private static final String LOCAL_VOID_NAME = ROOT + "/void.ttl";
    private static final String HOST_ADDR = "swlab.ic.uff.br";
    private static final String REMOTE_VOID_NAME = "/tomcat/void.ttl";
    private static String USERNAME = null;
    private static String PASSWORD = null;

    public static void main(String[] args) throws FileNotFoundException, IOException, ParseException, Exception {
        PropertyConfigurator.configure("./resources/conf/log4j.properties");
        configure("./resources/conf/swlab.properties");

        Model void_ = (new SWLABVoID()).getModel();

        (new File(ROOT)).mkdirs();
        try (OutputStream out = new FileOutputStream(LOCAL_VOID_NAME);) {
            RDFDataMgr.write(out, void_, Lang.TURTLE);
        } finally {
        }
        try (InputStream in = new FileInputStream(LOCAL_VOID_NAME)) {
            Host.uploadViaFTP(HOST_ADDR, USERNAME, PASSWORD, REMOTE_VOID_NAME, in);
        } finally {
        }
    }

    private static void configure(String file) throws IOException {
        try (InputStream input = new FileInputStream(file);) {
            Properties prop = new Properties();
            prop.load(input);

            USERNAME = prop.getProperty("username");
            PASSWORD = prop.getProperty("password");
        }
    }
}
