/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Utils;

import java.util.ArrayList;
import java.net.URL;
import br.com.edu.objects.Resource;
import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author angelo
 */
public class Download {
    
    public static void DownloadDump(ArrayList<Resource> Datasets_Dump, ArrayList Datasets_difdump){
        for(int i = 0; i < Datasets_Dump.size(); i++){
            String name = Datasets_Dump.get(i).getName();
            String url_name = Datasets_Dump.get(i).getUrl();
            File diretorio = new File(System.getProperty("user.dir") + "/Dumps/" + name);
            //System.out.println(diretorio);
            
            try {
                URL url = new URL(url_name);
                if(!diretorio.exists())
                    diretorio.mkdir();
                String nomeArquivoLocal = url.getPath();
                
            } catch (MalformedURLException ex) {
                Logger.getLogger(Download.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
}
