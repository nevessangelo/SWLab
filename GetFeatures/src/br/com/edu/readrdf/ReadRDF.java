/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.readrdf;

import java.io.File;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

/**
 *
 * @author angelo
 */
public class ReadRDF {
    
    public static void main(String[] args) {
        File arquivos[];
        File diretorio = new File("/home/angelo/SWLab/GetFeatures/Dumps/rkb-explorer-acm/models/");
        arquivos = diretorio.listFiles();
        for(int i = 0; i < arquivos.length; i++){
            Model model = ModelFactory.createDefaultModel();
            String arquivo = arquivos[i].toString();
            model.read(arquivo, "TURTLE") ;
            model.write(System.out);
        }


        
    }
    
}
