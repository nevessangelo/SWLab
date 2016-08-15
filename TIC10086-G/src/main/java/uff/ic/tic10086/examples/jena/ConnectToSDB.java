package uff.ic.tic10086.examples.jena;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sdb.SDBFactory;
import org.apache.jena.sdb.Store;
import org.apache.jena.sdb.StoreDesc;
import org.apache.jena.sdb.sql.SDBConnection;
import org.apache.jena.sdb.util.StoreUtils;
import org.apache.jena.vocabulary.VCARD;

public class ConnectToSDB {

    private static final String RDF = "./src/main/resources/dat/rdf";

    public static void main(String[] args) {
        try {
            run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void run() throws SQLException, ClassNotFoundException, FileNotFoundException {
        String NS = "http://localhost:3030/myontology/";

        String jdbcurl = "jdbc:postgresql://localhost:5432/jena";
        String username = "postgres";
        String password = "fluminense";
        Class.forName("org.postgresql.Driver");
        Connection jdbcConnection = DriverManager.getConnection(jdbcurl, username, password);
        jdbcConnection.setSchema("sdb");

        SDBConnection conn = SDBFactory.createConnection(jdbcConnection);
        String assemblerFile = "./src/main/resources/conf/sdb-assembler.ttl";
        StoreDesc storeDesc = StoreDesc.read(assemblerFile);
        Store store = SDBFactory.connectStore(conn, storeDesc);
        Dataset dataset = SDBFactory.connectDataset(store);

        Model model = dataset.getDefaultModel();
        if (!StoreUtils.isFormatted(store))
            store.getTableFormatter().create();

        //dataset.begin(ReadWrite.WRITE);
        model.removeAll();
        model.getNsPrefixMap().entrySet().stream().forEach((entry) -> {
            model.removeNsPrefix(entry.getKey());
        });
        model.read(VCARD.getURI());
        model.setNsPrefix("", NS);
        model.setNsPrefix("vcard", VCARD.getURI());

        String personURI = NS + "JohnSmith";
        String givenName = "John";
        String familyName = "Smith";
        String fullName = givenName + " " + familyName;
        Resource johnSmith = model.createResource(personURI)
                .addProperty(VCARD.FN, fullName)
                .addProperty(VCARD.N,
                        model.createResource()
                        .addProperty(VCARD.Given, givenName)
                        .addProperty(VCARD.Family, familyName));
        //dataset.commit();
        //dataset.end();

        model.write(new FileOutputStream(RDF + "/connectToSDB.ttl", false), "TURTLE");
        store.close();
    }
}
