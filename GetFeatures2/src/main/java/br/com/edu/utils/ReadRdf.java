/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.utils;

import br.com.edu.Connection.InsertFeaturesBD;
import br.com.edu.DBPedia.DBPediaSpotlight;
import static br.com.edu.DBPedia.DBPediaSpotlight.getCategories;
import br.com.edu.Objects.ClassPartition;
import br.com.edu.Objects.Entites;
import br.com.edu.Objects.PropretyPartition;
import br.com.edu.Objects.Types;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
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
import org.apache.jena.riot.Lang;

/**
 *
 * @author angelo
 */
public class ReadRdf {

    public static void SearchTypes(String name_dataset, List<String> list_entites) throws Exception {
        double frequencia_types = 0;
        double frequencia_types_update = 0;
        double frequencia = 0;
        for (int i = 0; i < list_entites.size(); i++) {
            String result = getCategories(list_entites.get(i));
            if (result != null) {
                ArrayList<String> l_categories = new ArrayList();
                String armazenar;
                String category[] = result.split(",");
                int cont = 1;
                //armazenar = "Thing/" + cont;
                //l_categories.add(armazenar);
                for (Object array_category : category) {
                    if (array_category.toString().contains("DBpedia")) {
                        armazenar = array_category + "/" + cont;
                        l_categories.add(armazenar);
                        cont++;

                    }
                }
                for (Object teste : l_categories) {
                    frequencia_types = InsertFeaturesBD.VerificaUpdateTypes(name_dataset, (String) teste);
                    System.out.println("oi" + " " + frequencia_types);
                    if (frequencia_types == 0) {
                        frequencia = InsertFeaturesBD.UpdateFrequencia(name_dataset, list_entites.get(i));
                        Types types = new Types();
                        types.setName_dataset(name_dataset);
                        types.setFeature((String) teste);
                        types.setFrequen(frequencia);
                        types.setType("Dump");
                        InsertFeaturesBD.InsertTypes(types);
                    } else {
                        double frequen_total = 0;
                        frequencia_types_update = InsertFeaturesBD.GetFrequenType(name_dataset, (String) teste);
                        frequen_total = frequencia_types_update + frequencia_types;
                        InsertFeaturesBD.UpdateTypes(name_dataset, frequen_total, (String) teste);
                    }

                }
            }
        }

    }

    public static void SearchEntites(Dataset ds, String name_dataset) throws Exception {
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
            if (object.length() >= 500) {
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

                    SearchTypes(name_dataset, list_entites);
                }
            }

        }

    }

    public static void SearchProprety(Dataset ds, String name_dataset) throws ClassNotFoundException, SQLException {
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
            frequen = InsertFeaturesBD.VerificaUpdateProprety(name_dataset, propretypartition);
            if (frequen >= 1) {
                System.out.println("UPDATE Proprety do " + name_dataset);
                frequencia_update = frequen + num_frequencia;
                InsertFeaturesBD.UpdateProprety(name_dataset, frequencia_update, propretypartition);
            } else {
                PropretyPartition pp = new PropretyPartition();
                pp.setName_dataset(name_dataset);
                pp.setFeature(propretypartition);
                pp.setFrequen(num_frequencia);
                pp.setType("dump");
                InsertFeaturesBD.InsertProprety(pp);
                System.out.println(pp.getFeature());
            }

        }
    }

    public static void SearchClass(Dataset ds, String name_dataset) throws ClassNotFoundException, SQLException {
        double frequen, frequencia_update = 0;
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
            if (classpartition != null || !classpartition.equals("") || !classpartition.equals("null")) {
                frequen = InsertFeaturesBD.VerificaUpdateClass(name_dataset, classpartition);
                if (frequen >= 1) {
                    System.out.println("UPDATE Class do " + name_dataset);
                    frequencia_update = frequen + num_frequencia;
                    InsertFeaturesBD.UpdateClass(name_dataset, frequencia_update, classpartition);
                } else {
                    ClassPartition classp = new ClassPartition();
                    classp.setName_dataset(name_dataset);
                    classp.setFeature(classpartition);
                    classp.setFrequen(num_frequencia);
                    classp.setType("dump");
                    InsertFeaturesBD.InsertClass(classp);
                    System.out.println(classp.getFeature());
                }
            }

        }
    }

    public static Dataset read(File files_dump, String name_dataset) {
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

    public static void Read() throws ClassNotFoundException, SQLException, Exception {
        //File diretorio = new File(System.getProperty("user.dir") + "/Dumps/");
        File file = new File("/media/angelo/DATA/Dumps/");
        //File file = new File("/media/angelo/DATA/Teste/");
        File files[];
        File files_dump[];
        File files_directory[];
        files = file.listFiles();
        for (int i = 19; i < files.length; i++) {
            System.out.println(i);
            System.out.println("Lendo os datasets da base: " + files[i].toString());
            String[] getNameDataset = files[i].toString().split("/");
            int size = getNameDataset.length - 1;
            String name_dataset = getNameDataset[size];
            int bool1 = InsertFeaturesBD.VerificaClass(name_dataset);
            int bool2 = InsertFeaturesBD.VerificaProprety(name_dataset);
            if (bool1 == 0 && bool2 == 0) {
                File name_dump = new File(files[i].toString());
                files_dump = name_dump.listFiles();
                for (int j = 0; j < files_dump.length; j++) {
                    if (files_dump[j].isDirectory()) {
                        System.out.println(i);
                        String path_diretory = files_dump[j].toString();
                        File file_directory = new File(path_diretory);
                        files_directory = file_directory.listFiles();
                        for (int k = 0; k < files_directory.length; k++) {
                            System.out.println(files_directory[k]);
                            Dataset ds = ReadRdf.read(files_directory[k], name_dataset);
                            if (ds != null) {
                                SearchClass(ds, name_dataset);
                                SearchProprety(ds, name_dataset);
                                SearchEntites(ds, name_dataset);
                            }

                        }
                    } else {
                        Dataset ds = ReadRdf.read(files_dump[j], name_dataset);
                        if (ds != null) {
                            SearchClass(ds, name_dataset);
                            SearchProprety(ds, name_dataset);
                            SearchEntites(ds, name_dataset);
                        }

                    }
                }
            } else {
                System.out.println("Já tem no banco de dados o dataset: " + name_dataset);
            }
        }
    }

}
