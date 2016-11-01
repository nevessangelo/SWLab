/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Utils;

import java.util.ArrayList;
import java.net.URL;
import br.com.edu.objects.Resource;
import com.github.junrar.exception.RarException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

/**
 *
 * @author angelo
 */
public class Download {

    public static String verificazip(String url_name, File path, String name) {
        ArrayList<String> zips = new ArrayList<>();
        zips.add("tgz");
        zips.add("rar");
        zips.add("tar.gz");
        zips.add("gz");
        zips.add("zip");
        zips.add("tar");
        zips.add("bz2");

        for (int i = 0; i < zips.size(); i++) {
            int j = url_name.toLowerCase().indexOf(zips.get(i));
            if (j > 0) {
                return zips.get(i);
            }
        }
        return "";
    }
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

    public static int GetDownload(String url_name, File diretorio, String name) throws IOException {
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

    public static ArrayList DownloadDump(ArrayList<Resource> Datasets_Dump, ArrayList<String> Datasets_difdump) throws IOException, FileNotFoundException, RarException, ArchiveException, Exception {
        for (int i = 6; i < Datasets_Dump.size(); i++) {
            String name = Datasets_Dump.get(i).getName();
            String url_name = Datasets_Dump.get(i).getUrl();
            File diretorio = new File(System.getProperty("user.dir") + "/Dumps/" + name);
            String extensao = verificazip(url_name, diretorio, name);
            if (extensao != null) {
                int retorno = GetDownload(url_name, diretorio, name);
                if (retorno == 1) {
                    String[] verifica_dump = url_name.split("/");
                    int tamanho = tamanho = verifica_dump.length;
                    String arquivo = diretorio.toString() + "/" + verifica_dump[tamanho - 1];
                    File arquivo_extrair = new File(arquivo);
                    int valor = Unzip.extract(arquivo_extrair, name, extensao);
                    if(valor == 1){
                        int resultado = ReadRdf.Read(diretorio, name);
                        if(resultado == 1){
                            Datasets_difdump.add(name);
                        }
                    }else{
                        Datasets_difdump.add(name);
                    }
                    
                }else{
                    Datasets_difdump.add(name);
                }

            } else {
                int retorno = GetDownload(url_name, diretorio, name);
                if(retorno == 1){
                    int resultado = ReadRdf.Read(diretorio, name);
                    if(resultado == 1){
                        Datasets_difdump.add(name);
                    }
                }else{
                    Datasets_difdump.add(name);
                }
                
            }

        }
        return Datasets_difdump;
    }

}
