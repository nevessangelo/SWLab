/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Search;

import br.com.edu.Connection.InsertFeaturesBD;
import br.com.edu.Objects.ClassPartition;
import br.com.edu.Objects.PropretyPartition;
import eu.trentorise.opendata.jackan.CkanClient;
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
import org.apache.jena.riot.Lang;

/**
 *
 * @author angelo
 */
public class SearchDifDump {

    public static Dataset ReadRdf(String arquivo) {
        try {
            Lang[] langs = {null, Lang.TURTLE, Lang.RDFXML, Lang.NTRIPLES, Lang.TRIG,
                Lang.NQUADS, Lang.JSONLD, Lang.RDFJSON, Lang.TRIX, Lang.RDFTHRIFT};
            for (Lang lang : langs) {
                try {
                    //  if (lang == null) {
                    if (lang == null) {
                        Dataset tempDataset = DatasetFactory.create();
                        org.apache.jena.riot.RDFDataMgr.read(tempDataset, arquivo);
                        //RDFDataMgr.write(System.out, tempDataset, Lang.NQ);
                        return tempDataset;
                    } else {
                        Dataset tempDataset = DatasetFactory.create();
                        org.apache.jena.riot.RDFDataMgr.read(tempDataset, arquivo, lang);
                        //RDFDataMgr.write(System.out, tempDataset, lang);
                        return tempDataset;
                    }
                    // }
                } catch (Throwable e) {
                }

            }

        } catch (Throwable e) {
        }
        return DatasetFactory.create();

    }

    public static void SearchClass(Dataset ds, String name_dataset) throws ClassNotFoundException, SQLException {
        int num_frequencia = 0;
        String graph = "<http://linkeddatacatalog.dws.informatik.uni-mannheim.de/api/rest/dataset/" + name_dataset + ">";
        String qr = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX void: <http://rdfs.org/ns/void#>\n"
                + "PREFIX prov: <http://www.w3.org/ns/prov#>\n"
                + "PREFIX foaf:  <http://xmlns.com/foaf/0.1/>\n"
                + "\n"
                + "select ?g ?d2 ?featureType ?feature ?frequency ?datasetSize\n"
                + "from named " + graph
                + "where {\n"
                + "\n"
                + "  {graph ?g {?g a void:Dataset.\n"
                + "             ?d2 void:classPartition ?cp.\n"
                + "             ?cp void:class ?feature.\n"
                + "             optional {?cp void:entities ?frequency}\n"
                + "             optional {?g void:triples ?datasetSize}\n"
                + "bind(\"Class\" as ?featureType)}\n"
                + "  }\n"
                + "\n"
                + "}";
        System.out.println(qr);
        Query query = QueryFactory.create(qr);
        QueryExecution exec = QueryExecutionFactory.create(query, ds);

        ResultSet rs = exec.execSelect();
        while (rs.hasNext()) {
            QuerySolution soln = rs.nextSolution();
            String classpartition = String.valueOf(soln.get("feature"));
            //String frequencia = String.valueOf(soln.get("frequency"));
            Literal frequencia = soln.getLiteral("frequency");
            //int num_frequencia = Integer.parseInt(frequencia);
            if (frequencia == null) {
                num_frequencia = 0;
            } else {
                num_frequencia = frequencia.getInt();
            }
            ClassPartition classp = new ClassPartition();
            classp.setName_dataset(name_dataset);
            classp.setFeature(classpartition);
            classp.setFrequen(num_frequencia);
            classp.setType("sparql");
            InsertFeaturesBD.InsertClass(classp);
            System.out.println(classp.getFeature());

        }

    }

    public static void SearchProprety(Dataset ds, String name_dataset) throws ClassNotFoundException, SQLException {
        int num_frequencia = 0;
        String graph = "<http://linkeddatacatalog.dws.informatik.uni-mannheim.de/api/rest/dataset/" + name_dataset + ">";
        String qr = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX void: <http://rdfs.org/ns/void#>\n"
                + "\n"
                + "SELECT ?d1 ?d2 ?featureType ?feature ?frequency ?datasetSize\n"
                + "from named " + graph
                + "                where {{graph ?d1 {?d1 a void:Dataset."
                + "                ?d2 void:propertyPartition ?pp.\n"
                + "                ?pp void:property ?feature.\n"
                + "                optional {?pp void:triples ?frequency}\n"
                + "                optional {?d1 void:triples ?datasetSize}\n"
                + "                bind(\"Property\" as ?featureType)}}\n"
                + "}";
        Query query = QueryFactory.create(qr);
        QueryExecution exec = QueryExecutionFactory.create(query, ds);

        ResultSet rs = exec.execSelect();
        while (rs.hasNext()) {
            QuerySolution soln = rs.nextSolution();
            String propretypartition = String.valueOf(soln.get("feature"));
            //String frequencia = String.valueOf(soln.get("frequency"));
             //String frequencia = String.valueOf(soln.get("frequency"));
            Literal frequencia = soln.getLiteral("frequency");
            //int num_frequencia = Integer.parseInt(frequencia);
            if (frequencia == null) {
                num_frequencia = 0;
            } else {
                num_frequencia = frequencia.getInt();
            }
            PropretyPartition pp = new PropretyPartition();
            pp.setName_dataset(name_dataset);
            pp.setFeature(propretypartition);
            pp.setFrequen(num_frequencia);
            pp.setType("sparql");
            InsertFeaturesBD.InsertProprety(pp);
            System.out.println(pp.getFeature());

        }

    }

    public static void Search(CkanClient cc, List datasets) throws ClassNotFoundException, SQLException {
        String arquivo = System.getProperty("user.dir") + "/Void/void.gz";
        Dataset ds2 = ReadRdf(arquivo);
        for (int i = 0; i < datasets.size(); i++) {
            String nome_dataset = datasets.get(i).toString();
            int bool1 = InsertFeaturesBD.VerificaClass(nome_dataset);
            int bool2 = InsertFeaturesBD.VerificaProprety(nome_dataset);
            if (bool1 == 0 && bool2 == 0) {
                SearchClass(ds2, nome_dataset);
                SearchProprety(ds2, nome_dataset);

            }

        }

    }

}
