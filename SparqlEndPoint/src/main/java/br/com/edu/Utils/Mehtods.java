/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Utils;

import br.com.edu.Connection.InsertFeaturesBD;
import br.com.edu.Objects.ClassPartition;
import br.com.edu.Objects.PropretyPartition;
import eu.trentorise.opendata.jackan.CkanClient;
import eu.trentorise.opendata.jackan.model.CkanDataset;
import eu.trentorise.opendata.jackan.model.CkanResource;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;

/**
 *
 * @author angelo
 */
public class Mehtods {
    
    public static void SearchProprety(String url, String nome_dataset){
        String qr = "SELECT (COUNT (?p) as ?freq) ?p\n"
                + "WHERE {\n"
                + "  { [] ?p [] }\n"
                + "  UNION { graph ?g {[] ?p [] }}\n"
                + "} \n"
                + "group by ?p "
                + "limit 20";
        
         try{
            QueryExecution q = QueryExecutionFactory.sparqlService(url, qr);
            ResultSet resultSet = q.execSelect();
            while (resultSet.hasNext()) {
                QuerySolution soln = resultSet.nextSolution();
                String proprety = String.valueOf(soln.get("p"));
                Literal frequencia = soln.getLiteral("freq");
                int num_frequencia = frequencia.getInt();
                PropretyPartition pp = new PropretyPartition();
                pp.setName_dataset(nome_dataset);
                pp.setFeature(proprety);
                pp.setFrequen(num_frequencia);
                pp.setType("sparql");
                InsertFeaturesBD.InsertProprety(pp);
                System.out.println("Proprety do dataset " + nome_dataset + "Proprety: " + proprety + "Frequencia: "+ num_frequencia);
            }
        }catch(Throwable e){
            System.out.println("Erro do dataset: "+ nome_dataset);
        }
        
    }
    
    
    public static void SearchClass(String url, String nome_dataset){
        String qr = "SELECT (COUNT (?cl) as ?freq) ?cl\n"
                + "WHERE {\n"
                + " 		{ [] a ?cl }\n"
                + " 		UNION { graph ?g {[] a ?cl }}\n"
                + "} \n"
                + "group by ?cl "
                + "limit 20";
        
        try{
            QueryExecution q = QueryExecutionFactory.sparqlService(url, qr);
            ResultSet resultSet = q.execSelect();
            while (resultSet.hasNext()) {
                QuerySolution soln = resultSet.nextSolution();
                String classpartition = String.valueOf(soln.get("cl"));
                Literal frequencia = soln.getLiteral("freq");
                int num_frequencia = frequencia.getInt();
                System.out.println("Class do dataset " + nome_dataset + "Class: " + classpartition + "Frequencia: "+ num_frequencia);
                ClassPartition classp = new ClassPartition();
                classp.setName_dataset(nome_dataset);
                classp.setFeature(classpartition);
                classp.setFrequen(num_frequencia);
                classp.setType("sparql");
                InsertFeaturesBD.InsertClass(classp);
            }
        }catch(Throwable e){
            System.out.println("Erro do dataset: "+ nome_dataset);
        }
        
        
        
    }

    public static int getResponseCODE(String urlString) throws IOException {
        URL u = new URL(urlString);
        int CODEResponse = 0;
        HttpURLConnection huc = (HttpURLConnection) u.openConnection();
        huc.setRequestMethod("GET");
        try {
            huc.connect();
        } catch (Throwable e) {
            return -1;
        } 
        CODEResponse = huc.getResponseCode();
        if (CODEResponse != 200) {
            return -1;

        } else {
            return 1;
        }
    }

    public static void SparqlEndPoint(CkanClient cc, List datasets) throws IOException, ClassNotFoundException, SQLException {
        int verifica = 0;
        int verifica_class = 0;
        int verifica_proprety = 0;
        String find = "sparql";
        List<String> Datasets_return = new ArrayList<>();
        List<CkanResource> resources = null;

        for (int i = 0; i < datasets.size(); i++) {
            int achei = 0;
            System.out.println(datasets.get(i));
            verifica_class = InsertFeaturesBD.VerificaClass((String) datasets.get(i));
            verifica_proprety = InsertFeaturesBD.VerificaProprety((String) datasets.get(i));
            if (verifica_class == 0 && verifica_proprety == 0) {
                CkanDataset d = cc.getDataset((String) datasets.get(i));
                resources = d.getResources();
                for (int j = 0; j < resources.size(); j++) {
                    String name_resource = resources.get(j).getName();
                    String name_description = resources.get(j).getDescription();
                    String format = resources.get(j).getFormat();
                    String url = resources.get(j).getUrl();

                    if (name_resource != null) {
                        if (name_resource.toLowerCase().contains(find) && url.toLowerCase().contains(find)) {
                            achei = 1;
                            verifica = getResponseCODE(url);
                            if (verifica == 1) {
                                System.out.println(url + " " + "OK");
                                SearchClass(url, (String) datasets.get(i));
                                SearchProprety(url, (String) datasets.get(i));
                                
                            }
                        }
                    }
                    if (name_description != null && achei == 0) {
                        if (name_description.toLowerCase().contains(find) && url.toLowerCase().contains(find)) {
                            achei = 1;
                            verifica = getResponseCODE(url);
                            if (verifica == 1) {
                                System.out.println(url + " " + "OK");
                                SearchClass(url, (String) datasets.get(i));
                                SearchProprety(url, (String) datasets.get(i));
                                
                            }
                        }
                    }
                    if (format != null && achei == 0) {
                        if (format.toLowerCase().contains(find) && url.toLowerCase().contains(find)) {
                            achei = 1;
                            verifica = getResponseCODE(url);
                            if (verifica == 1) {
                                System.out.println(url + " " + "OK");
                                SearchClass(url, (String) datasets.get(i));
                                SearchProprety(url, (String) datasets.get(i));
                                
                            }
                        }
                    }

                }

            }

        }

    }

}
