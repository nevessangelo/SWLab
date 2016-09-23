/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Utils;

import br.com.edu.Connection.InsertEntitesBD;
import br.com.edu.DBPedia.DBPediaSpotlight;
import br.com.edu.objects.Entites;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

/**
 *
 * @author angelo
 */
public class ReadRdf {

    public static void ReadUrl(String url_name, String name_dataset) throws Exception {
        Lang[] langs = {Lang.TURTLE, Lang.RDFXML, Lang.NTRIPLES, Lang.TRIG,
                    Lang.NQUADS, Lang.JSONLD, Lang.RDFJSON, Lang.TRIX, Lang.RDFTHRIFT};
                for (Lang lang : langs) {
                    Model tempModel = ModelFactory.createDefaultModel();
                    RDFDataMgr.read(tempModel, url_name, lang);
                    StringWriter out = new StringWriter();
                    String result = out.toString();
                    tempModel.write(out, "TURTLE");
                    List<String> Entites = DBPediaSpotlight.getEntity(result);
                    for (int k = 0; k < Entites.size(); k++) {
                        int frequencia = DBPediaSpotlight.Frequen(Entites.get(k), Entites);
                        Entites entites = new Entites();
                        entites.setName(Entites.get(k));
                        entites.setFrequen(frequencia);
                        InsertEntitesBD.Insert(name_dataset, entites, "dump");
                        frequencia = 0;
                    }

                    
                }
        

    }

    public static void Read(File path, String name_dataset) throws Exception {
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
                    model.write(out, "TURTLE");
                    String result = out.toString();  
                    List<String> Entites = DBPediaSpotlight.getEntity(result);
                    for (int k = 0; k < Entites.size(); k++) {
                        int frequencia = DBPediaSpotlight.Frequen(Entites.get(k), Entites);
                        Entites entites = new Entites();
                        entites.setName(Entites.get(k));
                        entites.setFrequen(frequencia);
                        InsertEntitesBD.Insert(name_dataset, entites, "dump");
                        frequencia = 0;
                    }
                    //System.out.println(model.write(out, "TURTLE"));
                }
            } else {
                Model model = ModelFactory.createDefaultModel();
                String arquivo = arquivos[i].toString();
                model.read(arquivo, "TURTLE");
                StringWriter out = new StringWriter();
                model.write(out, "TURTLE");
                String result = out.toString();
                List<String> Entites = DBPediaSpotlight.getEntity(result);
                for (int k = 0; k < Entites.size(); k++) {
                    int frequencia = DBPediaSpotlight.Frequen(Entites.get(k), Entites);
                    Entites entites = new Entites();
                    entites.setName(Entites.get(k));
                    entites.setFrequen(frequencia);
                    InsertEntitesBD.Insert(name_dataset, entites, "dump");
                    frequencia = 0;
                }
                //System.out.println(model.write(out, "TURTLE"));
            }
        }
    }
}
