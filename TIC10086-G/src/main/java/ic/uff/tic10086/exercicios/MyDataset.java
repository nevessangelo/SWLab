package ic.uff.tic10086.exercicios;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public abstract class MyDataset {

    public static final String DOMAIN = "swlab.ic.uff.br";
    public static final String DEREF_PORT = "";
    public static final String SPARQL_PORT = ":3030";
    public static final Lang EXPORT_LANG = Lang.TTL;
    public static final Lang IMPORT_LANG = Lang.RDFXML;
    protected static final String OWL_DIR = "./src/main/resources/dat/owl";
    protected static final String RDF_DIR = "./src/main/resources/dat/rdf";
    protected static final String TDB_DIR = "./src/main/resources/dat/tdb";
    protected static final String CONF_DIR = "./src/main/resources/conf";

    public static String FILENAME = null;
    public static String DEFAULT_NS = null;
    public static String TDB_ASSEMBLER_FILE = null;
    public static String FUSEKI_UPDATE_URL = null;
    public static String FUSEKI_DATA_URL = null;

    protected static void init() {
        Logger.getRootLogger().setLevel(Level.OFF);
        new File(OWL_DIR).mkdirs();
        new File(RDF_DIR).mkdirs();
        new File(TDB_DIR).mkdirs();
        new File(CONF_DIR).mkdirs();
    }

    protected static void exportResources(Dataset dataset) throws FileNotFoundException {
        dataset.begin(ReadWrite.READ);

        Model model = dataset.getDefaultModel();
        OutputStream out = new FileOutputStream(new File(RDF_DIR + "/" + FILENAME + "." + EXPORT_LANG.getFileExtensions().get(0)));
        RDFDataMgr.write(out, model, EXPORT_LANG);
        DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(FUSEKI_DATA_URL);
        accessor.putModel(model);

        dataset.end();
    }

}
