/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package getfeatures;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import static org.apache.commons.io.FileUtils.getFile;
import org.rauschig.jarchivelib.IOUtils;

/**
 *
 * @author angelo
 */
public class TesteZip {

    public static void main(String[] args) throws FileNotFoundException, ArchiveException, IOException {
        //extrair zip//
//        final File input = getFile("/home/angelo/Área de Trabalho/Teste/CC195814-83A4-386F-509A-37E2F35A204B.rdf.zip");
//        final InputStream is = new FileInputStream(input);
//        final ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream("zip", is);
//        final ZipArchiveEntry entry = (ZipArchiveEntry) in.getNextEntry();
//        final OutputStream out = new FileOutputStream(new File("/home/angelo/Área de Trabalho/Teste/", entry.getName()));
//        IOUtils.copy(in, out);
//        out.close();
//        in.close();
        
        //extrair tgz já tem
        
        
        
        //extrair gz
    
//    InputStream is = new FileInputStream("/home/angelo/Área de Trabalho/Teste/pokedex-data-rdf.gz");
//    BufferedInputStream in = new BufferedInputStream(is);
//    FileOutputStream out = new FileOutputStream("/home/angelo/Área de Trabalho/Teste/export");
//    GzipCompressorInputStream gzIn = new GzipCompressorInputStream(in);
//    final byte[] buffer = new byte[1000];
//    int n = 0;
//    while (-1 != (n = gzIn.read(buffer))) {
//        out.write(buffer, 0, n);
//    }
//    out.close();
//    gzIn.close();
//        
        
        
        

    }

}
