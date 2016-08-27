package ic.uff.datacrawler;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MongoTeste {

	public static void main(String[] args) {
		Logger.getRootLogger().setLevel(Level.OFF);
		MongoClient mongo = new MongoClient("localhost", 27017);
		MongoDatabase db = mongo.getDatabase("data_catalog");
		MongoCollection<Document> datasets = db.getCollection("datasets");
		MongoCursor<Document> cursor = datasets.find().iterator();
		try {
			while (cursor.hasNext()) {
				Document doc = cursor.next();

			}
		} finally {
			cursor.close();
		}
		mongo.close();
	}

}
