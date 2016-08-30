package uff.ic.swlab.datacrawler;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.bson.Document;
import uff.ic.swlab.utils.SparqlServer;
import uff.ic.swlab.utils.VoID;

public class Main {

    public static void main(String[] args) {
        try {
            run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void run(String[] args) throws MalformedURLException {
        Logger.getRootLogger().setLevel(Level.OFF);
        System.out.println("Crawler started.");

        SparqlServer server = new SparqlServer();
        server.dataURL = "http://localhost:8080/fuseki/void/data";

        try (MongoClient mongo = new MongoClient("localhost", 27017)) {

            MongoDatabase db = mongo.getDatabase("data_catalog");
            MongoCollection<Document> datasets = db.getCollection("datasets");

            try (MongoCursor<Document> cursor = datasets.find().iterator()) {

                while (cursor.hasNext()) {
                    Dataset dataset = new Dataset(cursor.next());

                    String name_uri = dataset.getNameURI();

                    Set<String> u = new HashSet<>();
                    u.add(dataset.getHomepage());
                    u.addAll(Arrays.asList(dataset.getNamespaces()));
                    u.addAll(Arrays.asList(dataset.getExamples()));
                    u.addAll(Arrays.asList(dataset.getVoids()));
                    u = u.stream()
                            .filter(line -> line != null)
                            .collect(Collectors.toSet());
                    String[] urls = u.toArray(args);

                    String[] sparqlEndPoints = Arrays.asList(dataset.getSparqlEndPoints())
                            .stream()
                            .filter(line -> line != null)
                            .collect(Collectors.toSet()).toArray(args);

                    server.putModel(name_uri, VoID.getVoID(sparqlEndPoints, urls));
                }
            }
        }
        System.out.println("Done.");
    }
}
