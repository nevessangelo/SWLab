package ic.uff.tic10086.exercicios;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.riot.Lang;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.vocabulary.ReasonerVocabulary;

public class Mashup extends MyDataset {

    public static final String SOURCE_ASSEMBLER_FILE = CONF_DIR + "/dbpedia.ttl";
    public static final String SOURCE_FUSEKI_DATA_URL = "http://" + DOMAIN + SPARQL_PORT + "/dbpedia.temp/data";
    public static final String TARGET_ASSEMBLER_FILE = CONF_DIR + "/turismoECultura.ttl";
    public static final String TARGET_FUSEKI_DATA_URL = "http://" + DOMAIN + SPARQL_PORT + "/turismoECultura.temp/data";
    public static final String REFERENCE_LINKS_ASSEMBLER_FILE = CONF_DIR + "/referenceLinks.ttl";
    public static final String REFERENCE_LINKS_FILENAME = RDF_DIR + "/referenceLinks-swlab.xml";
    public static final String REFERENCE_LINKS_FUSEKI_DATA_URL = "http://" + DOMAIN + SPARQL_PORT + "/referenceLinks.temp/data";
    public static final String DRAFT_ASSEMBLER_FILE = CONF_DIR + "/draft.ttl";
    public static final String DRAFT_FUSEKI_DATA_URL = "http://" + DOMAIN + SPARQL_PORT + "/draft.temp/data";

    public static final String ALIGNMENT_TO_SAME_AS_RULES = RDF_DIR + "/alignmentToSameAs.rules";
    public static final String DBPEDIA_TO_SCHEMA_ORG_RULES = RDF_DIR + "/dbpediaToSchemaOrg.rules";
    public static final String SAME_AS_RULES = RDF_DIR + "/sameAs1.rules";
    public static final String SAME_AS_RULES2 = RDF_DIR + "/sameAs2.rules";

    public static void main(String[] args) {
        try {
            init();

            FILENAME = "mashup";
            DEFAULT_NS = "http://" + DOMAIN + DEREF_PORT + "/resource/";
            TDB_ASSEMBLER_FILE = CONF_DIR + "/mashup.ttl";
            FUSEKI_UPDATE_URL = "http://" + DOMAIN + SPARQL_PORT + "/mashup/update";
            FUSEKI_DATA_URL = "http://" + DOMAIN + SPARQL_PORT + "/mashup/data";

            Dataset source = TDBFactory.assembleDataset(SOURCE_ASSEMBLER_FILE);
            Dataset target = TDBFactory.assembleDataset(TARGET_ASSEMBLER_FILE);
            Dataset referenceLinks = TDBFactory.assembleDataset(REFERENCE_LINKS_ASSEMBLER_FILE);
            Dataset draft = TDBFactory.assembleDataset(DRAFT_ASSEMBLER_FILE);
            Dataset mashup = TDBFactory.assembleDataset(TDB_ASSEMBLER_FILE);

            try {

                prepareReferenceLinks(referenceLinks);
                prepareDraft(source, target, referenceLinks, draft);
                mergeDatasets(draft, mashup);
                exportResources(mashup);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                source.close();
                target.close();
                referenceLinks.close();
                mashup.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void mergeDatasets(Dataset draft, Dataset mashup) throws FileNotFoundException {
        UpdateExecutionFactory.create(UpdateFactory.create("clear all"), mashup).execute();

        mashup.begin(ReadWrite.WRITE);
        Model mashupModel = mashup.getDefaultModel();
        mashupModel.getNsPrefixMap().clear();
        mashupModel.setNsPrefix("", DEFAULT_NS);
        mashupModel.setNsPrefix("dbr", "http://dbpedia.org/resource/");
        mashupModel.setNsPrefix("sch", "http://schema.org/");

        draft.begin(ReadWrite.WRITE);
        Model draftModel = draft.getDefaultModel();

        BufferedReader br = new BufferedReader(new FileReader(SAME_AS_RULES));
        List rules = Rule.parseRules(Rule.rulesParserFromReader(br));
        // Convert DBpedia schema to Schema.org schema.
        Reasoner reasoner = new GenericRuleReasoner(rules);
        reasoner.setParameter(ReasonerVocabulary.PROPruleMode, "forward");
        reasoner.setDerivationLogging(true);
        InfModel inf = ModelFactory.createInfModel(reasoner, draftModel);

        BufferedReader br2 = new BufferedReader(new FileReader(SAME_AS_RULES2));
        List rules2 = Rule.parseRules(Rule.rulesParserFromReader(br2));
        // Convert DBpedia schema to Schema.org schema.
        Reasoner reasoner2 = new GenericRuleReasoner(rules2);
        reasoner2.setParameter(ReasonerVocabulary.PROPruleMode, "forward");
        reasoner2.setDerivationLogging(true);
        InfModel inf2 = ModelFactory.createInfModel(reasoner, inf);

        BufferedReader br3 = new BufferedReader(new FileReader(DBPEDIA_TO_SCHEMA_ORG_RULES));
        List rules3 = Rule.parseRules(Rule.rulesParserFromReader(br3));
        // Convert DBpedia schema to Schema.org schema.
        Reasoner reasoner3 = new GenericRuleReasoner(rules3);
        reasoner3.setParameter(ReasonerVocabulary.PROPruleMode, "forward");
        reasoner3.setDerivationLogging(true);
        InfModel inf3 = ModelFactory.createInfModel(reasoner, inf2);

        String query = ""
                + "prefix sch: <http://schema.org/>\n"
                + "PREFIX dbo: <http://dbpedia.org/ontology/>\n"
                + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "\n"
                + "construct {?s1 ?p ?o. ?o ?p2 ?o2}\n"
                + "where {?s2 ?p ?o.\n"
                + "       optional {?o ?p2 ?o2.\n"
                + "                 FILTER regex(str(?o), \"" + DOMAIN + "\")} {\n"
                + "       select ?s1 ?s2\n"
                + "       where {?s1 (owl:sameAs| ^owl:sameAs)+ ?s2.\n"
                + "              FILTER regex(str(?s1), \"" + DOMAIN + "/\")}}\n"
                + "       FILTER (regex(str(?p), \"http://schema.org/\") \n"
                + "               || regex(str(?o), \"http://schema.org/\")\n"
                + "               || ?p = owl:sameAs)}\n";
        query = ""
                + "PREFIX sch: <http://schema.org/>\n"
                + "PREFIX dbo: <http://dbpedia.org/ontology/>\n"
                + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "\n"
                + "CONSTRUCT {?s ?p ?o.}\n"
                + "WHERE {?s ?p ?o.\n"
                + "       FILTER REGEX(str(?s), \"http://" + DOMAIN + "/resource/id2-\")"
                //+ "       FILTER (REGEX(str(?p), \"http://schema.org/\") || REGEX(str(?o), \"http://schema.org/\"))"
                + "}";
        QueryExecution exec = QueryExecutionFactory.create(query, inf3);
        exec.execConstruct(mashupModel);
        draft.end();

        mashup.commit();

    }

    private static void prepareReferenceLinks(Dataset referenceLinks) throws FileNotFoundException {
        referenceLinks.begin(ReadWrite.WRITE);
        Model model = referenceLinks.getDefaultModel();
        model.getNsPrefixMap().clear();
        model.removeAll();
        model.read(REFERENCE_LINKS_FILENAME);
        model.setNsPrefix("", DEFAULT_NS);
        model.setNsPrefix("dbr", "http://dbpedia.org/resource/");
        DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(REFERENCE_LINKS_FUSEKI_DATA_URL);
        accessor.putModel(model);
        referenceLinks.commit();
    }

    private static void prepareDraft(Dataset source, Dataset target, Dataset referenceLinks, Dataset draft) throws FileNotFoundException, MalformedURLException, IOException {
        UpdateExecutionFactory.create(UpdateFactory.create("clear all"), draft).execute();

        {
            draft.begin(ReadWrite.WRITE);
            draft.getDefaultModel().getNsPrefixMap().clear();
            draft.commit();
        }

        {
            Lang lang = Lang.JSONLD;
            draft.begin(ReadWrite.WRITE);
            URLConnection yc = (new URL(SOURCE_FUSEKI_DATA_URL)).openConnection();
            yc.setRequestProperty("Accept", lang.getHeaderString());
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            draft.getDefaultModel().read(in, null, lang.getName());
            draft.commit();
        }

        {
            Lang lang = Lang.JSONLD;
            draft.begin(ReadWrite.WRITE);
            URLConnection yc = (new URL(TARGET_FUSEKI_DATA_URL)).openConnection();
            yc.setRequestProperty("Accept", lang.getHeaderString());
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            draft.getDefaultModel().read(in, null, lang.getName());
            draft.commit();
        }

        {
            Lang lang = Lang.JSONLD;
            draft.begin(ReadWrite.WRITE);
            BufferedReader br = new BufferedReader(new FileReader(ALIGNMENT_TO_SAME_AS_RULES));
            List rules = Rule.parseRules(Rule.rulesParserFromReader(br));
            // Infer sameAS links from mappings
            URLConnection yc = (new URL(REFERENCE_LINKS_FUSEKI_DATA_URL)).openConnection();
            yc.setRequestProperty("Accept", lang.getHeaderString());
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            Model linksModel = ModelFactory.createDefaultModel();
            linksModel.read(in, null, lang.getHeaderString());
            Reasoner reasoner = new GenericRuleReasoner(rules);
            reasoner.setDerivationLogging(true);
            InfModel inf = ModelFactory.createInfModel(reasoner, linksModel);
            draft.getDefaultModel().add(inf.getDeductionsModel());
            draft.commit();
        }

        draft.begin(ReadWrite.READ);
        DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(DRAFT_FUSEKI_DATA_URL);
        accessor.putModel(draft.getDefaultModel());
        draft.end();
    }
}
