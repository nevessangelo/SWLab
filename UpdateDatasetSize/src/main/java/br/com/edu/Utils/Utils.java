/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Utils;

import br.com.edu.Connection.Methods;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.riot.Lang;

/**
 *
 * @author angelo
 */
public class Utils {

    public static Dataset read(File files_dump) {
        Dataset tempDataset = DatasetFactory.create();
        try {
            Lang[] langs = {null, Lang.TURTLE, Lang.RDFXML, Lang.NTRIPLES, Lang.TRIG,
                Lang.NQUADS, Lang.JSONLD, Lang.RDFJSON, Lang.TRIX, Lang.RDFTHRIFT};
            for (Lang lang : langs) {
                try {
                    if (lang == null) {
                        // Dataset tempDataset = DatasetFactory.create();
                        org.apache.jena.riot.RDFDataMgr.read(tempDataset, files_dump.toString());
                        //RDFDataMgr.write(System.out, tempDataset, Lang.NQ);
                        return tempDataset;
                    } else {
                        // Dataset tempDataset = DatasetFactory.create();
                        org.apache.jena.riot.RDFDataMgr.read(tempDataset, files_dump.toString(), lang);
                        //RDFDataMgr.write(System.out, tempDataset, lang);
                        return tempDataset;
                    }
                } catch (Throwable e) {
                    continue;

                }

            }

        } catch (Throwable e) {
            System.out.println("Error ao ler");
            return tempDataset;

        }
        return DatasetFactory.create();
    }

    public static void SearchTotalTriples(Dataset ds, String nome_dataset) throws ClassNotFoundException, SQLException {
        String nome = "http://linkeddatacatalog.dws.informatik.uni-mannheim.de/api/rest/dataset/"+"nome_dataset";
        int triplas = 0;
        String qr = "SELECT (count(*) as ?count) WHERE {\n"
                + "  {?s ?p ?o }\n"
                + "}";
        Query qy = QueryFactory.create(qr);
        QueryExecution qe = QueryExecutionFactory.create(qy, ds);
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution soln = rs.nextSolution();
            Literal total = soln.getLiteral("count");
            triplas = total.getInt();
        }
        if(triplas != 0){
            Methods.Update(triplas, nome);
        }
        
    }

    public static void VerificaDataset(ArrayList<String> nomes) throws ClassNotFoundException, SQLException {
        File file = new File("/media/angelo/DATA/Dumps/");
        File files[];
        files = file.listFiles();
        for (File file1 : files) {
            for (String nome : nomes) {
                if (file1.toString() == nome) {
                    System.out.println("Lendo os datasets da base: " + file1.toString());
                    String[] getNameDataset = file1.toString().split("/");
                    int size = getNameDataset.length - 1;
                    String name_dataset = getNameDataset[size];
                    File name_dump = new File(file.toString());
                    File[] files_dump = name_dump.listFiles();
                    for (File files_dump1 : files_dump) {
                        if (files_dump1.isDirectory()) {
                            String path_diretory = files_dump1.toString();
                            File file_directory = new File(path_diretory);
                            File[] files_directory = file_directory.listFiles();
                            for (int k = 0; k < files_directory.length; k++) {
                                Dataset ds = read(files_directory[k]);
                                if (ds != null) {
                                    SearchTotalTriples(ds,name_dataset);
                                }
                            }
                        }else{
                            Dataset ds = read(file1);
                            if (ds != null) {
                                SearchTotalTriples(ds,name_dataset);
                            }
                        }
                    }
                }
            }
        }
    }

    public static ArrayList<String> Datasets(ArrayList<String> datasets) {
        ArrayList<String> nome_datasets = new ArrayList<>();
        for (String dataset : datasets) {
            String[] v_nome = dataset.split("/");
            int tam = v_nome.length;
            String nome = v_nome[tam - 1];
            nome_datasets.add(nome);
        }

        return nome_datasets;

    }

}
