package ic.uff.tic10086.exercicios;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
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
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;

public class Mashup extends MyDataset {

    public static final String SOURCE_ASSEMBLER_FILE = "./src/main/resources/conf/dbpedia.ttl";
    public static final String TARGET_ASSEMBLER_FILE = "./src/main/resources/conf/turismoECultura.ttl";
    public static final String REFERENCE_LINKS_ASSEMBLER_FILE = "./src/main/resources/conf/referenceLinks.ttl";
    public static final String REFERENCE_LINKS_FILENAME = "./src/main/resources/dat/rdf/referenceLinks.xml";
    public static final String REFERENCE_LINKS_FUSEKI_DATA_URL = "http://localhost:3030/referenceLinks/data";
    public static final String DRAFT_ASSEMBLER_FILE = "./src/main/resources/conf/draft.ttl";
    public static final String DRAFT_FUSEKI_DATA_URL = "http://localhost:3030/draft/data";

    public static final String ALIGNMENT_TO_SAME_AS_RULES = "./src/main/resources/dat/rdf/alignmentToSameAs.rules";
    public static final String DBPEDIA_TO_SCHEMA_ORG_RULES = "./src/main/resources/dat/rdf/dbpediaToSchemaOrg.rules";

    public static void main(String[] args) {
        try {
            init();

            FILENAME = "mashup";
            TDB_ASSEMBLER_FILE = "./src/main/resources/conf/mashup.ttl";
            FUSEKI_UPDATE_URL = "http://localhost:3030/mashup/update";
            FUSEKI_DATA_URL = "http://localhost:3030/mashup/data";

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
        mashupModel.setNsPrefix("sch", "http://schema.org/");
        mashupModel.setNsPrefix("dbr", "http://dbpedia.org/resource/");
        mashupModel.setNsPrefix("", "http://localhost:8080/resource/");

        draft.begin(ReadWrite.WRITE);
        Model draftModel = draft.getDefaultModel();
        BufferedReader br = new BufferedReader(new FileReader(DBPEDIA_TO_SCHEMA_ORG_RULES));
        List rules = Rule.parseRules(Rule.rulesParserFromReader(br));
        // Convert DBpedia schema to Schema.org schema.
        Reasoner reasoner = new GenericRuleReasoner(rules);
        reasoner.setDerivationLogging(true);
        InfModel inf = ModelFactory.createInfModel(reasoner, draftModel);

        String query = ""
                + "prefix sch: <http://schema.org/>\n"
                + "PREFIX dbo: <http://dbpedia.org/ontology/>\n"
                + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "\n"
                + "construct {?s1 ?p ?o. ?o ?p2 ?o2}\n"
                + "where {?s2 ?p ?o.\n"
                + "       optional {?o ?p2 ?o2.\n"
                + "                 FILTER regex(str(?o), \"http://localhost:8080/resource/\")} {\n"
                + "       select ?s1 ?s2\n"
                + "       where {?s1 (owl:sameAs| ^owl:sameAs)+ ?s2.\n"
                + "              FILTER regex(str(?s1), \"http://localhost:8080/resource/\")}}\n"
                + "       FILTER (regex(str(?p), \"http://schema.org/\") \n"
                + "               || regex(str(?o), \"http://schema.org/\")\n"
                + "               || ?p = owl:sameAs)}\n";
        QueryExecution exec = QueryExecutionFactory.create(query, inf);
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
        OutputStream out = new FileOutputStream("./src/main/resources/dat/rdf/referenceLinks.ttl");
        RDFDataMgr.write(out, model, Lang.TTL);
        DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(REFERENCE_LINKS_FUSEKI_DATA_URL);
        accessor.putModel(model);
        referenceLinks.commit();
    }

    private static void prepareDraft(Dataset source, Dataset target, Dataset referenceLinks, Dataset draft) throws FileNotFoundException {
        UpdateExecutionFactory.create(UpdateFactory.create("clear all"), draft).execute();

        draft.begin(ReadWrite.WRITE);
        Model draftModel = draft.getDefaultModel();
        draftModel.getNsPrefixMap().clear();
        draft.commit();

        draft.begin(ReadWrite.WRITE);
        source.begin(ReadWrite.READ);
        Model sourceModel = source.getDefaultModel();
        draftModel.setNsPrefixes(sourceModel.getNsPrefixMap());
        draftModel.add(sourceModel);
        source.end();

        target.begin(ReadWrite.READ);
        Model targetModel = target.getDefaultModel();
        draftModel.setNsPrefixes(targetModel.getNsPrefixMap());
        draftModel.add(targetModel);
        target.end();

        BufferedReader br = new BufferedReader(new FileReader(ALIGNMENT_TO_SAME_AS_RULES));
        List rules = Rule.parseRules(Rule.rulesParserFromReader(br));
        // Infer sameAS links from mappings
        referenceLinks.begin(ReadWrite.READ);
        Model linksModel = referenceLinks.getDefaultModel();
        Reasoner reasoner = new GenericRuleReasoner(rules);
        reasoner.setDerivationLogging(true);
        InfModel inf = ModelFactory.createInfModel(reasoner, linksModel);
        Model deductions = inf.getDeductionsModel();
        draftModel.add(deductions);
        referenceLinks.end();

        DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(DRAFT_FUSEKI_DATA_URL);
        accessor.putModel(draftModel);
        draft.commit();
    }
}
