/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.codehaus.plexus.archiver.tar.TarGZipUnArchiver;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;

/**
 *
 * @author angelo
 */
public class Unzip {

    public static void extract() throws FileNotFoundException, IOException {
        //teste
        File archive = new File("/home/angelo/SWLab/GetFeatures/Dumps/acm/models.zip");
        File destination = new File("/home/angelo/SWLab/GetFeatures/Dumps/acm/");
        String type = "rar";
        String nome = "acm";
        //fim teste

        if (type.equals("tgz")) {
            Archiver archiver = ArchiverFactory.createArchiver("tar", "gz");
            archiver.extract(archive, destination);
        } else if (type.equals("tar.gz")) {
            TarGZipUnArchiver ua = new TarGZipUnArchiver();
            ua.setSourceFile(archive);
            ua.setDestDirectory(destination);
            ua.extract();
        } else if (type.equals("rar")) {
        }
    }

}
