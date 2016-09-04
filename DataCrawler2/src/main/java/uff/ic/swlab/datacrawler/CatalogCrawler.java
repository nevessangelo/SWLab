package uff.ic.swlab.datacrawler;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class CatalogCrawler extends Crawler<Dataset> {

    private MongoCursor<Document> cursor = null;
    private MongoClient mongo = null;

    public CatalogCrawler() {
        mongo = new MongoClient("localhost", 27017);
        MongoDatabase db = mongo.getDatabase("data_catalog");
        MongoCollection<Document> datasets = db.getCollection("datasets");

        cursor = datasets
                .find(new Document("extras2.catalog_name", "Mannheim Linked Data Catalog"))
                //.find(new Document("name", "rkb-explorer-acm"))
                .batchSize(1)
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
        try {
            cursor.close();
        } catch (Throwable t) {
        }
        try {
            mongo.close();
        } catch (Throwable t) {
        }
    }

}
