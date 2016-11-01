/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.rdf;

import br.com.edu.Connection.InsertFeaturesBD;
import br.com.edu.DBPedia.DBPediaSpotlight;
import br.com.edu.object.Entites;
import eu.trentorise.opendata.jackan.CkanClient;
import eu.trentorise.opendata.jackan.model.CkanDataset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author angelo
 */
public class Notes {

    public static void getEntites(String name_dataset, String notes) throws ClassNotFoundException, SQLException, Exception {
        int frequencia = 0;
        try{
             List<String> list_entites = DBPediaSpotlight.getEntity(notes);
        if (list_entites != null) {
            for (int i = 0; i < list_entites.size(); i++) {
                frequencia = InsertFeaturesBD.UpdateFrequencia(name_dataset, list_entites.get(i));
                if (frequencia >= 1) {
                    InsertFeaturesBD.Update(name_dataset, frequencia, list_entites.get(i));
                } else {
                    frequencia = 1;
                    Entites entites = new Entites();
                    entites.setName_dataset(name_dataset);
                    entites.setFeature(list_entites.get(i));
                    entites.setFrequen(1);
                    entites.setType("notes");
                    InsertFeaturesBD.InsertEntites(entites);
                }
            }
        }
            
        }catch(Throwable e){
            
        }
       

    }

    public static void getNotes() throws ClassNotFoundException, SQLException, Exception {
        CkanClient cc = new CkanClient("http://linkeddatacatalog.dws.informatik.uni-mannheim.de");
        ArrayList<String> datasets = new ArrayList<String>();
        datasets = (ArrayList<String>) cc.getDatasetList();
        int result = 0;
        for (String dataset : datasets) {
            String name_dateset = dataset;
            System.out.println(name_dateset);
            result = InsertFeaturesBD.VerificaEntite(name_dateset);
            if (result == 0) {
                CkanDataset d = cc.getDataset((String) dataset);
                String notes = d.getNotes();
                if (notes != null) {
                    getEntites(name_dateset, notes);
                }
            }

        }
    }

}
