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
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.VOID;
import uff.ic.swlab.dataset.entityrelatednesstestdata.v3.vocabulary.EREL;

public class EntityRelatednessTestData_v3 extends DatasetDescription {

    public final String NAME = "EntityRelatednessTestData_v3";
    public final String URI;
    public final VoIDDescription v;

    public final Resource root;
    public final Model description = ModelFactory.createDefaultModel();

    public EntityRelatednessTestData_v3(VoIDDescription v) throws ParseException {
        this.v = v;
        this.URI = v.MYVOID_NS + NAME;

        description.setNsPrefix("erel", EREL.NS);

        String title = "Entity Relatedness Test Data (v3)";
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
                .addProperty(FOAF.homepage, this.description.createResource("https://" + v.HOST_ADDR + "/dataset/" + NAME))
                .addProperty(RDFS.seeAlso, this.description.createResource("https://doi.org/10.6084/m9.figshare.5007983.v1"))
                //.addProperty(RDFS.seeAlso, description.createDatasetDescription("https://datahub.io/dataset/EntityRelatednessTestData_v3"))
                .addProperty(FOAF.page, this.description.createResource(EREL.NS))
                .addProperty(DCTerms.creator, this.description.createResource("http://lattes.cnpq.br/2460021788616803"))
                .addProperty(DCTerms.creator, this.description.createResource("http://www.inf.puc-rio.br/~casanova"))
                .addProperty(DCTerms.publisher, this.description.createResource("http://www.ic.uff.br/~lapaesleme/foaf/#me"))
                .addProperty(DCTerms.source, this.description.createResource("http://dbpedia.org/resource/DBpedia"))
                .addProperty(DCTerms.source, this.description.createResource("http://lastfm.rdfize.com/meta.n3#Dataset"))
                .addProperty(DCTerms.source, this.description.createResource("http://www.imdb.com"))
                .addProperty(DCTerms.license, this.description.createResource("https://creativecommons.org/licenses"))
                .addProperty(DCTerms.created, this.description.createTypedLiteral(created))
                .addProperty(DCTerms.issued, this.description.createTypedLiteral(issued))
                .addProperty(DCTerms.modified, this.description.createTypedLiteral(modified))
                .addProperty(DCTerms.subject, this.description.createResource("http://dbpedia.org/resource/Category:Information_retrieval_techniques"))
                .addProperty(DCTerms.subject, this.description.createResource("http://dbpedia.org/resource/Web-based_technologies"))
                .addProperty(VOID.uriSpace, "http://" + v.HOST_ADDR + "/resource/")
                //.addProperty(VOID.exampleResource, this.description.createResource("http://" + VoIDDescription.HOST_ADDR + "/resource/Michael_Jackson"))
                //.addProperty(VOID.exampleResource, this.description.createResource("http://" + VoIDDescription.HOST_ADDR + "/resource/Metallica"))
                //.addProperty(VOID.rootResource, description.createDatasetDescription("http://" + HOST_ADDR + "/resource/Metallica"))
                .addProperty(VOID.sparqlEndpoint, this.description.createResource("http://" + v.HOST_ADDR + "/fuseki/dataset.html?tab=query&ds=/" + NAME))
                .addProperty(VOID.dataDump, this.description.createResource("https://ndownloader.figshare.com/articles/5007983/versions/1"))
                .addProperty(VOID.dataDump, this.description.createResource("http://" + v.HOST_ADDR + "/dataset/" + NAME + ".rdf.gz"))
                .addProperty(VOID.dataDump, this.description.createResource("http://" + v.HOST_ADDR + "/dataset/" + NAME + ".ttl.gz"))
                .addProperty(VOID.dataDump, this.description.createResource("http://" + v.HOST_ADDR + "/dataset/" + NAME + ".json.gz"))
                .addProperty(VOID.dataDump, this.description.createResource("http://" + v.HOST_ADDR + "/dataset/" + NAME + ".nt.gz"))
                .addProperty(VOID.dataDump, this.description.createResource("http://" + v.HOST_ADDR + "/fuseki/" + NAME + "/"))
                .addProperty(VOID.feature, this.description.createResource("http://www.w3.org/ns/formats/RDF_XML"))
                .addProperty(VOID.feature, this.description.createResource("http://www.w3.org/ns/formats/Turtle"))
                .addProperty(VOID.feature, this.description.createResource(" http://www.w3.org/ns/formats/RDF_JSON"))
                .addProperty(VOID.feature, this.description.createResource("http://www.w3.org/ns/formats/N-Triples"))
                .addProperty(VOID.uriRegexPattern, "^http://swlab\\.ic\\.uff\\.br/dataset/(.+)\\.ttl.gz$")
                .addProperty(VOID.uriRegexPattern, "^http://swlab\\.ic\\.uff\\.br/dataset/(.+)\\.rdf.gz$")
                .addProperty(VOID.uriRegexPattern, "^http://swlab\\.ic\\.uff\\.br/dataset/(.+)\\.json.gz$")
                .addProperty(VOID.uriRegexPattern, "^http://swlab\\.ic\\.uff\\.br/dataset/(.+)\\.nt.gz$")
                .addProperty(VOID.vocabulary, this.description.createResource(EREL.NS));
        this.description.add(getStructure());
        this.description.add(getRootResources());
    }

    @Override
    protected final Model getRootResources() {
        String query = "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "prefix owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX void: <http://rdfs.org/ns/void#>\n"
                + "prefix myvoid: <%s>\n"
                + "prefix erel: <%s>\n"
                + "prefix swlab: <%s>\n"
                + "\n"
                + "construct {?dataset void:rootResource ?rootResource.\n"
                + "}\n"
                + "where{?rootResource a erel:EntityPair.\n"
                + "      bind (myvoid:%s as ?dataset)}\n";

        query = String.format(query, v.MYVOID_NS, EREL.NS, v.SWLAB_NS, NAME);
        return execConstruct(query, v.getSPARQLEndpoint(NAME));
    }

    @Override
    protected final Model getStructure() {
        String query = "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "prefix owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX void: <http://rdfs.org/ns/void#>\n"
                + "prefix myvoid: <%s>\n"
                + "prefix erel: <%s>\n"
                + "prefix swlab: <%s>\n"
                + "\n"
                + "construct {?dataset a void:Dataset.\n"
                + "           ?dataset void:triples ?triples.\n"
                + "           ?dataset void:entities ?entities.\n"
                + "           myvoid:%s void:subset ?linkset.\n"
                + "           ?linkset a void:Linkset.\n"
                + "           ?linkset void:subjectsTarget myvoid:%s.\n"
                + "           ?linkset void:objectsTarget ?target.\n"
                + "           ?linkset void:linkPredicate ?linkpredicate.\n"
                + "           ?linkset void:triples ?triples.\n"
                + "           myvoid:%s void:classPartition ?classpartition.\n"
                + "           ?classpartition a void:Dataset.\n"
                + "           ?classpartition void:class ?class.\n"
                + "           ?classpartition void:triples ?triples.\n"
                + "           myvoid:%s void:propertyPartition ?propertypartition.\n"
                + "           ?propertypartition a void:Dataset.\n"
                + "           ?propertypartition void:property ?property.\n"
                + "           ?propertypartition void:triples ?triples.\n"
                + "}\n"
                + "where{\n"
                + "      {SELECT ?dataset ?triples\n"
                + "       WHERE {\n"
                + "              {SELECT (count(?s) as ?triples) WHERE {?s ?p [].}}\n"
                + "              {SELECT (count(distinct(?s)) as ?entities) WHERE {?s ?p [].}}\n"
                + "              bind (myvoid:%s as ?dataset)}}\n"
                + "\n"
                + "      union {select ?linkset ?target ?linkpredicate (count (?s) as ?triples)\n"
                + "             WHERE {\n"
                + "                    {?s owl:sameAs ?o.\n"
                + "                     filter regex (str(?o), \"http://dbpedia.org\")\n"
                + "                     bind (myvoid:LinksToDBpedia_v3 as ?linkset)\n"
                + "                     bind (<http://dbpedia.org/resource/DBpedia> as ?target)\n"
                + "                     bind (owl:sameAs as ?linkpredicate)}\n"
                + "\n"
                + "                    union {?s owl:sameAs ?o.\n"
                + "                           filter regex (str(?o), \"https://www.last.fm\")\n"
                + "                           bind (myvoid:LinksToLastFM_v3 as ?linkset)\n"
                + "                           bind (<http://lastfm.rdfize.com/meta.n3#Dataset> as ?target)\n"
                + "                           bind (owl:sameAs as ?linkpredicate)}\n"
                + "\n"
                + "                    union {?s owl:sameAs ?o. \n"
                + "                           filter regex (str(?o), \"http://www.imdb.com\")\n"
                + "                           bind (myvoid:LinksToIMDB_v3 as ?linkset)\n"
                + "                           bind (<http://www.imdb.com> as ?target)\n"
                + "                           bind (owl:sameAs as ?linkpredicate)}}	  \n"
                + "             group by ?linkset ?target ?linkpredicate }\n"
                + "\n"
                + "      union {SELECT ?classpartition ?class ?triples\n"
                + "             WHERE {\n"
                + "                    {SELECT ?class (count(?s) as ?triples)\n"
                + "                     WHERE {?s a|erel:type ?class.}\n"
                + "                     GROUP BY ?class}\n"
                + "                    bind (iri(str(myvoid:)+\"id-\"+STRUUID()) as ?classpartition)}}\n"
                + "\n"
                + "      union {SELECT ?propertypartition ?property ?triples\n"
                + "             WHERE {\n"
                + "                    {SELECT ?property (count(?s) as ?triples)\n"
                + "                     WHERE {?s ?property [].\n"
                + "                            filter (?property not in (owl:sameAs,rdf:type,erel:type))}\n"
                + "                     GROUP BY ?property}\n"
                + "                    bind (iri(str(myvoid:)+\"id-\"+STRUUID()) as ?propertypartition)}}\n"
                + "}";
        query = String.format(query, v.MYVOID_NS, EREL.NS, v.SWLAB_NS, NAME, NAME, NAME, NAME, NAME);
        return execConstruct(query, v.getSPARQLEndpoint(NAME));
    }
}
