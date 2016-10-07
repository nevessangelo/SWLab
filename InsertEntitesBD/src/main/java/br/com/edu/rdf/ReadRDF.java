/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.rdf;

import br.com.edu.Connection.InsertFeaturesBD;
import br.com.edu.DBPedia.DBPediaSpotlight;
import br.com.edu.object.ClassPartition;
import br.com.edu.object.Entites;
import br.com.edu.object.PropretyPartition;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.tdb.TDBFactory;
import sun.font.TrueTypeFont;

/**
 *
 * @author angelo
 */
public class ReadRDF {

    public static void SearchEntites(Dataset ds, String name_dataset) throws Exception {
        int frequencia = 0;
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
            if (object.length() >= 100) {
                List<String> list_entites = DBPediaSpotlight.getEntity(object);
                if (list_entites != null) {
                    for (int i = 0; i < list_entites.size(); i++) {
                        frequencia = InsertFeaturesBD.UpdateFrequencia(name_dataset, list_entites.get(i));
                        if (frequencia >= 1) {
                            InsertFeaturesBD.Update(name_dataset, frequencia, list_entites.get(i));
                        } else {
                            frequencia = 1;
                            Entites entites = new Entites();
                            entites.setName_dataset(name_dataset);
                            entites.setFeature(list_entites.get(i));
                            entites.setFrequen(1);
                            entites.setType("Dump");
                            InsertFeaturesBD.InsertEntites(entites);
                        }

                    }
                }
            }

        }

    }

    public static void SearchProprety(Dataset ds, String name_dataset) throws ClassNotFoundException, SQLException {
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
            if (propretypartition != null) {
                PropretyPartition pp = new PropretyPartition();
                pp.setName_dataset(name_dataset);
                pp.setFeature(propretypartition);
                pp.setFrequen(num_frequencia);
                pp.setType("dump");
                InsertFeaturesBD.InsertProprety(pp);
            }

        }
    }

    public static void SearchClass(Dataset ds, String name_dataset) throws ClassNotFoundException, SQLException {
        String qr = "SELECT (COUNT (?cl) as ?freq) ?cl\n"
                + "WHERE {\n"
                + " 		{ [] a ?cl }\n"
                + " 		UNION { graph ?g {[] a ?cl }}\n"
                + "} \n"
                + "group by ?cl";

        Query qy = QueryFactory.create(qr);
        QueryExecution qe = QueryExecutionFactory.create(qy, ds);
        ResultSet rs = qe.execSelect();
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
                classp.setType("dump");
                InsertFeaturesBD.InsertClass(classp);
                //System.out.println(classp.getFeature());
            }

        }
    }

    public static Dataset read(File files_dump) {
        try {
            Lang[] langs = {null, Lang.TURTLE, Lang.RDFXML, Lang.NTRIPLES, Lang.TRIG,
                Lang.NQUADS, Lang.JSONLD, Lang.RDFJSON, Lang.TRIX, Lang.RDFTHRIFT};
            for (Lang lang : langs) {
                try {
                    if (lang == null) {
                        if (lang == null) {
                            Dataset tempDataset = DatasetFactory.create();
                            org.apache.jena.riot.RDFDataMgr.read(tempDataset, files_dump.toString());
                            //RDFDataMgr.write(System.out, tempDataset, Lang.NQ);
                            return tempDataset;
                        } else {
                            Dataset tempDataset = DatasetFactory.create();
                            org.apache.jena.riot.RDFDataMgr.read(tempDataset, files_dump.toString(), lang);
                            //RDFDataMgr.write(System.out, tempDataset, lang);
                            return tempDataset;
                        }
                    }
                } catch (Throwable e) {
                }

            }

        } catch (Throwable e) {
        }
        return DatasetFactory.create();
    }

    public static void SelectRDF(String path) throws Exception {
        File file = new File(path);
        File files[];
        File files_dump[];
        files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            String[] getNameDataset = files[i].toString().split("/");
            int size = getNameDataset.length - 1;
            String name_dataset = getNameDataset[size];
            File name_dump = new File(files[i].toString());
            files_dump = name_dump.listFiles();
            for (int j = 0; j < files_dump.length; j++) {
                //System.out.println("DATASET: " + files[i] + "DO DUMP" + files_dump[j]);
                Dataset ds = ReadRDF.read(files_dump[j]);
                SearchClass(ds, name_dataset);
                SearchProprety(ds, name_dataset);
                SearchEntites(ds, name_dataset);
            }
        }
    }

}
