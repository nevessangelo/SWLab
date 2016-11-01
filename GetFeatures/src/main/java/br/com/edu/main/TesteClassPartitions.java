/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.main;

import java.net.URLEncoder;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;

/**
 *
 * @author angelo
 */
public class TesteClassPartitions {

    public static void main(String[] args) {
//        Model model = ModelFactory.createDefaultModel();
//        RDFDataMgr.read(model, "/home/angelo/WebSemantica/apache-jena-fuseki/Datasets/TopWikiLak.rdf");
//        //model.write(System.out);
//        String qr = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
//                + "SELECT  ?class (count(?subject) as ?freq)\n"
//                + "	 WHERE{ \n"
//                + "  		   ?subject rdf:type ?class }\n"
//                + "	  group by ?class\n"
//                + "      limit 30";
//        
//        Query qy = QueryFactory.create(qr);
//        QueryExecution qe = QueryExecutionFactory.create(qy, model);
//        ResultSet rs = qe.execSelect();
//        while (rs.hasNext()) {
//            QuerySolution soln = rs.nextSolution();
//            String classpartition = String.valueOf(soln.get("class"));
//            Literal frequencia = soln.getLiteral("freq");
//            int num_frequencia = frequencia.getInt();
//            //System.out.println(num_frequencia);
//
//        }
//    }
        String teste = "ESTIMATED RESIDENT POPULATION - Persons - 75 years to 79 years (no.)@fr";
        URLEncoder.encode(teste);
        //URLEncoder.encode(text.text()
        System.out.println(teste);
    }
}
