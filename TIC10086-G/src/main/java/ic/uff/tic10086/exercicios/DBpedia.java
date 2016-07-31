package ic.uff.tic10086.exercicios;

import static ic.uff.tic10086.utils.DBpediaSearch.search;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

    public static void main(String[] args) {
        try {
            Logger.getRootLogger().setLevel(Level.OFF);
            extractSample();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void extractSample() throws FileNotFoundException {
        Model model = ModelFactory.createDefaultModel();
        String keywordsString = "Cidade das Artes";
        int limit = 20, offset = 0;

        search(keywordsString, limit, offset, model);

        RDFDataMgr.write(new FileOutputStream(new File(RDF_DIR + "/" + FILENAME + "." + EXPORT_LANG.getFileExtensions().get(0))), model, EXPORT_LANG);
    }
}
