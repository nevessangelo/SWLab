/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.DBPedia;

import java.util.List;

/**
 *
 * @author angelo
 */
public class DBPediaSpotlight {
    
       public static List<String> getEntity(String text) throws Exception{
        List<String> retorno = null;
        db c = new db();
        c.configiration(0.7, 0, "non", "CoOccurrenceBasedSelector", "Default", "yes");
        c.evaluate(text);
        if(c.getResu().size() > 0){
            return c.getResu();
        }
        return null; 
    }
    
}
