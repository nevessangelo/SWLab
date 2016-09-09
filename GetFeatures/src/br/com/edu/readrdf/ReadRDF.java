/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.readrdf;

import br.com.edu.dbpediaspotlight.DBPediaSpotlight;
import java.io.File;
import java.io.StringWriter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

/**
 *
 * @author angelo
 */
public class ReadRDF {
    
    public static void main(String[] args) throws Exception {
        File arquivos[];
        File diretorio = new File("/home/angelo/SWLab/GetFeatures/Dumps/rkb-explorer-acm/models/");
        arquivos = diretorio.listFiles();
        for(int i = 0; i < arquivos.length; i++){
            Model model = ModelFactory.createDefaultModel();
            String arquivo = arquivos[i].toString();
            model.read(arquivo, "TURTLE");
            StringWriter out = new StringWriter();
            model.write(out, "TURTLE");
            String result = out.toString();
            DBPediaSpotlight dbpedia = new DBPediaSpotlight();
            dbpedia.getEntity(result);
        }


        
    }
    
}
