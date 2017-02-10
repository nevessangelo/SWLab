/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Search;

import br.com.edu.utils.*;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author angelo
 */
public class SearchDump {

    
    public static void run(String nome_dataset, String[] url) {
        //  File diretorio = new File(System.getProperty("user.dir") + "/Dumps/" + name);
        File diretorio = new File("/media/angelo/DATA/Dumps2/" + nome_dataset);
        if (!diretorio.exists()) {
            for (String vetor_url : url) {
                try {
                    Download.DownloadDump(diretorio, nome_dataset, vetor_url);
                } catch (Exception ex) {
                    Logger.getLogger(SearchDump.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
