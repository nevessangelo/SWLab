/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.mongodb;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author angelo
 */
public class Connection {

    public static void main(String[] args) {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase("data_catalog");
        System.out.println(db.getName());
        
       for (String name : db.listCollectionNames())
            System.out.println(name);
       
        System.out.println("Pegando collection dos voids...");
        MongoCollection coll = db.getCollection("voids");
        
        System.out.println("Mostrando objetos..");
        
        try (MongoCursor<Document> cur = coll.find().iterator()) {
            while (cur.hasNext()) {
                Document doc = cur.next();
                List list = new ArrayList(doc.values());
                System.out.print(list.get(1));
                System.out.print(": ");
                System.out.println(list.get(2));
            }
            
        }

        
        
       // System.out.println(coll.toString());

        
        mongoClient.close();
    }
}
