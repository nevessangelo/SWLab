/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.main;

import br.com.edu.rdf.SPARQL;
import java.sql.SQLException;

/**
 *
 * @author angelo
 */
public class Main2 {
    
    public static void main(String[] args) throws ClassNotFoundException, SQLException, InterruptedException {
        SPARQL.getsparql();
        System.out.println("Fim da extração das Propretys e Class do SPARQL endpoint");
    }
    
}
