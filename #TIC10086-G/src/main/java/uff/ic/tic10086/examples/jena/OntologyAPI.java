package uff.ic.tic10086.examples.jena;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Iterator;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.Ontology;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.vocabulary.XSD;

public class OntologyAPI {

    public static void main(String[] args) throws FileNotFoundException {
        // Assembler way: Make a TDB-back Jena model in the named directory.
        // This way, you can change the model being used without changing the code.
        // The assembler file is a configuration file.
        // The same assembler description will work in Fuseki.
        String assemblerFile = "./conf/tdb-assembler.ttl";
        Dataset dataset = TDBFactory.assembleDataset(assemblerFile);

        String SOURCE = "http://www.eswc2006.org/technologies/ontology";
        String NS = SOURCE + "#";
        {
            // transaction 0
            dataset.begin(ReadWrite.WRITE);
            Model model = dataset.getDefaultModel();
            model.removeAll();
            model.getNsPrefixMap().entrySet().stream().forEach((entry) -> {
                model.removeNsPrefix(entry.getKey());
            });
            model.setNsPrefix("eswc", NS);
            dataset.commit();
            dataset.end();
        }

        {
            // transaction 1
            // Get model inside the transaction
            dataset.begin(ReadWrite.WRITE);
            Model model = dataset.getDefaultModel();

            // create the base model
            OntModel base = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, model);
            Ontology ont = base.createOntology(NS);
            ont.addImport(base.createResource("http://www.eswc2006.org/www.eswc2006.org/technologies/ontology-content/2006-09-21.rdf"));
            base.loadImports();
            base.listImportedOntologyURIs().stream().forEach((entry) -> {
                System.out.println(entry + "<< *****");
            });

            // create a dummy paper for this example
            OntClass paper = base.getOntClass(NS + "Paper");
            Individual p1 = base.createIndividual(NS + "paper1", paper);
            dataset.commit();
            dataset.end(); // if not commited then abort
        }

        {
            // transaction 2
            dataset.begin(ReadWrite.READ);
            Model model = dataset.getDefaultModel();
            OntModel base = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, model);
            Individual p1 = base.getIndividual(NS + "paper1");

            // list the asserted types
            for (Iterator<Resource> i = p1.listRDFTypes(false); i.hasNext();)
                System.out.println(p1.getURI() + " is asserted in class " + i.next());

            // create the reasoning model using the base
            OntModel inf = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF, base);

            // list the inferred types
            p1 = inf.getIndividual(NS + "paper1");
            for (Iterator<Resource> i = p1.listRDFTypes(false); i.hasNext();)
                System.out.println(p1.getURI() + " is inferred to be in class " + i.next());
            dataset.end();
        }

        {
            // transaction 3
            dataset.begin(ReadWrite.WRITE);
            Model model = dataset.getDefaultModel();
            OntModel base = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, model);
            OntClass paper = base.getOntClass(NS + "Paper");

            Resource r = base.getResource(NS + "Paper");
            paper = r.as(OntClass.class);
            paper = base.getOntClass(NS + "Paper");
            // paper = base.createClass(NS + "Paper");
            OntClass bestPaper = base.createClass(NS + "BestPaper");
            OntClass anonClass = base.createClass();
            dataset.commit();
            dataset.end();
        }
        {
            // transaction 4
            dataset.begin(ReadWrite.READ);
            Model model = dataset.getDefaultModel();
            OntModel base = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, model);
            OntClass artefact = base.getOntClass(NS + "Artefact");
            for (Iterator<OntClass> i = artefact.listSubClasses(); i.hasNext();) {
                OntClass c = i.next();
                System.out.println(c.getURI());
            }
            dataset.end();
        }

        {
            //transaction 5
            dataset.begin(ReadWrite.WRITE);
            Model model = dataset.getDefaultModel();
            OntModel base = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, model);
            OntClass programme = base.getOntClass(NS + "Programme");
            OntClass orgEvent = base.createClass(NS + "OrganizedEvent");

            DatatypeProperty subDeadline = base.getDatatypeProperty(NS + "hasSubmissionDeadline");
            DatatypeProperty notifyDeadline = base.getDatatypeProperty(NS + "hasNotificationDeadline");
            DatatypeProperty cameraDeadline = base.getDatatypeProperty(NS + "hasCameraReadyDeadline");
            DatatypeProperty deadline = base.createDatatypeProperty(NS + "deadline");
            deadline.addDomain(base.getOntClass(NS + "Call"));
            deadline.addRange(XSD.dateTime);
            deadline.addSubProperty(subDeadline);
            deadline.addSubProperty(notifyDeadline);
            deadline.addSubProperty(cameraDeadline);
            dataset.commit();
            dataset.end();
        }

        {
            // transaction 6
            dataset.begin(ReadWrite.READ);
            Model model = dataset.getDefaultModel();
            OntModel base = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, model);
            FileOutputStream out = new FileOutputStream("./dat/rdf/example.owl", false);
            base.write(out, "RDF/XML");
            dataset.end();
        }
    }
}
