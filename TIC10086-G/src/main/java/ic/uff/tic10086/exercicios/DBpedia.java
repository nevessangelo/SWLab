package ic.uff.tic10086.exercicios;

import com.opencsv.CSVReader;
import static ic.uff.tic10086.utils.DBpediaSearch.search;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class DBpedia {

    private static final String RDF_DIR = "./src/main/resources/dat/rdf";
    private static final String OWL_DIR = "./src/main/resources/dat/owl";
    public static final Lang EXPORT_LANG = Lang.TTL;

    public static final String FILENAME = "dbpediaData";
    public static final String DATASET_URL = "http://dadosabertos.rio.rj.gov.br/apiCultura/apresentacao/csv/turismoECultura_.csv";

    public static void main(String[] args) {
        try {
            Logger.getRootLogger().setLevel(Level.OFF);
            extractSample();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void extractSample() throws FileNotFoundException, MalformedURLException, IOException {
        Model model = ModelFactory.createDefaultModel();
        int limit = 20, offset = 0;

        URL url = new URL(DATASET_URL);
        try (
                InputStreamReader in = new InputStreamReader(url.openStream(), "Windows-1252");
                BufferedReader buff = new BufferedReader(in);
                CSVReader reader = new CSVReader(buff, ',', '"', 1);) {

            String[] nextLine;
            String uri1, uri2, uri3, name, street, number, neighborhood, telephone, latitude, longitude;
            while ((nextLine = reader.readNext()) != null)
                try {
                    name = nextLine[0];
                    street = nextLine[1];
                    number = nextLine[2];
                    neighborhood = nextLine[3];
                    telephone = nextLine[4];
                    latitude = nextLine[5];
                    longitude = nextLine[6];

                    search(name, limit, offset, model);

                } catch (Exception e) {
                }
        }

        OutputStream out = new FileOutputStream(new File(RDF_DIR + "/" + FILENAME + "." + EXPORT_LANG.getFileExtensions().get(0)));
        RDFDataMgr.write(out, model, EXPORT_LANG);
    }
}
