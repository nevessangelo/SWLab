/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Utils;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.codehaus.plexus.archiver.tar.TarGZipUnArchiver;
import org.rauschig.jarchivelib.ArchiveFormat;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;
import org.rauschig.jarchivelib.Compressor;
import org.rauschig.jarchivelib.CompressorFactory;

/**
 *
 * @author angelo
 */
public class Unzip {

    public static int extract(File archive, String name, String type) throws FileNotFoundException, IOException, RarException, ArchiveException {
        int retorno = 0;
        //teste
        // File archive = new File("/home/angelo/SWLab/GetFeatures/Dumps/acm/EnviarDaniel.rar");
        File destination = new File("/home/angelo/SWLab/GetFeatures/Dumps/" + name);
        //String type = "rar";
        //fim teste

        System.out.println("Extraindo o Dump do Dataset: " + name);
        if (type.equals("tgz") || type.equals("tar.gz")) {
            Archiver archiver = ArchiverFactory.createArchiver("tar", "gz");
            archiver.extract(archive, destination);
            archive.delete();
            return retorno = 1;
        } else if (type.equals("rar")) {
            String out_string = destination.toString();
            Archive a = new Archive(archive);
            //a.getMainHeader().print();
            FileHeader fh = a.nextFileHeader();
            while (fh != null) {
                File out = new File(out_string + "/" + fh.getFileNameString().trim());
                System.out.println(out.getAbsolutePath());
                FileOutputStream os = new FileOutputStream(out);
                a.extractFile(fh, os);
                os.close();
                fh = a.nextFileHeader();
            }
            archive.delete();
            return retorno = 1;
        } else if (type.equals("gz")) {
            File dest = new File(destination + "/extract.nq");
            Compressor compressor = CompressorFactory.createCompressor(archive);
            compressor.decompress(archive, dest);
            archive.delete();
            return retorno = 1;
        } else if (type.equals("zip")) {
            Archiver archiver = ArchiverFactory.createArchiver(ArchiveFormat.ZIP);
            archiver.extract(archive, destination);
            archive.delete();
            return retorno = 1;
        }
        return retorno = 0;
    }

}
