/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.getFeaturesDump;

import br.com.edu.dbpediaspotlight.DBPediaSpotlight;
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
        int tamanho = tamanho = verifica_dump.length;
        String DirReadRdf;
        File diretorio = new File(System.getProperty("user.dir") + "/Dumps/" + nome_dataset);
        diretorio.mkdir();
        String verifica_zip = verifica_dump[tamanho - 1];
        String[] achar_zip = verifica_zip.split("\\.");
        for (int j = 0; j < achar_zip.length; j++) {
            if (achar_zip[j].equals("tgz") || achar_zip[j].equals("gz")) {
                String caminho = diretorio.toString();
                DownloadDump download = new DownloadDump();
                download.gravaArquivoDeURL(url, caminho);
                String arquivo_extrair = diretorio.toString() + "/" + verifica_dump[tamanho - 1];
                Unzip unzip = new Unzip();
                DirReadRdf = unzip.extract(arquivo_extrair, caminho, url, achar_zip[j]);
             //   ReadRDF rdf = new ReadRDF();
             //   rdf.Read(DirReadRdf);
            }
        }
    }

    public static void extract() throws Exception {
        List<Object> names_dump = new BasicDBList();
        Boolean resultado;
        try (MongoClient mongo = new MongoClient("localhost", 27017)) {

            MongoDatabase db = mongo.getDatabase("data_catalog");
            MongoCollection<Document> datasets = db.getCollection("datasets");
            List<Document> cursor = (List<Document>) datasets.find(new Document("name", "data-incubator-pokedex")).into(new ArrayList<Document>());
            for (Document cursors : cursor) {
                String nome_dataset = cursors.getString("name");
                List<Document> resources = (List<Document>) cursors.get("resources");
                for (Document resource : resources) {
                    String name = resource.getString("name");
                    if (name != null) {
                        String name_minuscula = name.toLowerCase();
                        String[] names = name_minuscula.split(" ");
                        for (int i = 0; i < names.length; i++) {
                            if (names[i].equals("download") || names[i].equals("export")) {
                                String url = resource.getString("url");
                                verificarExisteDump(url, nome_dataset);
                            }
                        }

                    }

                }
                Document organization = (Document) cursors.get("organization");
                if (organization != null) {
                    String descricao = organization.getString("description");
                    DBPediaSpotlight dbpedia = new DBPediaSpotlight();
                    dbpedia.getEntity(descricao);
                    //System.out.println(descricao);
                } else {
                    String notes = cursors.getString("notes");
                    DBPediaSpotlight dbpedia = new DBPediaSpotlight();
                    dbpedia.getEntity(notes);
                    //System.out.println(notes);

                }

            }

        }
        System.out.println("Fim da extração dos Dumps");
    }
}
