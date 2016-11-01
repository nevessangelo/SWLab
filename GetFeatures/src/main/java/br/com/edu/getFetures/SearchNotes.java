/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.getFetures;

import br.com.edu.Connection.InsertFeaturesBD;
import br.com.edu.DBPedia.DBPediaSpotlight;
import br.com.edu.objects.Entites;
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
        for (int i = 0; i < datasets.size(); i++) {
            String name = (String) datasets.get(i);
            CkanDataset d = cc.getDataset(name);
            String notes = d.getNotes().toLowerCase();
            List<String> Entites = DBPediaSpotlight.getEntity(notes);
            if (Entites != null) {
                for (int k = 0; k < Entites.size(); k++) {
                    frequencia = InsertFeaturesBD.UpdateFrequencia((String) datasets.get(i), Entites.get(k));
                    if (frequencia >= 1) {
                        InsertFeaturesBD.Update((String) datasets.get(i), frequencia, Entites.get(k));
                    } else {
                        frequencia = 1;
                        Entites entites = new Entites();
                        entites.setName(Entites.get(k));
                        entites.setFrequen(frequencia);
                        InsertFeaturesBD.InsertEntites((String) datasets.get(i), entites, "notes");
                    }
                }

            }

        }

    }

}
