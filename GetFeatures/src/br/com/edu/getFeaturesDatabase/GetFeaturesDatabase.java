/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.getFeaturesDatabase;

import java.util.ArrayList;
import org.bson.Document;

/**
 *
 * @author angelo
 */
public class GetFeaturesDatabase {
    
    Document doc = null;
    
    public GetFeaturesDatabase(){
        
    }
    
    public GetFeaturesDatabase(Document doc) {
        this.doc = doc;
    }
  
    public String[] getDumpRDF() {
        try {
            ArrayList<String> getDumpRDF = doc.get("resources", Document.class).get("url", ArrayList.class);
            return getDumpRDF != null ? getDumpRDF.toArray(new String[0]) : new String[0];
        } catch (Exception e) {
            return new String[0];
        }
    }
    
}
