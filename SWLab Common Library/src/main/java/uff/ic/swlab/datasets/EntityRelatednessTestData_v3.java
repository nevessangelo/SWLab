package uff.ic.swlab.datasets;

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
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.VOID;

public abstract class EntityRelatednessTestData_v3 {

    public static Resource createResource() throws ParseException {
        Model model = ModelFactory.createDefaultModel();
        String datasetURI = SWLABVoID.NAMESPACE + "EntityRelatednessTestData_v3";
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
                //.addProperty(RDFS.seeAlso, MODEL.createResource("https://datahub.io/dataset/EntityRelatednessTestData_v3"))
                .addProperty(FOAF.page, model.createResource("https://swlab.ic.uff.br/ontology/EntityRelatednessTestData_v1"))
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
                .addProperty(VOID.uriSpace, "http://swlab.ic.uff.br/resource/")
                .addProperty(VOID.exampleResource, model.createResource("http://swlab.ic.uff.br/resource/Michael_Jackson"))
                .addProperty(VOID.exampleResource, model.createResource("http://swlab.ic.uff.br/resource/Metallica"))
                //.addProperty(VOID.rootResource, MODEL.createResource("http://swlab.ic.uff.br/resource/Metallica"))
                .addProperty(VOID.sparqlEndpoint, model.createResource("http://swlab.ic.uff.br/fuseki/dataset.html?tab=query&ds=/EntityRelatednessTestData_v3"))
                .addProperty(VOID.dataDump, model.createResource("https://ndownloader.figshare.com/articles/5007983/versions/1"))
                .addProperty(VOID.dataDump, model.createResource("http://swlab.ic.uff.br/dataset/EntityRelatednessTestData_v3.rdf"))
                .addProperty(VOID.dataDump, model.createResource("http://swlab.ic.uff.br/dataset/EntityRelatednessTestData_v3.ttl"))
                .addProperty(VOID.dataDump, model.createResource("http://swlab.ic.uff.br/dataset/EntityRelatednessTestData_v3.json"))
                .addProperty(VOID.dataDump, model.createResource("http://swlab.ic.uff.br/dataset/EntityRelatednessTestData_v3.nt"))
                .addProperty(VOID.dataDump, model.createResource("http://swlab.ic.uff.br/fuseki/EntityRelatednessTestData_v3/"))
                .addProperty(VOID.feature, model.createResource("http://www.w3.org/ns/formats/RDF_XML"))
                .addProperty(VOID.feature, model.createResource("http://www.w3.org/ns/formats/Turtle"))
                .addProperty(VOID.feature, model.createResource(" http://www.w3.org/ns/formats/RDF_JSON"))
                .addProperty(VOID.feature, model.createResource("http://www.w3.org/ns/formats/N-Triples"))
                .addProperty(VOID.uriRegexPattern, "^http://swlab\\.ic\\.uff\\.br/dataset/(.+)\\.ttl$")
                .addProperty(VOID.uriRegexPattern, "^http://swlab\\.ic\\.uff\\.br/dataset/(.+)\\.rdf$")
                .addProperty(VOID.uriRegexPattern, "^http://swlab\\.ic\\.uff\\.br/dataset/(.+)\\.json$")
                .addProperty(VOID.uriRegexPattern, "^http://swlab\\.ic\\.uff\\.br/dataset/(.+)\\.nt$")
                .addProperty(VOID.vocabulary, model.createResource("http://swlab.ic.uff.br/ontology/EntityRelatednessTestData_v1.rdf#"))
                .addProperty(VOID.triples, model.createTypedLiteral(127541))
                .addProperty(VOID.subset, model.createResource(SWLABVoID.NAMESPACE + "LinksToDBpedia_v3", VOID.Linkset)
                        .addProperty(VOID.objectsTarget, model.createResource("http://dbpedia.org/resource/DBpedia"))
                        .addProperty(VOID.subjectsTarget, model.createResource(datasetURI))
                        .addProperty(VOID.linkPredicate, OWL.sameAs)
                        .addProperty(VOID.triples, model.createTypedLiteral(736)))
                .addProperty(VOID.subset, model.createResource(SWLABVoID.NAMESPACE + "LinksToLastFM_v3", VOID.Linkset)
                        .addProperty(VOID.objectsTarget, model.createResource("http://lastfm.rdfize.com/meta.n3#Dataset"))
                        .addProperty(VOID.subjectsTarget, model.createResource(datasetURI))
                        .addProperty(VOID.linkPredicate, OWL.sameAs)
                        .addProperty(VOID.triples, model.createTypedLiteral(216)))
                .addProperty(VOID.subset, model.createResource(SWLABVoID.NAMESPACE + "LinksToIMDB_v3", VOID.Linkset)
                        .addProperty(VOID.objectsTarget, model.createResource("http://www.imdb.com"))
                        .addProperty(VOID.subjectsTarget, model.createResource(datasetURI))
                        .addProperty(VOID.linkPredicate, OWL.sameAs)
                        .addProperty(VOID.triples, model.createTypedLiteral(514)));

    }

    private static void addLinksetsStatisticsForEntityRelatednessTestData_v3() {
        String query = "prefix ds: <http://swlab.ic.uff.br/void/>"
                + "prefix owl: <http://www.w3.org/2002/07/owl#>"
                + "PREFIX void: <http://rdfs.org/ns/void#>"
                + "construct {ds:EntityRelatednessTestData_v3 void:subset ?linkset."
                + "?linkset a void:Linkset."
                + "?linkset void:subjectsTarget ?linkset."
                + "?linkset void:objectsTarget ?target."
                + "?linkset void:linkPredicate ?predicate."
                + "?linkset void:triples ?triples.}"
                + "where{"
                + "select ?linkset ?target ?predicate (count (?s) as ?triples)"
                + "WHERE {{?s owl:sameAs ?o."
                + "filter regex (str(?o), \"http://dbpedia.org\")"
                + "bind (ds:LinksToDBpedia_v3 as ?linkset)"
                + "bind (<http://dbpedia.org/resource/DBpedia> as ?target)"
                + "bind (owl:sameAs as ?predicate)}"
                + ""
                + "union {?s owl:sameAs ?o."
                + "filter regex (str(?o), \"https://www.last.fm\")"
                + "bind (ds:LinksToLastFM_v3 as ?linkset)"
                + "bind (<http://lastfm.rdfize.com/meta.n3#Dataset> as ?target)"
                + "bind (owl:sameAs as ?predicate)}"
                + ""
                + "union {?s owl:sameAs ?o. "
                + "filter regex (str(?o), \"http://www.imdb.com\")"
                + "bind (ds:LinksToIMDB_v3 as ?linkset)"
                + "bind (<http://www.imdb.com> as ?target)"
                + "bind (owl:sameAs as ?predicate)}	  "
                + "} group by ?linkset ?target ?predicate }";
        MODEL.add(getStatistics(query, "EntityRelatednessTestData_v3"));
    }
}
