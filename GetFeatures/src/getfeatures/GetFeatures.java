/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package getfeatures;

import br.com.edu.getFeaturesDatabase.DownloadDump;
import java.io.IOException;

/**
 *
 * @author angelo
 */
public class GetFeatures {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        DownloadDump download = new DownloadDump();
        download.gravaArquivoDeURL("http://acm.rkbexplorer.com/models/dump.tgz","/home/angelo/SWLab/GetFeatures/teste");
    }
    
}
