/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Insert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import org.apache.jena.query.Dataset;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.tdb.TDBFactory;

/**
 *
 * @author angelo
 */
public class Output {
    
    public static void main(String[] args) throws FileNotFoundException {
        String filename = "/home/angelo/Área de Trabalho/teste/voids_2016-09-23_22-46-03.nq.gz";
        
        new File("/home/angelo/Área de Trabalho/teste/tdb").mkdirs();
        String assemblerFile = "/home/angelo/WebSemantica/apache-jena-fuseki/apache-jena-fuseki-2.4.0/run/configuration/read2.ttl";
        Dataset ds2 = TDBFactory.assembleDataset(assemblerFile);
        RDFDataMgr.read(ds2, filename);
        
         FileOutputStream out = new FileOutputStream("/home/angelo/Área de Trabalho/teste/Dataset.nq");
        
        RDFDataMgr.write(out, ds2, Lang.NQUADS);
        
        ds2.close();
    }
    
}
