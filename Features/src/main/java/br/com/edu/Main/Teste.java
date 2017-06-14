/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Main;

import br.com.edu.Connection.ConnectionMySql;
import br.com.edu.Connection.InsertFeaturesBD;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import uff.ic.swlab.datasetcrawler.CatalogCrawler;
import uff.ic.swlab.datasetcrawler.adapter.Dataset;

/**
 *
 * @author angelo
 */
public class Teste {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        int conter = 0;
        File diretorio = new File(System.getProperty("user.dir") + "/Dumps/");
        CatalogCrawler crawler = new CatalogCrawler("http://linkeddatacatalog.dws.informatik.uni-mannheim.de");
        File files[];
        files = diretorio.listFiles();
        Connection conn = ConnectionMySql.Conectar();
        for (int i = 0; i < files.length; i++) {
            String[] getNameDataset = files[i].toString().split("/");
            int size = getNameDataset.length - 1;
            String name_dataset = getNameDataset[size];
            //System.out.println(name_dataset);
            try {
                Dataset dataset = crawler.getDataset(name_dataset);
                //System.out.println(dataset.getName());
                if (dataset.getName() != null) {
                    int verifica_class = InsertFeaturesBD.VerificaClass(dataset.getName(), conn);
                    int verifica_proprety = InsertFeaturesBD.VerificaProprety(dataset.getName(), conn);
                    int verifica_entites = InsertFeaturesBD.VerificaEntites(dataset.getName(), conn);
                    if (verifica_class == 0 && verifica_proprety == 0 && verifica_entites == 0) {
                        System.out.println(dataset.getName());
                        conter = conter + 1;
                    }

                }
            } catch (Throwable e) {
                continue;
            }
            conn.close();

        }
        System.out.println(conter);
    }

}
