/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.main;

import br.com.edu.getFetures.SearchDump;
import com.github.junrar.exception.RarException;
import eu.trentorise.opendata.jackan.CkanClient;
import eu.trentorise.opendata.jackan.model.CkanDataset;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author angelo
 */
public class Main {
    public static void main(String[] args) throws IOException, FileNotFoundException, RarException {
        CkanClient cc = new CkanClient("http://linkeddatacatalog.dws.informatik.uni-mannheim.de");
        ArrayList<String> datasets = new ArrayList<String>();
        SearchDump dump = new SearchDump();
       
                
        String name = "rkb-explorer-acm";
        CkanDataset d = cc.getDataset(name);

        datasets.add(d.getName());
        //datasets = (ArrayList<String>) cc.getDatasetList();
        dump.Search(cc, datasets);
        
    }
    
}
