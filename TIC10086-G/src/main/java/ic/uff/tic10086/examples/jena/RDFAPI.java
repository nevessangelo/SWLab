package ic.uff.tic10086.examples.jena;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.vocabulary.VCARD;

public class RDFAPI {

    public static void main(String[] args) throws FileNotFoundException {
        // Assembler way: Make a TDB-back Jena model in the named directory.
        // This way, you can change the model being used without changing the code.
        // The assembler file is a configuration file.
        // The same assembler description will work in Fuseki.
        String assemblerFile = "./conf/tdb-assembler.ttl";
        Dataset dataset = TDBFactory.assembleDataset(assemblerFile);
        dataset.begin(ReadWrite.WRITE);

        // Get model inside the transaction
        Model model = dataset.getDefaultModel();
        //Model model = dataset.getNamedModel("http://localhost:3030/graph97");
        model.removeAll();
        model.getNsPrefixMap().entrySet().stream().forEach((entry) -> {
            model.removeNsPrefix(entry.getKey());
        });
        model.read("http://www.w3.org/2006/vcard/ns#", "http://www.w3.org/2006/vcard/ns#");
        model.setNsPrefix("", "http://localhost:3030/myontology/ns/");
        model.setNsPrefix("vcard", "http://www.w3.org/2006/vcard/ns#");
        model.getProperty(assemblerFile);

        // create the resource
        // add the property
        String personURI = "http://localhost:3030/myontology/ns/JohnSmith";
        String givenName = "John";
        String familyName = "Smith";
        String fullName = givenName + " " + familyName;
        Resource johnSmith
            = model.createResource(personURI)
            .addProperty(VCARD.FN, fullName)
            .addProperty(VCARD.N,
                model.createResource()
                .addProperty(VCARD.Given, givenName)
                .addProperty(VCARD.Family, familyName));
        dataset.commit();

        StmtIterator iter = model.listStatements();
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement();  // get next statement
            Resource subject = stmt.getSubject();     // get the subject
            Property predicate = stmt.getPredicate();   // get the predicate
            RDFNode object = stmt.getObject();      // get the object

            System.out.println(subject.toString());
            System.out.println(" " + predicate.toString() + " ");
            if (object instanceof Resource)
                System.out.println(object.toString());
            else
                // object is a literal
                System.out.println(" \"" + object.toString() + "\"");
            System.out.println("");
            System.out.println(stmt.toString());
            System.out.println("===========================================\n");
        }
        FileOutputStream out = new FileOutputStream("./dat/rdf/example.rdf", false);
        model.write(out, "TURTLE");
        dataset.end();
    }
}
