/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.main;

import br.com.edu.DBPedia.DBPediaSpotlight;
import br.com.edu.Utils.ReadRdf;
import br.com.edu.Utils.Unzip;
import br.com.edu.getFetures.SearchDump;
import br.com.edu.getFetures.SearchNotes;
import com.github.junrar.exception.RarException;
import eu.trentorise.opendata.jackan.CkanClient;
import eu.trentorise.opendata.jackan.model.CkanDataset;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.compress.archivers.ArchiveException;
/**
 *
 * @author angelo
 */
public class Main {
    public static void main(String[] args) throws IOException, FileNotFoundException, RarException, ArchiveException, Exception {
        CkanClient cc = new CkanClient("http://linkeddatacatalog.dws.informatik.uni-mannheim.de");
        ArrayList<String> datasets = new ArrayList<String>();
        SearchDump dump = new SearchDump();
      
        String name = "asn-us";
        CkanDataset d = cc.getDataset(name);

        //datasets.add(d.getName());
        datasets = (ArrayList<String>) cc.getDatasetList();
        List datasets_notes = dump.Search(cc, datasets);
        SearchNotes.Notes(cc, datasets_notes);
        
        System.out.println("Fim da Extração");
        

        
      
        
    }
    
}
