/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Utils;

import java.io.File;
import java.io.StringWriter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

/**
 *
 * @author angelo
 */
public class ReadRdf {
    
    public static void ReadUrl(String url){
        
    }

    public static void Read(File path) {
        File arquivos[];
        arquivos = path.listFiles();
        for (int i = 0; i < arquivos.length; i++) {
            if (arquivos[i].isDirectory()) {
                File path_archive = new File(arquivos[i].toString());
                File path_archives[];
                path_archives = path_archive.listFiles();
                for (int j = 0; j < path_archives.length; j++) {
                    Model model = ModelFactory.createDefaultModel();
                    String read = path_archives[j].toString();
                    model.read(read, "TURTLE");
                    StringWriter out = new StringWriter();
                    System.out.println(model.write(out, "TURTLE"));
                }
            } else {
                Model model = ModelFactory.createDefaultModel();
                String arquivo = arquivos[i].toString();
                model.read(arquivo, "TURTLE");
                StringWriter out = new StringWriter();
                System.out.println(model.write(out, "TURTLE"));
            }
        }
    }
}
