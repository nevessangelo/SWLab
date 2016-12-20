/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Search;

import br.com.edu.Connection.InsertFeaturesBD;
import br.com.edu.DBPedia.DBPediaSpotlight;
import br.com.edu.Objects.Entites;
import eu.trentorise.opendata.jackan.CkanClient;
import eu.trentorise.opendata.jackan.model.CkanDataset;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author angelo
 */
public class SearchNotes {
    public static void Notes(CkanClient cc, List datasets) throws Exception {
        int frequencia = 0;
        CkanDataset d = null;
        for (int i = 0; i < datasets.size(); i++) {
            String name = datasets.get(i).toString();
            try{
                 d = cc.getDataset(name);
            }catch(Throwable e){
                continue;
            }
            
            if(d == null)
                continue;
            String notes = d.getNotes();
            System.out.println(notes);
            if(notes == null)
                continue;
             List<String> Entites = DBPediaSpotlight.getEntity(notes);
             if (Entites != null) {
                for (int k = 0; k < Entites.size(); k++) {
                    frequencia = InsertFeaturesBD.UpdateFrequencia(name, Entites.get(k));
                    if (frequencia >= 1) {
                        InsertFeaturesBD.Update(name, frequencia, Entites.get(k));
                    } else {
                        frequencia = 1;
                        Entites entites = new Entites();
                        entites.setName_dataset(name);
                        entites.setFeature(Entites.get(k));
                        entites.setFrequen(frequencia);
                        entites.setType("notes");
                        InsertFeaturesBD.InsertEntites(entites);
                    }
                }

            }
            
        }
        
    }
    
}
