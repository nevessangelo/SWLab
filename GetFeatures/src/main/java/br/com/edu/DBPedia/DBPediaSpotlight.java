package br.com.edu.DBPedia;

import java.util.ArrayList;
import java.util.List;
import org.dbpedia.spotlight.exceptions.AnnotationException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author angelo
 */
public class DBPediaSpotlight {

    public static List<String> getEntity(String text) throws Exception {
        List<String> retorno = null;
        db c = new db();
        c.configiration(0.8, 80, "non", "CoOccurrenceBasedSelector", "Default", "yes");
        c.evaluate(text);
        if (c.getResu().size() > 0) {
            return c.getResu();
        }
        return null;
    }

    public static String getCategories(String Entite) throws Exception {
        db c = new db();
        c.configiration(0.8, 80, "non", "CoOccurrenceBasedSelector", "Default", "yes");
        c.evaluate2(Entite);
        String categories, armazenar;
        try {
            if (!c.getResu2().isEmpty()) {
                return categories = c.getResu2().get(0);
            } else {
                return null;
            }
        } catch (Throwable e) {
            return null;
        }

    }

}
