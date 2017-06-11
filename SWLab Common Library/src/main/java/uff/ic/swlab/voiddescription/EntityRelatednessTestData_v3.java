package uff.ic.swlab.voiddescription;

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
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.VOID;

public class EntityRelatednessTestData_v3 extends DatasetDescription {

    public static final String NAME = "EntityRelatednessTestData_v3";
    public static final String URI = VoIDDescription.NAMESPACE + NAME;
    public static final String ONTOLOGY_NS = "http://" + VoIDDescription.HOST_ADDR + "/ontology/EntityRelatednessTestData_v1/#";
    public final Resource root;
    public final Model description = ModelFactory.createDefaultModel();

    public EntityRelatednessTestData_v3() throws ParseException {
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

        this.root = this.description.createResource(URI, VOID.Dataset)
                .addProperty(DCTerms.title, title)
                .addProperty(DCTerms.description, description)
                .addProperty(FOAF.homepage, this.description.createResource("https://" + VoIDDescription.HOST_ADDR + "/dataset/" + NAME))
                .addProperty(RDFS.seeAlso, this.description.createResource("https://doi.org/10.6084/m9.figshare.5007983.v1"))
                //.addProperty(RDFS.seeAlso, description.createDatasetDescription("https://datahub.io/dataset/EntityRelatednessTestData_v3"))
                .addProperty(FOAF.page, this.description.createResource(ONTOLOGY_NS))
                .addProperty(DCTerms.creator, this.description.createResource("http://lattes.cnpq.br/2460021788616803"))
                .addProperty(DCTerms.creator, this.description.createResource("http://www.inf.puc-rio.br/~casanova"))
                .addProperty(DCTerms.publisher, this.description.createResource("http://www.ic.uff.br/~lapaesleme/foaf/#me"))
                .addProperty(DCTerms.contributor, this.description.createResource("mailto:bernardo@ccead.puc-rio.br"))
                .addProperty(DCTerms.contributor, this.description.createResource("mailto:giseli.lopes@gmail.com"))
                .addProperty(DCTerms.source, this.description.createResource("http://dbpedia.org/resource/DBpedia"))
                .addProperty(DCTerms.source, this.description.createResource("http://lastfm.rdfize.com/meta.n3#Dataset"))
                .addProperty(DCTerms.source, this.description.createResource("http://www.imdb.com"))
                .addProperty(DCTerms.license, this.description.createResource("https://creativecommons.org/licenses"))
                .addProperty(DCTerms.created, this.description.createTypedLiteral(created))
                .addProperty(DCTerms.issued, this.description.createTypedLiteral(issued))
                .addProperty(DCTerms.modified, this.description.createTypedLiteral(modified))
                .addProperty(DCTerms.subject, this.description.createResource("http://dbpedia.org/resource/Category:Information_retrieval_techniques"))
                .addProperty(DCTerms.subject, this.description.createResource("http://dbpedia.org/resource/Web-based_technologies"))
                .addProperty(VOID.uriSpace, "http://" + VoIDDescription.HOST_ADDR + "/resource/")
                .addProperty(VOID.exampleResource, this.description.createResource("http://" + VoIDDescription.HOST_ADDR + "/resource/Michael_Jackson"))
                .addProperty(VOID.exampleResource, this.description.createResource("http://" + VoIDDescription.HOST_ADDR + "/resource/Metallica"))
                //.addProperty(VOID.rootResource, description.createDatasetDescription("http://" + HOST_ADDR + "/resource/Metallica"))
                .addProperty(VOID.sparqlEndpoint, this.description.createResource("http://" + VoIDDescription.HOST_ADDR + "/fuseki/dataset.html?tab=query&ds=/" + NAME))
                .addProperty(VOID.dataDump, this.description.createResource("https://ndownloader.figshare.com/articles/5007983/versions/1"))
                .addProperty(VOID.dataDump, this.description.createResource("http://" + VoIDDescription.HOST_ADDR + "/dataset/" + NAME + ".rdf"))
                .addProperty(VOID.dataDump, this.description.createResource("http://" + VoIDDescription.HOST_ADDR + "/dataset/" + NAME + ".ttl"))
                .addProperty(VOID.dataDump, this.description.createResource("http://" + VoIDDescription.HOST_ADDR + "/dataset/" + NAME + ".json"))
                .addProperty(VOID.dataDump, this.description.createResource("http://" + VoIDDescription.HOST_ADDR + "/dataset/" + NAME + ".nt"))
                .addProperty(VOID.dataDump, this.description.createResource("http://" + VoIDDescription.HOST_ADDR + "/fuseki/" + NAME + "/"))
                .addProperty(VOID.feature, this.description.createResource("http://www.w3.org/ns/formats/RDF_XML"))
                .addProperty(VOID.feature, this.description.createResource("http://www.w3.org/ns/formats/Turtle"))
                .addProperty(VOID.feature, this.description.createResource(" http://www.w3.org/ns/formats/RDF_JSON"))
                .addProperty(VOID.feature, this.description.createResource("http://www.w3.org/ns/formats/N-Triples"))
                .addProperty(VOID.uriRegexPattern, "^http://swlab\\.ic\\.uff\\.br/dataset/(.+)\\.ttl$")
                .addProperty(VOID.uriRegexPattern, "^http://swlab\\.ic\\.uff\\.br/dataset/(.+)\\.rdf$")
                .addProperty(VOID.uriRegexPattern, "^http://swlab\\.ic\\.uff\\.br/dataset/(.+)\\.json$")
                .addProperty(VOID.uriRegexPattern, "^http://swlab\\.ic\\.uff\\.br/dataset/(.+)\\.nt$")
                .addProperty(VOID.vocabulary, this.description.createResource(ONTOLOGY_NS));
        this.description.add(getStructure());
    }

    @Override
    protected final Model getStructure() {
        String query = "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "prefix owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX void: <http://rdfs.org/ns/void#>\n"
                + "prefix swlabvoid: <%s>\n"
                + "prefix swlab: <%s>\n"
                + "prefix erel: <%s>\n"
                + "\n"
                + "construct {?dataset a void:Dataset.\n"
                + "           ?dataset void:triples ?triples.\n"
                + "           swlabvoid:%s void:subset ?linkset.\n"
                + "           ?linkset a void:Linkset.\n"
                + "           ?linkset void:subjectsTarget swlabvoid:%s.\n"
                + "           ?linkset void:objectsTarget ?target.\n"
                + "           ?linkset void:linkPredicate ?linkpredicate.\n"
                + "           ?linkset void:triples ?triples.\n"
                + "           swlabvoid:%s void:subset ?classpartition.\n"
                + "           ?classpartition a void:ClassPartition.\n"
                + "           ?classpartition void:class ?class.\n"
                + "           ?classpartition void:triples ?triples.\n"
                + "           swlabvoid:%s void:subset ?propertypartition.\n"
                + "           ?propertypartition a void:PropertyPartition.\n"
                + "           ?propertypartition void:property ?property.\n"
                + "           ?propertypartition void:triples ?triples.\n"
                + "}\n"
                + "where{\n"
                + "{SELECT ?dataset ?triples\n"
                + "WHERE {\n"
                + "{SELECT (count(?s) as ?triples) WHERE {?s ?p [].}}\n"
                + "bind (swlabvoid:%s as ?dataset)}}\n"
                + "\n"
                + "union {select ?linkset ?target ?linkpredicate (count (?s) as ?triples)\n"
                + "WHERE {{?s owl:sameAs ?o.\n"
                + "filter regex (str(?o), \"http://dbpedia.org\")\n"
                + "bind (swlabvoid:LinksToDBpedia_v3 as ?linkset)\n"
                + "bind (<http://dbpedia.org/resource/DBpedia> as ?target)\n"
                + "bind (owl:sameAs as ?linkpredicate)}\n"
                + "\n"
                + "union {?s owl:sameAs ?o.\n"
                + "filter regex (str(?o), \"https://www.last.fm\")\n"
                + "bind (swlabvoid:LinksToLastFM_v3 as ?linkset)\n"
                + "bind (<http://lastfm.rdfize.com/meta.n3#Dataset> as ?target)\n"
                + "bind (owl:sameAs as ?linkpredicate)}\n"
                + "\n"
                + "union {?s owl:sameAs ?o. \n"
                + "filter regex (str(?o), \"http://www.imdb.com\")\n"
                + "bind (swlabvoid:LinksToIMDB_v3 as ?linkset)\n"
                + "bind (<http://www.imdb.com> as ?target)\n"
                + "bind (owl:sameAs as ?linkpredicate)}	  \n"
                + "} group by ?linkset ?target ?linkpredicate }\n"
                + "\n"
                + "union {SELECT ?classpartition ?class ?triples\n"
                + "WHERE {\n"
                + "{SELECT ?class (count(?s) as ?triples)\n"
                + "WHERE {?s a|erel:type ?class.}\n"
                + "GROUP BY ?class}\n"
                + "bind (iri(str(swlabvoid:)+\"id-\"+STRUUID()) as ?classpartition)}}\n"
                + "\n"
                + "union {SELECT ?propertypartition ?property ?triples\n"
                + "WHERE {\n"
                + "{SELECT ?property (count(?s) as ?triples)\n"
                + "WHERE {?s ?property [].\n"
                + "filter (?property not in (owl:sameAs,rdf:type,erel:type))}\n"
                + "GROUP BY ?property}\n"
                + "bind (iri(str(swlabvoid:)+\"id-\"+STRUUID()) as ?propertypartition)}}\n"
                + "}";
        query = String.format(query, VoIDDescription.NAMESPACE, VoIDDescription.DATASET_NS, ONTOLOGY_NS, NAME, NAME, NAME, NAME, NAME);
        return execConstruct(query, VoIDDescription.getSPARQLEndpoint(NAME));
    }
}
