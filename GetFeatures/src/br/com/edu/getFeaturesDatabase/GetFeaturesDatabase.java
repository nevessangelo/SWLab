/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.getFeaturesDatabase;

import br.com.edu.Connection.ConnectionMySql;
import br.com.edu.getFeaturesDatabase.GetFeaturesDatabase;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.sql.Connection;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.bson.Document;

/**
 *
 * @author angelo
 */
public class GetFeaturesDatabase {

    public static void main(String[] args) {

        try (MongoClient mongo = new MongoClient("localhost", 27017)) {

            MongoDatabase db = mongo.getDatabase("data_catalog");
            MongoCollection<Document> datasets = db.getCollection("datasets");

            try (MongoCursor<Document> cursor = datasets.find(new Document("extras2.catalog_name", "Mannheim Linked Data Catalog")).noCursorTimeout(true).iterator()) {

                while (cursor.hasNext()) {
                    Document document = cursor.next();
                    String nome = document.getString("name");
                    String teste = document.getString("notes");
                    System.out.println(nome + "--------"+ teste);

                }
            }
        }
    }
}