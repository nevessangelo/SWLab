/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.main;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Iterator;
import static org.apache.jena.assembler.JA.FileManager;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.tdb.TDBFactory;

/**
 *
 * @author angelo
 */
public class TesteLiteral {

    public static void main(String[] args) {
        Model model = ModelFactory.createDefaultModel();
        RDFDataMgr.read(model, "/home/angelo/Área de Trabalho/aemet/201105212110_datos.ttl");
        //model.write(System.out);
        String qr = "SELECT  ?object\n"
                + "	 WHERE { ?subject ?predicate ?object .\n"
                + "  	    	 FILTER isLiteral(?object)  \n"
                + "  \n"
                + "} ORDER BY asc(?object)";
        Query qy = QueryFactory.create(qr);
        QueryExecution qe = QueryExecutionFactory.create(qy, model);
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution soln = rs.nextSolution();
            String literal = String.valueOf(soln.get("object"));
            System.out.println(literal);
            if(literal.length() > 100){
                System.out.println(literal);
            }
            
        }
        //ResultSetFormatter.out(System.out, rs);
        

//        
//        String directory = "/home/angelo/Área de Trabalho/models/acm-proceedings.rdf";
//        Model model = ModelFactory.createDefaultModel();        
//        model.read(directory,"RDF/XML-ABBREV");
//        model.write(System.out);
    }

}
