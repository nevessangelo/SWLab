/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.rdf;

import br.com.edu.Connection.InsertFeaturesBD;
import br.com.edu.object.ClassPartition;
import br.com.edu.object.PropretyPartition;
import eu.trentorise.opendata.jackan.CkanClient;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.tdb.TDBFactory;

/**
 *
 * @author angelo
 */
public class SPARQL {

    public static void getPropretyDump(String name_dateset, String url) throws ClassNotFoundException, SQLException {
        try {
            String serviceURI = url;
            String query = "SELECT (COUNT (?p) as ?freq) ?p\n"
                    + "WHERE {\n"
                    + "  { [] ?p [] }\n"
                    + "  UNION { graph ?g {[] ?p [] }}\n"
                    + "} \n"
                    + "group by ?p";
            QueryExecution q = QueryExecutionFactory.sparqlService(serviceURI, query);
            ResultSet rs = q.execSelect();
            while (rs.hasNext()) {
                QuerySolution soln = rs.nextSolution();
                String propretypartition = String.valueOf(soln.get("p"));
                Literal frequencia = soln.getLiteral("freq");
                int num_frequencia = frequencia.getInt();
                if (propretypartition != null) {
                    PropretyPartition pp = new PropretyPartition();
                    pp.setName_dataset(name_dateset);
                    pp.setFeature(propretypartition);
                    pp.setFrequen(num_frequencia);
                    pp.setType("sparql");
                    InsertFeaturesBD.InsertProprety(pp);
                }

            }

        } catch (Throwable e) {

        }

    }

    public static void getClassDump(String name_dataset, String url) throws ClassNotFoundException, SQLException {
        try {
            String serviceURI = url;
            String query = "SELECT (COUNT (?cl) as ?freq) ?cl\n"
                    + "WHERE {\n"
                    + " 		{[] a ?cl }\n"
                    + "} \n"
                    + "group by ?cl";
            QueryExecution q = QueryExecutionFactory.sparqlService(serviceURI, query);
            ResultSet rs = q.execSelect();
            while (rs.hasNext()) {
                QuerySolution soln = rs.nextSolution();
                String classpartition = String.valueOf(soln.get("cl"));
                Literal frequencia = soln.getLiteral("freq");
                int num_frequencia = frequencia.getInt();
                if (classpartition != null) {
                    ClassPartition classp = new ClassPartition();
                    classp.setName_dataset(name_dataset);
                    classp.setFeature(classpartition);
                    classp.setFrequen(num_frequencia);
                    classp.setType("sparql");
                    InsertFeaturesBD.InsertClass(classp);
                    System.out.println(classp.getFeature());
                    System.out.println(classp.getFrequen());
                }
            }
        } catch (Throwable e) {
        }

    }

    public static String getEndPoint(String name_dataset, Dataset ds2) {
        String url = null;
        String graph = "<http://linkeddatacatalog.dws.informatik.uni-mannheim.de/api/rest/dataset/" + name_dataset + ">";
        String qr = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "PREFIX void: <http://rdfs.org/ns/void#> "
                + "select distinct ?sparql "
                + "from named " + graph
                + " WHERE {{graph ?d1 {?d1 void:sparqlEndpoint ?sparql}}}";

        Query query = QueryFactory.create(qr);
        QueryExecution exec = QueryExecutionFactory.create(query, ds2);

        ResultSet rs = exec.execSelect();
        while (rs.hasNext()) {
            QuerySolution soln = rs.nextSolution();
            url = String.valueOf(soln.get("sparql"));
        }
        
        exec.close();

       

        return url;

    }
    
    public static Dataset ReadRdf(){
        String filename = "/home/angelo/Área de Trabalho/teste/void_2016-10-01_11-16-14.nq.gz";
        new File("/home/angelo/Área de Trabalho/teste/tdb").mkdirs();
        String assemblerFile = "/home/angelo/WebSemantica/apache-jena-fuseki/apache-jena-fuseki-2.4.0/run/configuration/readVoid.ttl";
        Dataset ds2 = TDBFactory.assembleDataset(assemblerFile);
        RDFDataMgr.read(ds2, filename);
        return ds2;
    }

    public static void getsparql() throws ClassNotFoundException, SQLException, InterruptedException {
        Dataset ds2 = ReadRdf();
        CkanClient cc = new CkanClient("http://linkeddatacatalog.dws.informatik.uni-mannheim.de");
        ArrayList<String> datasets = new ArrayList<String>();
        datasets = (ArrayList<String>) cc.getDatasetList();
        int result = 0, result2 = 0;
        for (int i = 0; i < datasets.size(); i++) {
            final String name_dateset = datasets.get(i);
            System.out.println("Dataset: " + name_dateset);
            result = InsertFeaturesBD.VerificaClass(name_dateset);
            if (result == 0) {
                final String url = getEndPoint(name_dateset,ds2);
                if(url != null){
                    System.out.println(url);
                       Thread thread = new Thread(new Runnable() {
                        public void run() {
                            try {  
                                getClassDump(name_dateset,url);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(SPARQL.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SQLException ex) {
                                Logger.getLogger(SPARQL.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                    thread.start();
                    thread.join(120000);
                    if (thread.isAlive()) {
                        thread.stop();
                    }

                    
                }
            }
            result2 = InsertFeaturesBD.VerificaProprety(name_dateset);
            if (result2 == 0) {
                final String url = getEndPoint(name_dateset, ds2);
                 if(url != null){
                    System.out.println(url);
                    Thread thread = new Thread(new Runnable() {
                        public void run() {
                            try {  
                                getPropretyDump(name_dateset,url);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(SPARQL.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SQLException ex) {
                                Logger.getLogger(SPARQL.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                    thread.start();
                    thread.join(120000);
                    if (thread.isAlive()) {
                        thread.stop();
                    }
                    
                }
            }
        }
        ds2.close();
    }

}
