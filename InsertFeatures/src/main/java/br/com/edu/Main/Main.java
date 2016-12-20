/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Main;

import br.com.edu.Insert.InsertFeatures;
import br.com.edu.objects.ReadRDF;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import org.apache.jena.query.Dataset;

/**
 *
 * @author angelo
 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, SQLException {
        String arquivo = System.getProperty("user.dir") + "/Void/void_completo.gz";
        File file_arquivo = new File(arquivo);
        Dataset ds2 = ReadRDF.Read(file_arquivo);
        InsertFeatures.InsertClassPartition(ds2);
        //InsertFeatures.InsertPropretyPartition(ds2);
        InsertFeatures.InsertEntites(ds2);
    }
    
}
