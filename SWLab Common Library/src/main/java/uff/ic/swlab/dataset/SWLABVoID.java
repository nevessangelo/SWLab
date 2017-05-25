package uff.ic.swlab.dataset;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.VOID;

public class SWLABVoID {

    private final String uri = "http://swlab.ic.uff.br/void.ttl";
    private final Model model = ModelFactory.createDefaultModel();

    public String getUri() {
        return uri;
    }

    public Model getModel() {
        return model;
    }

    public SWLABVoID() throws ParseException {
        model.setNsPrefix("void", VOID.NS);
        model.setNsPrefix("dcterms", DCTerms.NS);
        model.setNsPrefix("foaf", FOAF.NS);
        model.setNsPrefix("", uri + "#");

        Resource dsDesc = model.createResource(uri, VOID.DatasetDescription);
        dsDesc.addProperty(DCTerms.title, "Description of the available datasets at swlab.ic.uff.br")
                .addProperty(DCTerms.creator, "http://profile.lleme.net/foaf.rdf#me")
                .addProperty(FOAF.topic, createEntityRelatednessTestDataDescription());
    }

    private Resource createEntityRelatednessTestDataDescription() throws ParseException {

        String namespace = "http://swlab.ic.uff.br/resource";
        String datasetURI = uri + "#EntityRelatednessTestData";
        String title = "Entity Relatedness Test Data";
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
                .addProperty(FOAF.homepage, model.createResource("https://figshare.com/articles/Entity_Relatedness_Test_Dataset_-_V2/5007983"))
                //.addProperty(FOAF.page, model.createResource("https://datahub.io/dataset/EntityRelatednessTestData"))
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
                .addProperty(VOID.dataDump, model.createResource("https://ndownloader.figshare.com/articles/5007983/versions/1"))
                .addProperty(VOID.dataDump, model.createResource("http://swlab.ic.uff.br/dataset/EntityRelatednessTestData.rdf"))
                .addProperty(VOID.dataDump, model.createResource("http://swlab.ic.uff.br/dataset/EntityRelatednessTestData.ttl"))
                .addProperty(VOID.dataDump, model.createResource("http://swlab.ic.uff.br/dataset/EntityRelatednessTestData.json"))
                .addProperty(VOID.sparqlEndpoint, model.createResource("http://swlab.ic.uff.br/fuseki/dataset.html?tab=query&ds=/EntityRelatednessTestData"))
                .addProperty(VOID.vocabulary, model.createResource("http://swlab.ic.uff.br/ontology/EntityRelatednessTestData/0.1/"))
                .addProperty(VOID.uriSpace, namespace)
                .addProperty(VOID.triples, model.createTypedLiteral(0))
                .addProperty(VOID.subset, model.createResource(uri + "#LinksToDBpedia", VOID.Linkset)
                        .addProperty(VOID.objectsTarget, model.createResource("http://dbpedia.org/resource/DBpedia"))
                        .addProperty(VOID.subjectsTarget, model.createResource(datasetURI))
                        .addProperty(VOID.linkPredicate, OWL.sameAs)
                        .addProperty(VOID.triples, model.createTypedLiteral(0)))
                .addProperty(VOID.subset, model.createResource(uri + "#LinksToLastFM", VOID.Linkset)
                        .addProperty(VOID.objectsTarget, model.createResource("http://lastfm.rdfize.com/meta.n3#Dataset"))
                        .addProperty(VOID.subjectsTarget, model.createResource(datasetURI))
                        .addProperty(VOID.linkPredicate, OWL.sameAs)
                        .addProperty(VOID.triples, model.createTypedLiteral(0)))
                .addProperty(VOID.subset, model.createResource(uri + "#LinksToIMDB", VOID.Linkset)
                        .addProperty(VOID.objectsTarget, model.createResource("http://www.imdb.com"))
                        .addProperty(VOID.subjectsTarget, model.createResource(datasetURI))
                        .addProperty(VOID.linkPredicate, OWL.sameAs)
                        .addProperty(VOID.triples, model.createTypedLiteral(0)));

    }

}
