package uff.ic.swlab.test;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.net.MalformedURLException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.bson.Document;
import uff.ic.swlab.datacrawler.Dataset;

public class Test2 {

    public static void main(String[] args) {
        try {
            run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void run(String[] args) throws MalformedURLException {
        Logger.getRootLogger().setLevel(Level.OFF);
        System.out.println("Test2 started.");

        try (MongoClient mongo = new MongoClient("localhost", 27017)) {
            MongoDatabase db = mongo.getDatabase("data_catalog");
            MongoCollection<Document> datasets = db.getCollection("datasets");
            try (MongoCursor<Document> cursor = datasets.find().iterator()) {
                while (cursor.hasNext()) {
                    Dataset dataset = new Dataset(cursor.next());
                    if (dataset.getVoids().length > 0)
                        System.out.println(dataset.getVoids()[0]);
                    if (dataset.getNamespaces().length > 0)
                        System.out.println(dataset.getNamespaces()[0]);
                }
            }
        }
        System.out.println("Done.");
    }
}
