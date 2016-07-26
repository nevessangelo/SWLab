package ic.uff.tic10086.examples.jena;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Iterator;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.reasoner.ValidityReport;
import org.apache.jena.reasoner.rulesys.RDFSRuleReasonerFactory;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.ReasonerVocabulary;

public class InferenceOld {

    public static void main(String[] args) throws FileNotFoundException {
        // Assembler way: Make a TDB-back Jena model in the named directory.
        // This way, you can change the model being used without changing the code.
        // The assembler file is a configuration file.
        // The same assembler description will work in Fuseki.
        String assemblerFile = "./conf/tdb-assembler.ttl";
        Dataset dataset = TDBFactory.assembleDataset(assemblerFile);

        {
            // transaction 0
            dataset.begin(ReadWrite.WRITE);
            Model model = dataset.getDefaultModel();
            model.removeAll();
            model.getNsPrefixMap().entrySet().stream().forEach((entry) -> {
                model.removeNsPrefix(entry.getKey());
            });
            dataset.commit();
            dataset.end();
        }

        {
            //transaction 1
            String NS = "urn:x-hp-jena:eg/";
            // Build a trivial example data set
            dataset.begin(ReadWrite.WRITE);
            Model rdfsExample = dataset.getDefaultModel();
            Property p = rdfsExample.createProperty(NS, "p");
            Property q = rdfsExample.createProperty(NS, "q");
            rdfsExample.add(p, RDFS.subPropertyOf, q);
            rdfsExample.createResource(NS + "a").addProperty(p, "foo");

            InfModel inf = ModelFactory.createRDFSModel(rdfsExample);
            Resource a = inf.getResource(NS + "a");
            System.out.println("Statement: " + a.getProperty(q));
            dataset.commit();
            dataset.end();
        }

        {
            //transaction 2
            dataset.begin(ReadWrite.WRITE);
            Model rdfsExample = dataset.getDefaultModel();
            Reasoner reasoner = ReasonerRegistry.getRDFSReasoner();
            InfModel inf = ModelFactory.createInfModel(reasoner, rdfsExample);
            // do some inference
            dataset.commit();
            dataset.end();
        }

        {
            // transaction 3
            dataset.begin(ReadWrite.WRITE);
            Model model = dataset.getDefaultModel();
            model.removeAll();
            model.getNsPrefixMap().entrySet().stream().forEach((entry) -> {
                model.removeNsPrefix(entry.getKey());
            });
            dataset.commit();
            dataset.end();
        }
        {
            //transaction 4
            String SOURCE = "http://www.eswc2006.org/technologies/ontology";
            String NS = SOURCE + "#";

            dataset.begin(ReadWrite.WRITE);
            Model rdfsExample = dataset.getDefaultModel();

            Model schema = ModelFactory.createDefaultModel();
            schema.read("http://www.eswc2006.org/www.eswc2006.org/technologies/ontology-content/2006-09-21.rdf");
            OntModel ontSchema = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, schema);
            OntClass paper = ontSchema.getOntClass(NS + "Paper");
            OntClass bestPaper = ontSchema.createClass(NS + "BestPaper");
            paper.addSubClass(bestPaper);
            paper.addDisjointWith(bestPaper);
            bestPaper.addDisjointWith(paper);
            FileOutputStream out = new FileOutputStream("./dat/rdf/example.owl", false);
            schema.write(out, "RDF/XML");

            Resource config = ModelFactory.createDefaultModel()
                    .createResource()
                    .addProperty(ReasonerVocabulary.PROPsetRDFSLevel, ReasonerVocabulary.RDFS_SIMPLE);
            Reasoner reasoner = RDFSRuleReasonerFactory.theInstance().create(config);
            reasoner.setParameter(ReasonerVocabulary.PROPsetRDFSLevel, ReasonerVocabulary.RDFS_SIMPLE);
            reasoner = ReasonerRegistry.getOWLReasoner();
            //reasoner = OWLFBRuleReasonerFactory.theInstance().create(null);
            //Reasoner boundReasoner = reasoner.bindSchema(schema);

            InfModel infmodel = ModelFactory.createInfModel(reasoner, schema);

            ValidityReport validity = infmodel.validate();
            if (validity.isValid())
                System.out.println("OK");
            else {
                System.out.println("Conflicts");
                for (Iterator i = validity.getReports(); i.hasNext();)
                    System.out.println(" - " + i.next());
            }

            dataset.commit();
            dataset.end();
        }

    }
}
