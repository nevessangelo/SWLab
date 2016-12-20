/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.objects;

import java.io.File;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.tdb.TDBFactory;

/**
 *
 * @author angelo
 */
public class ReadRDF {

    public static Dataset Read(File files_dump) {
        Dataset tempDataset = DatasetFactory.create();
        try {
            Lang[] langs = {null, Lang.TURTLE, Lang.RDFXML, Lang.NTRIPLES, Lang.TRIG,
                Lang.NQUADS, Lang.JSONLD, Lang.RDFJSON, Lang.TRIX, Lang.RDFTHRIFT};
            for (Lang lang : langs) {
                try {
                    if (lang == null) {
                        // Dataset tempDataset = DatasetFactory.create();
                        org.apache.jena.riot.RDFDataMgr.read(tempDataset, files_dump.toString());
                        //RDFDataMgr.write(System.out, tempDataset, Lang.NQ);
                        return tempDataset;
                    } else {
                        // Dataset tempDataset = DatasetFactory.create();
                        org.apache.jena.riot.RDFDataMgr.read(tempDataset, files_dump.toString(), lang);
                        //RDFDataMgr.write(System.out, tempDataset, lang);
                        return tempDataset;
                    }
                } catch (Throwable e) {
                    continue;

                }

            }

        } catch (Throwable e) {
            System.out.println("Error ao ler");
            return tempDataset;

        }
        return DatasetFactory.create();
    }

}
