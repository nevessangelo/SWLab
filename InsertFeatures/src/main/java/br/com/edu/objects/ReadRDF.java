/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.objects;

import java.io.File;
import org.apache.jena.query.Dataset;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.tdb.TDBFactory;

/**
 *
 * @author angelo
 */
public class ReadRDF {
    
     public static Dataset ReadRdf() {
        String filename = "/home/angelo/Área de Trabalho/teste/void_2016-10-01_11-16-14.nq.gz";
        new File("/home/angelo/Área de Trabalho/teste/tdb").mkdirs();
        String assemblerFile = "/home/angelo/WebSemantica/apache-jena-fuseki/apache-jena-fuseki-2.4.0/run/configuration/readVoid.ttl";
        Dataset ds2 = TDBFactory.assembleDataset(assemblerFile);
        RDFDataMgr.read(ds2, filename);
        return ds2;
    }
    
}
