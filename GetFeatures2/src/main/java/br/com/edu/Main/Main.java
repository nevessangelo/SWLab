/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Main;

import br.com.edu.Search.SearchDifDump;
import br.com.edu.Search.SearchDump;
import br.com.edu.Search.SearchNotes;
import br.com.edu.utils.ReadRdf;
import eu.trentorise.opendata.jackan.CkanClient;
import java.io.FileNotFoundException;
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

    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, SQLException, InterruptedException, Exception {
        CkanClient cc = new CkanClient("http://linkeddatacatalog.dws.informatik.uni-mannheim.de");
        ArrayList<String> datasets = new ArrayList<String>();
        SearchDump dump = new SearchDump();
        datasets = (ArrayList<String>) cc.getDatasetList();
       //String name = "agris";
       // CkanDataset d = cc.getDataset(name);
       // datasets.add(d.getName());

        try {
            //ReadRdf.Read();
            dump.Search(cc, datasets);
            SearchDifDump.Search(cc, datasets);
            SearchNotes.Notes(cc, datasets);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
