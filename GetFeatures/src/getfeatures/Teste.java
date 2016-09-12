/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package getfeatures;

import br.com.edu.readrdf.ReadRDF;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author angelo
 */
public class Teste {

    public static void main(String[] args) throws Exception {
        // GZip input and output file.
        //
        String sourceFile = "/home/angelo/SWLab/GetFeatures/Dumps/data-incubator-pokedex/pokedex-data-rdf.gz";
        String targetFile = "/home/angelo/SWLab/GetFeatures/Dumps/data-incubator-pokedex/export.rdf";

        try {
            //
            // Create a file input stream to read the source file.
            //
            FileInputStream fis = new FileInputStream(sourceFile);

            //
            // Create a gzip input stream to decompress the source
            // file defined by the file input stream.
            //
            GZIPInputStream gzis = new GZIPInputStream(fis);

            //
            // Create a buffer and temporary variable used during the
            // file decompress process.
            //
            byte[] buffer = new byte[1024];
            int length;

            //
            // Create file output stream where the decompress result
            // will be stored.
            //
            FileOutputStream fos = new FileOutputStream(targetFile);

            //
            // Read from the compressed source file and write the
            // decompress file.
            //
            while ((length = gzis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }

            //
            // Close the resources.
            //
            fos.close();
            gzis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        ReadRDF read = new ReadRDF();
        read.Read("/home/angelo/SWLab/GetFeatures/Dumps/data-incubator-pokedex/");
        
        
    }

}
