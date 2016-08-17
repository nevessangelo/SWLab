package uff.ic.tic10086.examples.jena;

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

public class ConnectToTDB {

    private static final String RDF = "./src/main/resources/dat/rdf";

    public static void main(String[] args) {
        try {
            run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void run() throws FileNotFoundException {
        String NS = "http://localhost:3030/myontology/";

        // Assembler way: Make a TDB-back Jena model in the named directory.
        // This way, you can change the model being used without changing the code.
        // The assembler file is a configuration file.
        // The same assembler description will work in Fuseki.
        String assemblerFile = "./src/main/resources/conf/tdb-assembler.ttl";
        Dataset dataset = TDBFactory.assembleDataset(assemblerFile);
        // Get model inside the transaction
        Model model = dataset.getDefaultModel();
        //Model model = dataset.getNamedModel("http://localhost:3030/graph97");

        dataset.begin(ReadWrite.WRITE);
        model.removeAll();
        model.getNsPrefixMap().entrySet().stream().forEach((entry) -> {
            model.removeNsPrefix(entry.getKey());
        });
        model.read(VCARD.getURI());
        model.setNsPrefix("", NS);
        model.setNsPrefix("vcard", VCARD.getURI());

        // create the resource
        // add the property
        String personURI = NS + "JohnSmith";
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
        dataset.end();

        model.write(new FileOutputStream(RDF + "/connectToTDB.ttl", false), "TURTLE");
    }
}
