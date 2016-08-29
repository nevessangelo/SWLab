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

        SparqlServer sparql = new SparqlServer();
        sparql.dataURL = "";

        MongoClient mongo = new MongoClient("localhost", 27017);
        MongoDatabase db = mongo.getDatabase("data_catalog");
        MongoCollection<Document> datasets = db.getCollection("datasets");
        MongoCursor<Document> cursor = datasets.find().iterator();

        try {
            while (cursor.hasNext()) {
                Dataset dataset = new Dataset(cursor.next());

                String name_uri = dataset.getNameURI();

                Set<String> u = new HashSet<>();
                u.add(dataset.getHomepage());
                u.addAll(Arrays.asList(dataset.getNamespaces()));
                u.addAll(Arrays.asList(dataset.getVoids()));
                u = u.stream()
                        .filter(line -> line != null)
                        .collect(Collectors.toSet());
                String[] urls = u.toArray(args);

                String[] sparqlEndPoints = Arrays.asList(dataset.getSparqlEndPoints())
                        .stream()
                        .filter(line -> line != null)
                        .collect(Collectors.toSet()).toArray(args);

                sparql.putModel(name_uri, VoID.getVoID(sparqlEndPoints, urls));

            }
        } finally {
            cursor.close();
        }
        mongo.close();
    }

    public static String[] extractURLs(Document doc) {
        String[] urls = null;

        // TODO Extrair urls das chaves 'homepage', 'voids', 'namespaces',
        // 'examples' e 'voids' do doc informado.
        return urls;
    }

    public static String[] extractSparqlEndPoints(Document doc) {
        String[] urls = null;

        // TODO Extrair valor da chave 'sparqlEndPoints' do doc informado.
        return urls;
    }

}
