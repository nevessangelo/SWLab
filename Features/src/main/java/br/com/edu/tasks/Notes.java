/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.tasks;

import java.sql.Connection;

/**
 *
 * @author angelo
 */
public class Notes implements Runnable{
    
    private String name_dataset;
    private Connection conn;
    private String notes;
    
    public Notes(String name_dataset, String notes, Connection conn){
        this.name_dataset = name_dataset;
        this.notes = notes;
        this.conn = conn;
    }

    @Override
    public void run() {
        String notes_url = java.net.URLEncoder.encode(notes);
        br.com.edu.DBPedia.DBpediaWB.getEntite(notes_url, name_dataset, conn);
    }
    
    
}
