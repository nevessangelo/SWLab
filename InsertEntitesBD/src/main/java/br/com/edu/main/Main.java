/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.main;

import br.com.edu.rdf.ReadRDF;

/**
 *
 * @author angelo
 */
public class Main {
    
    public static void main(String[] args) throws Exception {
        String path = "/media/angelo/DATA/Dumps/";
        ReadRDF.SelectRDF(path);
        System.out.println("Fim do armazenamento das Entidades, Class e Propreprty dos Dumps");
        
    }
    
}
