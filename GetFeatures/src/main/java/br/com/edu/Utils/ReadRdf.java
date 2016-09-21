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

    public static void Read(File path) {
        File arquivos[];
        arquivos = path.listFiles();
        for (int i = 0; i < arquivos.length; i++) {
            Model model = ModelFactory.createDefaultModel();
            String arquivo = arquivos[i].toString();
            model.read(arquivo, "TURTLE");
            StringWriter out = new StringWriter();
            model.write(out, "TURTLE");
        }
    }
}
