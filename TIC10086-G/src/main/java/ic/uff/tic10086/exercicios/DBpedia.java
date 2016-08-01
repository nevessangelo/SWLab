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
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.tdb.TDBFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class DBpedia {

    private static final String RDF_DIR = "./src/main/resources/dat/rdf";
    private static final String OWL_DIR = "./src/main/resources/dat/owl";
    public static final Lang EXPORT_LANG = Lang.TTL;

    public static final String FILENAME = "dbpediaData";
    public static final String SEEDS_URL = "http://dadosabertos.rio.rj.gov.br/apiCultura/apresentacao/csv/turismoECultura_.csv";
    public static final String UPDATE_URL = "http://localhost:3030/dbpediaData/update";
    public static final String DATA_URL = "http://localhost:3030/dbpediaData/data";

    public static void main(String[] args) {
        try {
            Logger.getRootLogger().setLevel(Level.OFF);
            extractSample();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void extractSample() throws FileNotFoundException, MalformedURLException, IOException {
        String assemblerFile = "./src/main/resources/conf/dbpedia.ttl";
        Dataset dataset = TDBFactory.assembleDataset(assemblerFile);
        Model model = dataset.getDefaultModel();
        model.getNsPrefixMap().clear();
        model.removeAll();
        int limit = 20, offset = 0;

        URL url = new URL(SEEDS_URL);
        try (
                InputStreamReader in = new InputStreamReader(url.openStream(), "Windows-1252");
                BufferedReader buff = new BufferedReader(in);
                CSVReader reader = new CSVReader(buff, ',', '"', 1);) {

            String[] nextLine;
            String uri1, uri2, uri3, name, street, number, neighborhood, telephone, latitude, longitude;
            while ((nextLine = reader.readNext()) != null)
                try {
                    name = nextLine[0];
                    search(name, limit, offset, model);
                } catch (Exception e) {
                }
        }
        OutputStream out = new FileOutputStream(new File(RDF_DIR + "/" + FILENAME + "." + EXPORT_LANG.getFileExtensions().get(0)));
        RDFDataMgr.write(out, model, EXPORT_LANG);

        DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(DATA_URL);
        accessor.putModel(model);
        /*DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(UPDATE_URL);
        Model modelFuseki = accessor.getModel();
        modelFuseki.setNsPrefixes(model.getNsPrefixMap());
        StmtIterator stmts = model.listStatements();
        while (stmts.hasNext())
            try {
                modelFuseki.add(stmts.next());
            } catch (Exception e) {
            }*/
    }
}
