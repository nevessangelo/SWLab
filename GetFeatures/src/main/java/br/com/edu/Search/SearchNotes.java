/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Search;

import br.com.edu.Connection.InsertFeaturesBD;
import br.com.edu.DBPedia.DBPediaSpotlight;
import static br.com.edu.DBPedia.DBPediaSpotlight.getCategories;
import br.com.edu.Objects.Entites;
import br.com.edu.Objects.Types;
import eu.trentorise.opendata.jackan.CkanClient;
import eu.trentorise.opendata.jackan.model.CkanDataset;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author angelo
 */
public class SearchNotes {
    
    
    public static void SearchTypes(String name_dataset, List<String> list_entites) throws Exception {
        double frequencia_types = 0;
        double frequencia_types_update = 0;
        double frequencia = 0;
        for (int i = 0; i < list_entites.size(); i++) {
            String result = getCategories(list_entites.get(i));
            if (result != null) {
                ArrayList<String> l_categories = new ArrayList();
                String armazenar;
                String category[] = result.split(",");
                int cont = 1;
                //armazenar = "Thing/" + cont;
                //l_categories.add(armazenar);
                for (Object array_category : category) {
                    if (array_category.toString().contains("DBpedia")) {
                        armazenar = array_category + "/" + cont;
                        l_categories.add(armazenar);
                        cont++;

                    }
                }
                for (Object teste : l_categories) {
                    frequencia_types = InsertFeaturesBD.VerificaUpdateTypes(name_dataset, (String) teste);
                    System.out.println("oi" + " " + frequencia_types);
                    if (frequencia_types == 0) {
                        frequencia = InsertFeaturesBD.UpdateFrequencia(name_dataset, list_entites.get(i));
                        Types types = new Types();
                        types.setName_dataset(name_dataset);
                        types.setFeature((String) teste);
                        types.setFrequen(frequencia);
                        types.setType("Notes");
                        InsertFeaturesBD.InsertTypes(types);
                    } else {
                        double frequen_total = 0;
                        frequencia_types_update = InsertFeaturesBD.GetFrequenType(name_dataset, (String) teste);
                        frequen_total = frequencia_types_update + frequencia_types;
                        InsertFeaturesBD.UpdateTypes(name_dataset, frequen_total, (String) teste);
                    }

                }
            }
        }

    }
    public static void Notes(CkanClient cc, List datasets) throws Exception {
        double frequencia = 0;
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
                
                SearchTypes(name, Entites);

            }
            
        }
        
    }
    
}
