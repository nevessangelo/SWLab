package uff.ic.swlab.datacrawler;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.bson.Document;

public class CatalogCrawler extends Crawler<Dataset> {

    static String[] extractURLs(Dataset dataset) {
        String[] urls;
        Set<String> set = new HashSet<>();
        set.add(dataset.getHomepage());
        set.addAll(Arrays.asList(dataset.getNamespaces()));
        set.addAll(Arrays.asList(dataset.getExamples()));
        set.addAll(Arrays.asList(dataset.getVoids()));
        set = set.stream().filter((String line) -> line != null).collect(Collectors.toSet());
        urls = set.toArray(new String[0]);
        return urls;
    }

    static String[] extractSparqlEndPoints(Dataset dataset) {
        String[] sparqlEndPoints;
        sparqlEndPoints = Arrays.asList(dataset.getSparqlEndPoints()).stream().filter((String line) -> line != null).collect(Collectors.toSet()).toArray(new String[0]);
        return sparqlEndPoints;
    }

    private MongoCursor<Document> cursor = null;
    private MongoClient mongo = null;

    public CatalogCrawler() {
        mongo = new MongoClient("localhost", 27017);
        MongoDatabase db = mongo.getDatabase("data_catalog");
        MongoCollection<Document> datasets = db.getCollection("datasets");

        cursor = datasets
                .find(new Document("extras2.catalog_name", "Mannheim Linked Data Catalog"))
                .noCursorTimeout(true)
                .iterator();
    }

    @Override
    public boolean hasNext() {
        boolean hasNext = cursor.hasNext();
        return hasNext;
    }

    @Override
    public Dataset next() {
        return new Dataset(cursor.next());
    }

    @Override
    public void close() {
        cursor.close();
        mongo.close();
    }

}
