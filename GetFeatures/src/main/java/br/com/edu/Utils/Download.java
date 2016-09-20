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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author angelo
 */
public class Download {

    public static int getsizeFile(URL url) throws IOException {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.getInputStream();
            return conn.getContentLength();
        } catch (IOException e) {
            return -1;
        } finally {
            conn.disconnect();
        }
    }

    public static void DownloadDump(ArrayList<Resource> Datasets_Dump, ArrayList Datasets_difdump) throws IOException {
        for (int i = 0; i < Datasets_Dump.size(); i++) {
            String name = Datasets_Dump.get(i).getName();
            String url_name = Datasets_Dump.get(i).getUrl();
            File diretorio = new File(System.getProperty("user.dir") + "/Dumps/" + name);
            int size = 0;

            try {
                URL url = new URL(url_name);
                size = getsizeFile(url);
                if (size >= 5000) {
                    
                    if (!diretorio.exists()) {
                        diretorio.mkdir();
                    }

                    String nomeArquivoLocal = url.getPath();
                    String[] nomeArquivoLocalFinal = nomeArquivoLocal.split("/");
                    int pegar = nomeArquivoLocalFinal.length - 1;
                    InputStream is = url.openStream();
                    FileOutputStream fos = new FileOutputStream(diretorio + "/" + nomeArquivoLocalFinal[pegar]);
                    int umByte = 0;
                    System.out.println("Fazendo Download do Dump Dataset: " + name);
                    while ((umByte = is.read()) != -1) {
                        fos.write(umByte);
                    }
                    is.close();
                    fos.close();

                }else{
                    Datasets_difdump.add(name);
                }

            } catch (MalformedURLException ex) {
                //Logger.getLogger(Download.class.getName()).log(Level.SEVERE, null, ex);
                Datasets_difdump.add(name);

            }

        }
    }

}
