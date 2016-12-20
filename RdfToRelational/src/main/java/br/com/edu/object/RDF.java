/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.object;

import java.io.File;
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
import br.com.edu.Connection.InsertBD;
import java.sql.SQLException;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.riot.Lang;

/**
 *
 * @author angelo
 */
public class RDF {

//    public static Dataset ReadRdf() {
//        String filename = "/home/angelo/WebSemantica/apache-jena-fuseki/apache-jena-fuseki-2.4.0/run/backups/readVoid_2016-10-27_18-35-10.nq.gz";
//        new File("/home/angelo/Área de Trabalho/teste/tdb").mkdirs();
//        String assemblerFile = "/home/angelo/WebSemantica/apache-jena-fuseki/apache-jena-fuseki-2.4.0/run/configuration/readFeatures.ttl";
//        Dataset ds2 = TDBFactory.assembleDataset(assemblerFile);
//        RDFDataMgr.read(ds2, filename);
//        return ds2;
//    }
    
    public static Dataset Read(File files_dump) {
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


    public static void InserdBD() throws ClassNotFoundException, SQLException {
        int num_frequencia, num_datset;
        String arquivo = System.getProperty("user.dir") + "/Void/void_completo.gz";
        File file_arquivo = new File(arquivo);
        Dataset ds2 = Read(file_arquivo);
        String qr = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX void: <http://rdfs.org/ns/void#>\n"
                + "PREFIX prov: <http://www.w3.org/ns/prov#>\n"
                + "PREFIX foaf:  <http://xmlns.com/foaf/0.1/>\n"
                + "\n"
                + "select ?d1 ?d2 ?featureType ?feature ?frequency ?datasetSize\n"
                + "where {{graph ?d1 {?d2 void:subset ?ls.\n"
                + "?ls void:objectsTarget ?feature.\n"
                + "optional {?ls void:triples ?frequency}\n"
                + "optional {?d1 void:triples ?datasetSize}\n"
                + "bind(\"Linkset\" as ?featureType)}}\n"
                + "\n"
                + "union {graph ?d1 {?d2 void:subset ?ls.\n"
                + "?ls void:target ?feature.\n"
                + "optional {?ls void:triples ?frequency}\n"
                + "optional {?d1 void:triples ?datasetSize}\n"
                + "bind(\"Linkset\" as ?featureType)\n"
                + "\n"
                + "filter (not exists {?feature void:subset ?ls})}}\n"
                + "union {graph ?d1 {?d2 void:classPartition ?cp.\n"
                + "?cp void:class ?feature.\n"
                + "optional {?cp void:entities ?frequency}\n"
                + "optional {?d1 void:triples ?datasetSize}\n"
                + "bind(\"Class\" as ?featureType)}}\n"
                + "\n"
                + "union {graph ?d1 {?d2 void:propertyPartition ?pp.\n"
                + "?pp void:property ?feature.\n"
                + "optional {?pp void:triples ?frequency}\n"
                + "optional {?d1 void:triples ?datasetSize}\n"
                + "bind(\"Property\" as ?featureType)}}\n"
                + " \n"
                + "union {graph ?d1 {?d2 foaf:topic ?object.\n"
                + "      ?object prov:pairKey ?feature.\n"
                + "      ?object prov:pairValue ?frequency\n"
                + "      optional {?d1 void:triples ?datasetSize}\n"
                + "      bind(\"Entites\" as ?featureType)	}}\n"
                + "}";

        Query query = QueryFactory.create(qr);
        QueryExecution exec = QueryExecutionFactory.create(query, ds2);

        ResultSet rs = exec.execSelect();
        while (rs.hasNext()) {
            Features features = new Features();
            QuerySolution soln = rs.nextSolution();
            String name_dataset = String.valueOf(soln.get("d1"));
            String feature = String.valueOf(soln.get("feature"));
            String type_feature = String.valueOf(soln.get("featureType"));
            Literal frequencia = soln.getLiteral("frequency");
            Literal dataset_size = soln.getLiteral("datasetSize");
            if (frequencia == null) {
                num_frequencia = 0;
            } else {
                num_frequencia = frequencia.getInt();
            }
            if (dataset_size == null) {
                num_datset = 0;
            } else {
                num_datset = dataset_size.getInt();
            }
//            System.out.println(name_dataset);
//            System.out.println(feature);
//            System.out.println(type_feature);
//            System.out.println(num_frequencia);
            features.setName_dataset(name_dataset);
            features.setFeature(feature);
            features.setType_feature(type_feature);
            features.setFrequen(num_frequencia);
            features.setDatasetSize(num_datset);
            InsertBD.InsertEntites(features);

        }

    }

}
