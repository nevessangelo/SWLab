package ic.uff.tic10086.exercicios;

import com.opencsv.CSVReader;
import ic.uff.tic10086.utils.DBpediaSearch;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;

public class DBpedia extends MyDataset {

    public static final String SEEDS_URL = "http://dadosabertos.rio.rj.gov.br/apiCultura/apresentacao/csv/turismoECultura_.csv";

    public static void main(String[] args) {
        try {
            init();

            FILENAME = "dbpediaData";
            DEFAULT_NS = "http://" + DOMAIN + DEREF_PORT + "/resource/";
            TDB_ASSEMBLER_FILE = CONF_DIR + "/dbpedia.ttl";
            FUSEKI_UPDATE_URL = "http://" + DOMAIN + SPARQL_PORT + "/fuseki/dbpedia.temp/update";
            FUSEKI_DATA_URL = "http://" + DOMAIN + SPARQL_PORT + "/fuseki/dbpedia.temp/data";

            Dataset dataset = TDBFactory.assembleDataset(TDB_ASSEMBLER_FILE);
            try {
                extractResources(dataset);
                exportResources(dataset);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dataset.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void extractResources(Dataset dataset) throws IOException, MalformedURLException {
        dataset.begin(ReadWrite.WRITE);
        Model model = dataset.getDefaultModel();
        model.getNsPrefixMap().clear();
        model.removeAll();
        URL url = new URL(SEEDS_URL);
        try (
                InputStreamReader in = new InputStreamReader(url.openStream(), "Windows-1252");
                BufferedReader buff = new BufferedReader(in);
                CSVReader reader = new CSVReader(buff, ',', '"', 1);) {

            String name;
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null)
                try {
                    name = nextLine[0];
                    if (name != null && !name.equals(""))
                        DBpediaSearch.search(name, 7, 0, model);
                } catch (Exception e) {
                    System.out.println("Error reding CSV.");
                }
        }
        dataset.commit();
        dataset.end();
    }
}
