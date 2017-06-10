package uff.ic.swlab.datasets;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.VOID;

public abstract class EntityRelatednessTestData_v3 {

    private static final String NAME = "EntityRelatednessTestData_v3";
    private static final String URI = SWLABVoID.NAMESPACE + NAME;
    private static final String ONTOLOGY_NS = "http://swlab.ic.uff.br/ontology/EntityRelatednessTestData_v1/#";

    public static Model createDatasetDescription() throws ParseException {
        Model model = ModelFactory.createDefaultModel();
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

        model.createResource(URI, VOID.Dataset)
                .addProperty(DCTerms.title, title)
                .addProperty(DCTerms.description, description)
                .addProperty(FOAF.homepage, model.createResource("https://swlab.ic.uff.br/dataset/EntityRelatednessTestData_v3"))
                .addProperty(RDFS.seeAlso, model.createResource("https://doi.org/10.6084/m9.figshare.5007983.v1"))
                //.addProperty(RDFS.seeAlso, MODEL.createDatasetDescription("https://datahub.io/dataset/EntityRelatednessTestData_v3"))
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
                //.addProperty(VOID.rootResource, MODEL.createDatasetDescription("http://swlab.ic.uff.br/resource/Metallica"))
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
                .addProperty(VOID.vocabulary, model.createResource("http://swlab.ic.uff.br/ontology/EntityRelatednessTestData_v1.rdf#"));

        model.add(getStructure());
        return model;
    }

    private static Model getStructure() {
        String query = "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "prefix owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX void: <http://rdfs.org/ns/void#>\n"
                + "prefix myvoid: <$s>\n"
                + "prefix myont: <%s>\n"
                + "\n"
                + "construct {?dataset a void:Dataset.\n"
                + "           ?dataset void:triples ?triples.\n"
                + "           myvoid:%s void:subset ?linkset.\n"
                + "           ?linkset a void:Linkset.\n"
                + "           ?linkset void:subjectsTarget ?linkset.\n"
                + "           ?linkset void:objectsTarget ?target.\n"
                + "           ?linkset void:linkPredicate ?linkpredicate.\n"
                + "           ?linkset void:triples ?triples.\n"
                + "           myvoid:%s void:subset ?classpartition.\n"
                + "           ?classpartition a void:ClassPartition.\n"
                + "           ?classpartition void:class ?class.\n"
                + "           ?classpartition void:triples ?triples.\n"
                + "           myvoid:%s void:subset ?propertypartition.\n"
                + "           ?propertypartition a void:PropertyPartition.\n"
                + "           ?propertypartition void:property ?property.\n"
                + "           ?propertypartition void:triples ?triples.\n"
                + "}\n"
                + "where{\n"
                + "{SELECT ?dataset ?triples\n"
                + "WHERE {\n"
                + "{SELECT (count(?s) as ?triples) WHERE {?s ?p [].}}\n"
                + "bind (myvoid:%s as ?dataset)}}\n"
                + "\n"
                + "union {select ?linkset ?target ?linkpredicate (count (?s) as ?triples)\n"
                + "WHERE {{?s owl:sameAs ?o.\n"
                + "filter regex (str(?o), \"http://dbpedia.org\")\n"
                + "bind (myvoid:LinksToDBpedia_v3 as ?linkset)\n"
                + "bind (<http://dbpedia.org/resource/DBpedia> as ?target)\n"
                + "bind (owl:sameAs as ?linkpredicate)}\n"
                + "\n"
                + "union {?s owl:sameAs ?o.\n"
                + "filter regex (str(?o), \"https://www.last.fm\")\n"
                + "bind (myvoid:LinksToLastFM_v3 as ?linkset)\n"
                + "bind (<http://lastfm.rdfize.com/meta.n3#Dataset> as ?target)\n"
                + "bind (owl:sameAs as ?linkpredicate)}\n"
                + "\n"
                + "union {?s owl:sameAs ?o. \n"
                + "filter regex (str(?o), \"http://www.imdb.com\")\n"
                + "bind (myvoid:LinksToIMDB_v3 as ?linkset)\n"
                + "bind (<http://www.imdb.com> as ?target)\n"
                + "bind (owl:sameAs as ?linkpredicate)}	  \n"
                + "} group by ?linkset ?target ?linkpredicate }\n"
                + "\n"
                + "union {SELECT ?classpartition ?class ?triples\n"
                + "WHERE {\n"
                + "{SELECT ?class (count(?s) as ?triples)\n"
                + "WHERE {?s a|myont:type ?class.}\n"
                + "GROUP BY ?class}\n"
                + "bind (iri(str(myvoid:)+STRUUID()) as ?classpartition)}}\n"
                + "\n"
                + "union {SELECT ?propertypartition ?property ?triples\n"
                + "WHERE {\n"
                + "{SELECT ?property (count(?s) as ?triples)\n"
                + "WHERE {?s ?property [].\n"
                + "filter (?property not in (owl:sameAs,rdf:type,myont:type))}\n"
                + "GROUP BY ?property}\n"
                + "bind (iri(str(myvoid:)+STRUUID()) as ?propertypartition)}}\n"
                + "}";
        query = String.format(query, SWLABVoID.NAMESPACE, ONTOLOGY_NS, NAME, NAME, NAME, NAME);
        return SWLABVoID.execConstruct(query, NAME);
    }
}
