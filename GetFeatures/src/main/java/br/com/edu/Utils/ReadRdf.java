/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Utils;

import br.com.edu.Connection.InsertFeaturesBD;
import br.com.edu.DBPedia.DBPediaSpotlight;
import br.com.edu.objects.ClassPartition;
import br.com.edu.objects.Entites;
import br.com.edu.objects.PropretyPartition;
import java.io.File;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.tdb.TDBLoader;

/**
 *
 * @author angelo
 */
public class ReadRdf {

    public static void GetPropretyPartitions(Model model, String name_dataset) throws ClassNotFoundException, SQLException {
        String qr = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "\n"
                + "Select distinct ?p (count(?p) as ?freq)\n"
                + "where {?s ?p ?o\n"
                + "}\n"
                + "group by ?p\n"
                + "order by DESC (?freq)";

        Query qy = QueryFactory.create(qr);
        QueryExecution qe = QueryExecutionFactory.create(qy, model);
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            PropretyPartition proprety = new PropretyPartition();
            QuerySolution soln = rs.nextSolution();
            String propretypartition = String.valueOf(soln.get("p"));
            Literal frequencia = soln.getLiteral("freq");
            int num_frequencia = frequencia.getInt();
            proprety.setName(propretypartition);
            proprety.setFrequen(num_frequencia);
            proprety.setName_dataset(name_dataset);
            proprety.setType("dump");
            InsertFeaturesBD.InsertProprety(proprety);
        }

    }

    public static void GetClassPartitions(Model model, String name_dataset) throws ClassNotFoundException, SQLException {
        ArrayList<ClassPartition> list = new ArrayList<ClassPartition>();
        String qr = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "SELECT  ?class (count(?subject) as ?freq)\n"
                + "	 WHERE{ \n"
                + "  		   ?subject rdf:type ?class }\n"
                + "	  group by ?class\n"
                + "      limit 30";

        Query qy = QueryFactory.create(qr);
        QueryExecution qe = QueryExecutionFactory.create(qy, model);
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            ClassPartition classp = new ClassPartition();
            QuerySolution soln = rs.nextSolution();
            String classpartition = String.valueOf(soln.get("class"));
            Literal frequencia = soln.getLiteral("freq");
            int num_frequencia = frequencia.getInt();
            classp.setName(classpartition);
            classp.setFrequen(num_frequencia);
            classp.setName_dataset(name_dataset);
            classp.setType("dump");
            InsertFeaturesBD.InsertClass(classp);
        }
    }

    public static void GetFeatures(Model model, String name_dataset) throws Exception {
        int frequencia = 0;
        List<String> List_entites;
        String qr = "SELECT  ?object\n"
                + "	 WHERE { ?subject ?predicate ?object .\n"
                + "  	    	 FILTER isLiteral(?object)  \n"
                + "  \n"
                + "} ";
        Query qy = QueryFactory.create(qr);
        QueryExecution qe = QueryExecutionFactory.create(qy, model);
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution soln = rs.nextSolution();
            String literal = String.valueOf(soln.get("object")).toLowerCase();
            if (literal.length() > 60) {
                List_entites = DBPediaSpotlight.getEntity(literal);
                if (List_entites != null) {
                    for (int k = 0; k < List_entites.size(); k++) {
                        //int frequencia = DBPediaSpotlight.Frequen(List_entites.get(k), List_entites);
                        frequencia = InsertFeaturesBD.UpdateFrequencia(name_dataset, List_entites.get(k));
                        if (frequencia >= 1) {
                            InsertFeaturesBD.Update(name_dataset, frequencia, List_entites.get(k));
                        } else {
                            frequencia = 1;
                            Entites entites = new Entites();
                            entites.setName(List_entites.get(k));
                            entites.setFrequen(frequencia);
                            InsertFeaturesBD.InsertEntites(name_dataset, entites, "dump");

                        }

                    }

                }

            }

        }
        GetClassPartitions(model, name_dataset);
        GetPropretyPartitions(model, name_dataset);

    }

    public static int Read(File path, String name_dataset) throws Exception {
        int retorno;
        File arquivos[];
        arquivos = path.listFiles();
        for (int i = 0; i < arquivos.length; i++) {
            if (arquivos[i].isDirectory()) {
                File path_archive = new File(arquivos[i].toString());
                File path_archives[];
                path_archives = path_archive.listFiles();
                for (int j = 0; j < path_archives.length; j++) {
                    String read = path_archives[j].toString();
                    Model model = ModelFactory.createDefaultModel();
                    try{
                        RDFDataMgr.read(model, read);
                    }catch (Exception e){
                        return 1;
                    }
                    
                    GetFeatures(model, name_dataset);
                  
                }

            } else {
                Model model = ModelFactory.createDefaultModel();
                String arquivo = arquivos[i].toString();
                try{
                    RDFDataMgr.read(model, arquivo);
                }catch (Exception e){
                        return 1;
                }
                GetFeatures(model, name_dataset);
               
            }
        }
        return 0;

    }

//    public static void ReadUrl(String url_name, String name_dataset) throws Exception {
//        Lang[] langs = {Lang.TURTLE, Lang.RDFXML, Lang.NTRIPLES, Lang.TRIG,
//                    Lang.NQUADS, Lang.JSONLD, Lang.RDFJSON, Lang.TRIX, Lang.RDFTHRIFT};
//                for (Lang lang : langs) {
//                    Model tempModel = ModelFactory.createDefaultModel();
//                    RDFDataMgr.read(tempModel, url_name, lang);
//                    StringWriter out = new StringWriter();
//                    String result = out.toString();
//                    tempModel.write(out, "TURTLE");
//                    List<String> Entites = DBPediaSpotlight.getEntity(result);
//                    for (int k = 0; k < Entites.size(); k++) {
//                        int frequencia = DBPediaSpotlight.Frequen(Entites.get(k), Entites);
//                        Entites entites = new Entites();
//                        entites.setName(Entites.get(k));
//                        entites.setFrequen(frequencia);
//                        InsertFeaturesBD.InsertEntites(name_dataset, entites, "dump");
//                        frequencia = 0;
//                    }
//
//                    
//                }
//        
//
//    }
//
//    public static void Read(File path, String name_dataset) throws Exception {
//        File arquivos[];
//        arquivos = path.listFiles();
//        for (int i = 0; i < arquivos.length; i++) {
//            if (arquivos[i].isDirectory()) {
//                File path_archive = new File(arquivos[i].toString());
//                File path_archives[];
//                path_archives = path_archive.listFiles();
//                for (int j = 0; j < path_archives.length; j++) {
//                    Model model = ModelFactory.createDefaultModel();
//                    String read = path_archives[j].toString();
//                    model.read(read, "TURTLE");
//                    StringWriter out = new StringWriter();
//                    model.write(out, "TURTLE");
//                    String result = out.toString();  
//                    List<String> Entites = DBPediaSpotlight.getEntity(result);
//                    for (int k = 0; k < Entites.size(); k++) {
//                        int frequencia = DBPediaSpotlight.Frequen(Entites.get(k), Entites);
//                        Entites entites = new Entites();
//                        entites.setName(Entites.get(k));
//                        entites.setFrequen(frequencia);
//                        InsertFeaturesBD.InsertEntites(name_dataset, entites, "dump");
//                        frequencia = 0;
//                    }
//                    //System.out.println(model.write(out, "TURTLE"));
//                }
//            } else {
//                Dataset dataset = TDBFactory.createDataset();
//                Model model = dataset.getNamedModel("/home/angelo/WebSemantica/apache-jena-fuseki/apache-jena-fuseki-2.4.0/run/configuration/Read.ttl");
//                TDBLoader.loadModel(model, "/home/angelo/WebSemantica/apache-jena-fuseki/apache-jena-fuseki-2.4.0/run/configuration/Read.ttl");
//                //Model model = ModelFactory.createDefaultModel();
//                String arquivo = arquivos[i].toString();
//                model.read(arquivo, "TURTLE");
//                StringWriter out = new StringWriter();
//                model.write(out, "TURTLE");
//                String result = out.toString();
//                List<String> Entites = DBPediaSpotlight.getEntity(result);
//                for (int k = 0; k < Entites.size(); k++) {
//                    int frequencia = DBPediaSpotlight.Frequen(Entites.get(k), Entites);
//                    Entites entites = new Entites();
//                    entites.setName(Entites.get(k));
//                    entites.setFrequen(frequencia);
//                    InsertFeaturesBD.InsertEntites(name_dataset, entites, "dump");
//                    frequencia = 0;
//                }
//                //System.out.println(model.write(out, "TURTLE"));
//            }
//        }
//    }
}
