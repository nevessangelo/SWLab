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

    public static void Read(String caminho) throws Exception {
        File arquivos[];
        DBPediaSpotlight dbpedia = new DBPediaSpotlight();
        File diretorio = new File(caminho);
        arquivos = diretorio.listFiles();
        if (arquivos == null) { //nao existe pasta
            Model model = ModelFactory.createDefaultModel();
            //String[] achar_arquivo = caminho.split("/");
            //int fim = achar_arquivo.length;
            String arquivo = diretorio.toString();
            model.read(arquivo, "TURTLE");
            StringWriter out = new StringWriter();
            model.write(out, "TURTLE");
            String result = out.toString();
            dbpedia.getEntity(result);
        } else {
            for (int i = 0; i < arquivos.length; i++) {
                Model model = ModelFactory.createDefaultModel();
                String arquivo = arquivos[i].toString();
                model.read(arquivo, "TURTLE");
                StringWriter out = new StringWriter();
                model.write(out, "TURTLE");
                String result = out.toString();
                dbpedia.getEntity(result);
            }
        }
    }
}
