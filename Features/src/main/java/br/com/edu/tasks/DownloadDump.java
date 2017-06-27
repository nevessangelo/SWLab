/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.tasks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author angelo
 */
public class DownloadDump implements Runnable {

    private String nome_dataset;
    private String[] url;

    public DownloadDump(String nome_dataset, String[] url) {
        this.nome_dataset = nome_dataset;
        this.url = url;

    }

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
            if (size >= 1000) {
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

            }else{
                return 0;
            }

        } catch (MalformedURLException ex) {
            //Logger.getLogger(Download.class.getName()).log(Level.SEVERE, null, ex);
            //Datasets_difdump.add(name);
            return 0;

        }

    }

    public static int VerificaConexao(String url) throws MalformedURLException, IOException {
        try {
            java.net.URL mandarMail = new java.net.URL("http://www.guj.com.br");
            java.net.URLConnection conn = mandarMail.openConnection();
            java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) conn;
            httpConn.connect();
            int x = httpConn.getResponseCode();
            return x;
        } catch (java.net.MalformedURLException urlmal) {
        } catch (java.io.IOException ioexcp) {
        }
        return 0;
    }

    @Override
    public void run() {
        //File diretorio = new File(System.getProperty("user.dir") + "/Dumps/" + nome_dataset);
        File diretorio = new File("/media/angelo/DATA/Dumps2/" + nome_dataset);
        if (!diretorio.exists()) {
            for (String vetor_url : url) {
                try {
                    int conexao = VerificaConexao(vetor_url);
                    if (conexao == 200) {
                        try {
                            int retorno = GetDownload(vetor_url, diretorio, nome_dataset);
                            if (retorno == 1) {
                                String[] verifica_dump = vetor_url.split("/");
                                int tamanho = tamanho = verifica_dump.length;
                                String arquivo = diretorio.toString() + "/" + verifica_dump[tamanho - 1];
                            }
                        } catch (IOException ex) {
                        }

                    }
                } catch (IOException ex) {
                } catch (Throwable e) {
                }

            }
        }

    }

}
