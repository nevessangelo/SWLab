/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package getfeatures;

import br.com.edu.readrdf.ReadRDF;

/**
 *
 * @author angelo
 */
public class Teste2 {
    
    public static void main(String[] args) throws Exception {
        ReadRDF read = new ReadRDF();
        read.Read("/home/angelo/SWLab/GetFeatures/Dumps/data-incubator-pokedex/pokedex-data-rdf");
    }
    
}
