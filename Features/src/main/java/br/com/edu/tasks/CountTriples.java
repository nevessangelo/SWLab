/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.tasks;

import br.com.edu.Connection.InsertFeaturesBD;
import static br.com.edu.tasks.ReadRDF.Read;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import uff.ic.swlab.commons.util.Config;
import uff.ic.swlab.commons.util.Executor;

/**
 *
 * @author angelo
 */
public class CountTriples implements Runnable {

    private String name_dataset;
    private String caminho;
    private Connection conn;

    public CountTriples(String caminho, String name_dataset, Connection conn) {
        this.caminho = caminho;
        this.name_dataset = name_dataset;
        this.conn = conn;
    }

    public static Dataset Read(File files_dump) throws InterruptedException, ExecutionException, TimeoutException {
        Callable<Dataset> task = () -> {
            Dataset tempDataset = DatasetFactory.create();
            try {
                Lang[] langs = {null, Lang.TURTLE, Lang.RDFXML, Lang.NTRIPLES, Lang.TRIG,
                    Lang.NQUADS, Lang.JSONLD, Lang.RDFJSON, Lang.TRIX, Lang.RDFTHRIFT};
                for (Lang lang : langs) {
                    try {
                        if (lang == null) {
                            org.apache.jena.riot.RDFDataMgr.read(tempDataset, files_dump.toString());
                            return tempDataset;
                        } else {
                            org.apache.jena.riot.RDFDataMgr.read(tempDataset, files_dump.toString(), lang);
                            return tempDataset;
                        }
                    } catch (Throwable e) {
                        continue;
                    }
                }
            } catch (Throwable e) {
                return tempDataset;
            }
            return DatasetFactory.create();

        };
        return Executor.execute(task, Config.MODEL_READ_TIMEOUT);

    }

    public static int Triples(Dataset ds, String name_dataset, Connection conn) {
        int total_triples = 0;
        String qr = "SELECT (COUNT (*) as ?total) \n"
                + "WHERE {\n"
                + "  ?subject ?predicate ?object.\n"
                + "}";
        Query qy = QueryFactory.create(qr);
        QueryExecution qe = QueryExecutionFactory.create(qy, ds);
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution soln = rs.nextSolution();
            Literal frequencia = soln.getLiteral("total");
            total_triples = frequencia.getInt();
            
        }
        return total_triples;

    }

    @Override
    public void run() {
        File files_dump[];
        File files_directory[];
        File name_dump = new File(caminho);
        files_dump = name_dump.listFiles();
        int total = 0;
        for (int j = 0; j < files_dump.length; j++) {
            int aux = 0;
            if (files_dump[j].isDirectory()) {
                String path_diretory = files_dump[j].toString();
                File file_directory = new File(path_diretory);
                files_directory = file_directory.listFiles();
                for (int k = 0; k < files_directory.length; k++) {
                    Dataset ds = null;
                    try {
                        ds = Read(files_directory[k]);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ReadRDF.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ExecutionException ex) {
                        Logger.getLogger(ReadRDF.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (TimeoutException ex) {
                        Logger.getLogger(ReadRDF.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (ds != null || !ds.equals("") || !ds.equals("null")) {
                        int total_triples = Triples(ds, name_dataset, conn);
                        total = total_triples + aux;
                        aux = total;
                    }
                }

            } else {
                Dataset ds = null;
                try {
                    System.out.println("Read file: " + files_dump[j]);
                    ds = Read(files_dump[j]);
                } catch (InterruptedException ex) {
                    //Logger.getLogger(ReadRDF.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    //Logger.getLogger(ReadRDF.class.getName()).log(Level.SEVERE, null, ex);
                } catch (TimeoutException ex) {
                    System.out.println("Time out estourado do arquivo: " + files_dump[j]);
                    // Logger.getLogger(ReadRDF.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (ds != null || !ds.equals("") || !ds.equals("null")) {
                    int total_triples = Triples(ds, name_dataset, conn);
                    total = total_triples + aux;
                    aux = total;
                }

            }
        }
        try {
            InsertFeaturesBD.UpdateTriples(name_dataset, total, conn);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CountTriples.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CountTriples.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}
