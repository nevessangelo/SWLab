/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.dbpediaspotlight;

import br.com.edu.Connection.ConnectionMySql;
import br.com.edu.getFeaturesDatabase.GetFeaturesDatabase;
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
public class DBPediaSpotlight {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        
        try (MongoClient mongo = new MongoClient("localhost", 27017)) {

            MongoDatabase db = mongo.getDatabase("data_catalog");
            MongoCollection<Document> datasets = db.getCollection("datasets");

            try (MongoCursor<Document> cursor = datasets
                    .find(new Document("extras2.catalog_name", "Mannheim Linked Data Catalog"))
                    .noCursorTimeout(true)
                    .iterator()) {

                while (cursor.hasNext()) {
                    GetFeaturesDatabase getfeatures = new GetFeaturesDatabase(cursor.next());
                    String[] urls = extractURLs(getfeatures);
                   // System.out.println(urls[]);
                }
            }
        }
//        String question = "Semantic Web";
//        db c = new db();
//        c.configiration(0.0, 0, "non", "CoOccurrenceBasedSelector", "Default", "yes");
//        c.evaluate(question);
//        System.out.println("resource : " + c.getResu());
//        ConnectionMySql conexao = new ConnectionMySql();
//        Connection resultado = conexao.Conectar();
//        System.out.println(resultado);
    }
    
    private static String[] extractURLs(GetFeaturesDatabase getFeaturesDatabase) {
        String[] urls;
        Set<String> set = new HashSet<>();
        set.addAll(Arrays.asList(getFeaturesDatabase.getDumpRDF()));
        set = set.stream()
                .filter(line -> line != null)
                .collect(Collectors.toSet());
        urls = set.toArray(new String[0]);
        return urls;
    }

}

