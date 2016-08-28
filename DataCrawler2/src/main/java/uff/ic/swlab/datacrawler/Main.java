package uff.ic.swlab.datacrawler;

import java.net.MalformedURLException;

import org.apache.jena.rdf.model.Model;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import uff.ic.swlab.utils.VoID;

public class Main {

	public static void main(String[] args) throws MalformedURLException {
		Logger.getRootLogger().setLevel(Level.OFF);

		Logger.getRootLogger().setLevel(Level.OFF);
		MongoClient mongo = new MongoClient("localhost", 27017);
		MongoDatabase db = mongo.getDatabase("data_catalog");
		MongoCollection<Document> datasets = db.getCollection("datasets");
		MongoCursor<Document> cursor = datasets.find().iterator();

		try {
			while (cursor.hasNext()) {
				Document dataset = cursor.next();
				Document extras2 = dataset.get("extras2", Document.class);
				String[] urls = extractURLs(extras2);
				String[] sparqlEndPoints = extractSparqlEndPoints(extras2);

				Model void_ = VoID.getVoID(sparqlEndPoints, urls);
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
