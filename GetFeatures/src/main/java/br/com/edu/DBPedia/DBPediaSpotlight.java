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
    
    public static int Frequen(String key, List<String> Entites){
       int cont = 0;
        for(int i = 0; i < Entites.size(); i++){
            if(key.equals(Entites.get(i))){
                cont++;
            }
        }
        return cont;
    }

    /**
     * @param args the command line arguments
     */
     public static List<String> getEntity(String text) throws Exception{
        db c = new db();
        c.configiration(0.0, 0, "non", "CoOccurrenceBasedSelector", "Default", "yes");
        c.evaluate(text);
        if(c.getResu().size() > 0){
           // System.out.println("resource : " + c.getResu());
            return c.getResu();
        }
        return null;
    }

}
