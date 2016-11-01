/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Main;

import br.com.edu.Connection.InsertBD;
import br.com.edu.object.Features;
import br.com.edu.object.RDF;
import java.sql.SQLException;

/**
 *
 * @author angelo
 */
public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        RDF.InserdBD();
//          Features features = new Features();
//          features.setName_dataset("http://teste");
//          features.setFeature("http://linkset");
//          features.setType_feature("linkset");
//          features.setFrequen(40000);
//          features.setDatasetSize(40);
//          InsertBD.InsertEntites(features);
    }
    
}
