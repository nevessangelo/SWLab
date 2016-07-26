package ic.uff.tic10086.examples.jena;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.Derivation;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ValidityReport;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.RDFSRuleReasonerFactory;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.ReasonerVocabulary;
import org.openrdf.model.vocabulary.XMLSchema;

public class Inference {

    private static final String RDF = "./src/main/resources/dat/rdf";

    public static void main(String[] args) {
        try {
            run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void run() throws FileNotFoundException {
        System.out.println("========================================================================");
        {
            // Assembler way: Make a TDB-back Jena model in the named directory.
            // This way, you can change the model being used without changing the code.
            // The assembler file is a configuration file.
            // The same assembler description will work in Fuseki.
            String assemblerFile = "./src/main/resources/conf/inference.ttl";
            Dataset dataset = TDBFactory.assembleDataset(assemblerFile);
            dataset.begin(ReadWrite.WRITE);

            // Get model inside the transaction
            Model data = dataset.getDefaultModel();
            //Model model = dataset.getNamedModel("http://localhost:3030/graph97");
            data.removeAll();
            data.getNsPrefixMap().entrySet().stream().forEach((entry) -> {
                data.removeNsPrefix(entry.getKey());
            });
            String NS = "urn:x-hp-jena:eg/";

            data.setNsPrefix("", NS);
            data.setNsPrefix("vcard", "http://www.w3.org/2006/vcard/ns#");
            data.getProperty(assemblerFile);

            // Build a trivial example data set
            System.out.println("1 ---");
            Model schema = ModelFactory.createDefaultModel();
            Property p = schema.createProperty(NS, "p");
            Property q = schema.createProperty(NS, "q");
            schema.add(p, RDFS.subPropertyOf, q);

            data.createResource(NS + "a").addProperty(p, "foo");

            Resource config = ModelFactory.createDefaultModel()
                    .createResource()
                    .addProperty(ReasonerVocabulary.PROPsetRDFSLevel, ReasonerVocabulary.RDFS_SIMPLE);
            Reasoner reasoner = RDFSRuleReasonerFactory.theInstance().create(config);
            Reasoner boundReasoner = reasoner.bindSchema(schema);
            InfModel inf = ModelFactory.createInfModel(boundReasoner, data);
            //InfModel inf = ModelFactory.createRDFSModel(rdfsExample);

            Resource a = inf.getResource(NS + "a");
            System.out.println("Statement: " + a.getProperty(q));

            data.write(new FileOutputStream(RDF + "/inference.ttl", false), "TURTLE");
            dataset.end();
        }

        {//Validity 1
            System.out.println("2 ---");
            Model model = ModelFactory.createDefaultModel();
            model.read("https://raw.githubusercontent.com/apache/jena/master/jena-core/testing/reasoners/rdfs/dttest2.nt", "NT");
            model.setNsPrefix("", "http://www.hpl.hp.com/semweb/2003/eg#");
            model.setNsPrefix("rdfs", RDFS.getURI());
            model.setNsPrefix("xmls", XMLSchema.NAMESPACE);
            model.write(new FileOutputStream(RDF + "/dttest2.nt", false), "NT");
            InfModel infmodel = ModelFactory.createRDFSModel(model);
            ValidityReport validity = infmodel.validate();
            if (validity.isValid())
                System.out.println("OK");
            else {
                System.out.println("Conflicts");
                for (Iterator i = validity.getReports(); i.hasNext();)
                    System.out.println(" - " + i.next());
            }
        }

        {//Validity 2
            System.out.println("3 ---");
            Model model = ModelFactory.createDefaultModel();
            model.read("https://raw.githubusercontent.com/apache/jena/master/jena-core/testing/reasoners/rdfs/dttest3.nt", "NT");
            model.setNsPrefix("", "http://www.hpl.hp.com/semweb/2003/eg#");
            model.setNsPrefix("rdfs", RDFS.getURI());
            model.setNsPrefix("xmls", XMLSchema.NAMESPACE);
            model.write(new FileOutputStream(RDF + "/dttest3.nt", false), "NT");
            InfModel infmodel = ModelFactory.createRDFSModel(model);
            ValidityReport validity = infmodel.validate();
            if (validity.isValid())
                System.out.println("OK");
            else {
                System.out.println("Conflicts");
                for (Iterator i = validity.getReports(); i.hasNext();)
                    System.out.println(" - " + i.next());
            }
        }

        {
            //Rules
            System.out.println("4 ---");
            String NS = "http://localhost/eg/";
            String data = "PREFIX eg: <" + NS + "> "
                    + "eg:A eg:p eg:B. "
                    + "eg:B eg:p eg:C. "
                    + "eg:C eg:p eg:D.";
            Model rawData = ModelFactory.createDefaultModel();
            rawData.read(new StringReader(data), NS, "TTL");
            rawData.setNsPrefix("eg", NS);
            String rules = "@prefix eg: <" + NS + ">.\n"
                    + "[rule1: (?a eg:p ?b) (?b eg:p ?c) -> (?a eg:p ?c)]";
            List<Rule> lrules = Rule.parseRules(Rule.rulesParserFromReader(new BufferedReader(new StringReader(rules))));
            Reasoner reasoner = new GenericRuleReasoner(lrules);
            reasoner.setDerivationLogging(true);
            InfModel inf = ModelFactory.createInfModel(reasoner, rawData);
            PrintWriter out = new PrintWriter(System.out);

            Resource A = rawData.getResource(NS + "A");
            Property p = rawData.getProperty(NS + "p");
            Resource D = rawData.getResource(NS + "D");
            for (StmtIterator i = inf.listStatements(A, p, D); i.hasNext();) {
                Statement s = i.nextStatement();
                System.out.println("Statement is " + s);
                for (Iterator id = inf.getDerivation(s); id.hasNext();) {
                    Derivation deriv = (Derivation) id.next();
                    deriv.printTrace(out, true);
                }
            }
            out.flush();
        }

        System.out.println("========================================================================");
    }
}
