/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.main;

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
public class TestePropretyPartitions {

    public static void main(String[] args) {
        Model model = ModelFactory.createDefaultModel();
        RDFDataMgr.read(model, "/home/angelo/WebSemantica/apache-jena-fuseki/Datasets/TopWikiLak.rdf");
        //model.write(System.out);
        String qr = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "\n"
                + "Select distinct ?p (count(?p) as ?freq)\n"
                + "where {?s ?p ?o\n"
                + "}\n"
                + "group by ?p\n"
                + "order by DESC (?freq)";

        Query qy = QueryFactory.create(qr);
        QueryExecution qe = QueryExecutionFactory.create(qy, model);
        ResultSet rs = qe.execSelect();
           while (rs.hasNext()) {
            QuerySolution soln = rs.nextSolution();
            String propretypartition = String.valueOf(soln.get("p"));
            Literal frequencia = soln.getLiteral("freq");
            int num_frequencia = frequencia.getInt();
            System.out.println(num_frequencia);
        }
    }

}
