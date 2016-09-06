/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.getFeaturesDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author angelo
 */
public class DownloadDump {

    public static File gravaArquivoDeURL(String stringUrl, String pathLocal) throws MalformedURLException, IOException {
        try {
            URL url = new URL(stringUrl);
            String nomeArquivoLocal = url.getPath();
            InputStream is = url.openStream();
            FileOutputStream fos = new FileOutputStream(pathLocal + nomeArquivoLocal);
            int umByte = 0;
            while ((umByte = is.read()) != -1) {
                fos.write(umByte);
            }
            is.close();
            fos.close();
            return new File(pathLocal + nomeArquivoLocal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
