/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
        } catch (Throwable t) {
            return -1;
        }
    }

    public static int GetDownload(String url_name, File diretorio, String name) throws IOException {
        int size = 0;
        try {
            URL url = new URL(url_name);
            size = getsizeFile(url);
            if (size >= 6000) {

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
                return 1;

            } else {
                //Datasets_difdump.add(name);
                return 0;
            }

        } catch (MalformedURLException ex) {
            //Logger.getLogger(Download.class.getName()).log(Level.SEVERE, null, ex);
            //Datasets_difdump.add(name);
            return 0;

        }

    }

    public static void DownloadDump(File diretorio, String nome_dataset, String url_name) throws IOException, FileNotFoundException, Exception {
        
        
        
            int retorno = GetDownload(url_name, diretorio, nome_dataset);
            if (retorno == 1) {
                String[] verifica_dump = url_name.split("/");
                int tamanho = tamanho = verifica_dump.length;
                String arquivo = diretorio.toString() + "/" + verifica_dump[tamanho - 1];
            }
        

    }

}
