/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.tasks;

import br.com.edu.Connection.InsertFeaturesBD;
import br.com.edu.DBPedia.TypesDBWB;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author angelo
 */
public class GetTypes implements Runnable{
    
    private String nome_dataset;
    private Connection conn;
    
    public GetTypes(String nome_dataset, Connection conn){
        this.conn = conn;
        this.nome_dataset = nome_dataset;
    }

    @Override
    public void run() {
        try {
            ArrayList<String> list_entites = InsertFeaturesBD.GetEntites(nome_dataset, conn);
            for (String entites : list_entites) {
                entites = java.net.URLEncoder.encode(entites);
                TypesDBWB.getType(nome_dataset, entites, conn);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GetTypes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(GetTypes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
