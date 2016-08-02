package ic.uff.tic10086.exercicios;

import com.opencsv.CSVReader;
import ic.uff.tic10086.utils.DBpediaSearch;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.tdb.TDBFactory;

public class DBpedia extends MyDataset {

    public static final String SEEDS_URL = "http://dadosabertos.rio.rj.gov.br/apiCultura/apresentacao/csv/turismoECultura_.csv";
    public static final String FILENAME = "dbpediaData";
    public static final String TDB_ASSEMPLER_FILE = "./src/main/resources/conf/dbpedia.ttl";
    public static final String FUSEKI_UPDATE_URL = "http://localhost:3030/dbpedia/update";
    public static final String FUSEKI_DATA_URL = "http://localhost:3030/dbpedia/data";

    public static void main(String[] args) {
        try {
            init();

            Dataset dataset = TDBFactory.assembleDataset(TDB_ASSEMPLER_FILE);
            extractResources(dataset);
            exportResources(dataset);
            dataset.close();

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

    private static void exportResources(Dataset dataset) throws FileNotFoundException {
        dataset.begin(ReadWrite.READ);

        Model model = dataset.getDefaultModel();
        OutputStream out = new FileOutputStream(new File(RDF_DIR + "/" + FILENAME + "." + EXPORT_LANG.getFileExtensions().get(0)));
        RDFDataMgr.write(out, model, EXPORT_LANG);
        DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(FUSEKI_DATA_URL);
        accessor.putModel(model);

        dataset.end();
    }
}
