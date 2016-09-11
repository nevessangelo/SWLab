/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.getFeaturesDump;

import br.com.edu.readrdf.ReadRDF;
import br.com.edu.utils.DownloadDump;
import br.com.edu.utils.Unzip;
import com.mongodb.BasicDBList;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author angelo
 */
public class GetFeaturesDatabase {

    public static void verificarExisteDump(String url, String nome_dataset) throws IOException, Exception {
        String[] verifica_dump = url.split("/");
        String DirReadRdf;
        for (int i = 0; i < verifica_dump.length; i++) {
            if (verifica_dump[i].equals("dump.tgz")) {
                File diretorio = new File(System.getProperty("user.dir")+"/Dumps/"+nome_dataset);
                diretorio.mkdir();
                String caminho = diretorio.toString();
                DownloadDump download = new DownloadDump();
                download.gravaArquivoDeURL(url,caminho);
                String arquivo_extrair = diretorio.toString()+"/"+verifica_dump[i];               
                Unzip unzip = new Unzip();
                DirReadRdf = unzip.extract(arquivo_extrair,caminho,url);
                ReadRDF rdf = new ReadRDF();
                rdf.Read(DirReadRdf);                
            }
        }

    }

    public static void main(String[] args) throws Exception {
        List<Object> names_dump = new BasicDBList();
        Boolean resultado;
        try (MongoClient mongo = new MongoClient("localhost", 27017)) {

            MongoDatabase db = mongo.getDatabase("data_catalog");
            MongoCollection<Document> datasets = db.getCollection("datasets");
            List<Document> cursor = (List<Document>) datasets.find(new Document("name", "rkb-explorer-acm")).into(new ArrayList<Document>());
            for (Document cursors : cursor) {
                String nome_dataset = cursors.getString("name");
                List<Document> resources = (List<Document>) cursors.get("resources");
                for (Document resource : resources) {
                    String name = resource.getString("name");
                    String name_minuscula = name.toLowerCase();
                    String[] names = name_minuscula.split(" ");
                    for (int i = 0; i < names.length; i++) {
                        if (names[i].equals("download")) {
                            String url = resource.getString("url");
                            verificarExisteDump(url,nome_dataset);
                        }
                    }
                }
            }
        }
        System.out.println("fim");
    }
}
