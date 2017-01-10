/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Main;

import br.com.edu.Utils.Mehtods;
import eu.trentorise.opendata.jackan.CkanClient;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author angelo
 */
public class Main {
    
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        CkanClient cc = new CkanClient("http://linkeddatacatalog.dws.informatik.uni-mannheim.de");
        ArrayList<String> datasets = new ArrayList<String>();
        datasets = (ArrayList<String>) cc.getDatasetList();
        Mehtods.SparqlEndPoint(cc, datasets);
        
    }
    
}
