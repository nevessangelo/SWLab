/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.GetGroups;

import br.com.edu.Connection.MetodosMysql;
import java.sql.SQLException;
import java.util.ArrayList;
import uff.ic.swlab.datasetcrawler.adapter.Dataset;

/**
 *
 * @author angelo
 */
public class GetGroups {
    
public static int GetGroupDataset(String[] tags, String dataset) throws SQLException, ClassNotFoundException{
        int retorno = 0;
        ArrayList<String> grupos = new ArrayList();
        grupos.add("crossdomain");
        grupos.add("geography");
        grupos.add("government");
        grupos.add("lifesciences");
        grupos.add("linguistics");
        grupos.add("media");
        grupos.add("publications");
        //grupos.add("socialnetworking");
        grupos.add("socialweb");
        //grupos.add("usergenerated");
        grupos.add("usergeneratedcontent");
        
        for(int i = 0; i < tags.length; i++){
            String verifica_group = tags[i].replace(" ", "").toLowerCase();
            //System.out.println(verifica_group);
            for(String grup: grupos){
                if(verifica_group.equals(grup)){
                    MetodosMysql.InsertGroups(dataset, grup);
                    retorno = 1;
                    return retorno;
                    
                }else if((verifica_group.equals("bioportal")) || verifica_group.equals("bioportal") || verifica_group.equals("bio2rdf") || verifica_group.equals("bioinformatics")) {
                    grup = "lifesciences";
                    MetodosMysql.InsertGroups(dataset, grup);
                    retorno = 1;
                    return retorno;
                    
                }else if(verifica_group.equals("geographic")){
                    grup = "geography";
                    MetodosMysql.InsertGroups(dataset, grup);
                    retorno = 1;
                    return retorno;
                    
                }else if(verifica_group.equals("linquistics") || verifica_group.equals("linguistic") || verifica_group.equals("language"))  {
                    grup = "linguistics";
                    MetodosMysql.InsertGroups(dataset, grup);
                    retorno = 1;
                    return retorno;
                    
                }else if(verifica_group.equals("bibliographic")){
                    grup = "publications";
                    MetodosMysql.InsertGroups(dataset, grup);
                    retorno = 1;
                    return retorno;
                    
                }else if(verifica_group.equals("music")){
                    grup = "media";
                    MetodosMysql.InsertGroups(dataset, grup);
                    retorno = 1;
                    return retorno;
                    
                }
            }
        }
        return retorno;
        
      //  for (String link : datasets_groups){
      //      System.out.println(link);
      //  }
        
        
        
        
    }
    
}
