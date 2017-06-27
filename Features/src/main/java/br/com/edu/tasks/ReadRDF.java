/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.tasks;

import br.com.edu.Connection.InsertFeaturesBD;
import br.com.edu.Objects.ClassPartition;
import br.com.edu.Objects.PropretyPartition;
import java.io.File;
import java.io.IOException;
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
import org.apache.log4j.PropertyConfigurator;
import uff.ic.swlab.commons.util.Config;
import uff.ic.swlab.commons.util.Executor;
import uff.ic.swlab.datasetcrawler.CatalogCrawler;

/**
 *
 * @author angelo
 */
public class ReadRDF implements Runnable {

    private String name_dataset;
    private String caminho;
    private Connection conn;

    public ReadRDF(String caminho, String name_dataset, Connection conn) {
        this.caminho = caminho;
        this.name_dataset = name_dataset;
        this.conn = conn;
    }

    public static Dataset Read(File files_dump) throws InterruptedException, ExecutionException, TimeoutException {
        Callable<Dataset> task = () -> {
            try {
                Lang[] langs = {null, Lang.TURTLE, Lang.RDFXML, Lang.NTRIPLES, Lang.TRIG,
                    Lang.NQUADS, Lang.JSONLD, Lang.RDFJSON, Lang.TRIX, Lang.RDFTHRIFT, Lang.NQ, Lang.N3, Lang.NT, Lang.TTL};
                for (Lang lang : langs) {
                    try {
                        if (lang == null) {
                            Dataset tempDataset = DatasetFactory.create();
                            org.apache.jena.riot.RDFDataMgr.read(tempDataset, files_dump.toString());
                            return tempDataset;
                        } else {
                            Dataset tempDataset = DatasetFactory.create();
                            org.apache.jena.riot.RDFDataMgr.read(tempDataset, files_dump.toString(), lang);
                            return tempDataset;
                        }

                    } catch (Throwable e) {
                        System.out.println(e + "do arquivo: "+files_dump.toString());
                    }
                }

            } catch (Throwable e) {
                System.out.println(e + "do arquivo: "+files_dump.toString());
            }
            return DatasetFactory.create();
        };
        return Executor.execute(task, Config.MODEL_READ_TIMEOUT);

    }

//
//    public static Dataset Read(File files_dump) throws InterruptedException, ExecutionException, TimeoutException {
//        Callable<Dataset> task = () -> {
//            Dataset tempDataset = DatasetFactory.create();
//            try {
//                Lang[] langs = {null, Lang.TURTLE, Lang.RDFXML, Lang.NTRIPLES, Lang.TRIG,
//                    Lang.NQUADS, Lang.JSONLD, Lang.RDFJSON, Lang.TRIX, Lang.RDFTHRIFT, Lang.NQ, Lang.N3, Lang.NT, Lang.TTL};
//                for (Lang lang : langs) {
//                    try {
//                        if (lang == null) {
//                            System.out.println("aqui");
//                            org.apache.jena.riot.RDFDataMgr.read(tempDataset, files_dump.toString());
//                            return tempDataset;
//                        } else {
//                            System.out.println(lang);
//                            org.apache.jena.riot.RDFDataMgr.read(tempDataset, files_dump.toString(), lang);
//                            return tempDataset;
//                        }
//                    } catch (Throwable e) {
//                        continue;
//                    }
//                }
//            } catch (Throwable e) {
//                return tempDataset;
//            }
//            return DatasetFactory.create();
//
//        };
//        return Executor.execute(task, Config.MODEL_READ_TIMEOUT);
//
//    }
    public static void SearchEntites(Dataset ds, String name_dataset, Connection conn) throws Exception {
        double frequencia = 0;
        String qr = "SELECT ?object\n"
                + "WHERE {\n"
                + "  { ?subject ?predicate ?object }\n"
                + "  UNION { graph ?g {?subject ?predicate ?object }}\n"
                + "} \n"
                + "group by ?object";

        Query qy = QueryFactory.create(qr);
        QueryExecution qe = QueryExecutionFactory.create(qy, ds);
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution soln = rs.nextSolution();
            String object = String.valueOf(soln.get("object"));
            if (object.length() >= 300) {
                //setInput.add(object);
                String text = java.net.URLEncoder.encode(object);
                //text = text.replaceAll("[^A-Za-z0-9()\\[\\]]", "");
                br.com.edu.DBPedia.DBpediaWB.getEntite(text, name_dataset, conn);
            }
        }

    }

    public static void SearchClass(Dataset ds, String name_dataset, Connection conn) throws ClassNotFoundException, SQLException {
        double frequen, frequencia_update = 0;
        String qr = "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "select ?class (count(?s) as ?freq)\n"
                + "WHERE {{?s rdf:type ?class} union {graph ?g {?s rdf:type ?class}}}\n"
                + "group by ?class\n"
                + "order by desc(?freq)";
//        String qr = "SELECT (COUNT (?cl) as ?freq) ?cl\n"
//                + "WHERE {\n"
//                + " 		{ [] a ?cl }\n"
//                + " 		UNION { graph ?g {[] a ?cl }}\n"
//                + "} \n"
//                + "group by ?cl";

        Query qy = QueryFactory.create(qr);
        QueryExecution qe = QueryExecutionFactory.create(qy, ds);
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution soln = rs.nextSolution();
            String classpartition = String.valueOf(soln.get("class"));
            Literal frequencia = soln.getLiteral("freq");
            int num_frequencia = frequencia.getInt();
            if (classpartition != null || !classpartition.equals("") || !classpartition.equals("null")) {
                frequen = InsertFeaturesBD.VerificaUpdateClass(name_dataset, classpartition, conn);
                if (frequen >= 1) {
                    frequencia_update = frequen + num_frequencia;
                    InsertFeaturesBD.UpdateClass(name_dataset, frequencia_update, classpartition, conn);
                } else {
                    ClassPartition classp = new ClassPartition();
                    classp.setName_dataset(name_dataset);
                    classp.setFeature(classpartition);
                    classp.setFrequen(num_frequencia);
                    classp.setType("dump");
                    if (!classp.getFeature().equals("null")) {
                        InsertFeaturesBD.InsertClass(classp, conn);
                    }

                }
            }

        }
    }

    public static void SearchProprety(Dataset ds, String name_dataset, Connection conn) throws ClassNotFoundException, SQLException {
        double frequen, frequencia_update = 0;
        String qr = "SELECT (COUNT (?p) as ?freq) ?p\n"
                + "WHERE {\n"
                + "  { [] ?p [] }\n"
                + "  UNION { graph ?g {[] ?p [] }}\n"
                + "} \n"
                + "group by ?p";
        Query qy = QueryFactory.create(qr);
        QueryExecution qe = QueryExecutionFactory.create(qy, ds);
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution soln = rs.nextSolution();
            String propretypartition = String.valueOf(soln.get("p"));
            Literal frequencia = soln.getLiteral("freq");
            int num_frequencia = frequencia.getInt();
            frequen = InsertFeaturesBD.VerificaUpdateProprety(name_dataset, propretypartition, conn);
            if (frequen >= 1) {
                frequencia_update = frequen + num_frequencia;
                InsertFeaturesBD.UpdateProprety(name_dataset, frequencia_update, propretypartition, conn);
            } else {
                PropretyPartition pp = new PropretyPartition();
                pp.setName_dataset(name_dataset);
                pp.setFeature(propretypartition);
                pp.setFrequen(num_frequencia);
                pp.setType("dump");
                if (!pp.getFeature().equals("null")) {
                    InsertFeaturesBD.InsertProprety(pp, conn);
                }

            }

        }
    }

    public static void GetNotes(String nome_dataset, Connection conn) throws IOException {
        PropertyConfigurator.configure("./src/main/resources/conf/log4j.properties");
        Config.configure("./src/main/resources/conf/datasetcrawler.properties");
        CatalogCrawler crawler = new CatalogCrawler(Config.CKAN_CATALOG);
        uff.ic.swlab.datasetcrawler.adapter.Dataset dataset = crawler.getDataset(nome_dataset);
        String notes = dataset.getNotes();
        if (notes != null || !notes.equals("")) {
            String notes_url = java.net.URLEncoder.encode(notes);
            br.com.edu.DBPedia.DBpediaWB.getEntite(notes_url, nome_dataset, conn);
        }

    }

    @Override
    public void run() {
        File files_dump[];
        File files_directory[];
        File name_dump = new File(caminho);
        files_dump = name_dump.listFiles();
        for (int j = 0; j < files_dump.length; j++) {
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
                        try {
                            SearchClass(ds, name_dataset, conn);
                            SearchProprety(ds, name_dataset, conn);
                            SearchEntites(ds, name_dataset, conn);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(ReadRDF.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SQLException ex) {
                            Logger.getLogger(ReadRDF.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (Exception ex) {
                            Logger.getLogger(ReadRDF.class.getName()).log(Level.SEVERE, null, ex);
                        }
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
                    try {
                        SearchClass(ds, name_dataset, conn);
                        SearchProprety(ds, name_dataset, conn);
                        SearchEntites(ds, name_dataset, conn);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ReadRDF.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(ReadRDF.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                        Logger.getLogger(ReadRDF.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        try {
            System.out.println("Notes do: " + name_dataset);
            GetNotes(name_dataset, conn);
        } catch (IOException ex) {
            Logger.getLogger(ReadRDF.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
