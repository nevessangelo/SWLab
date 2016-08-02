package ic.uff.tic10086.exercicios;

import java.io.File;
import org.apache.jena.riot.Lang;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public abstract class MyDataset {

    public static final Lang EXPORT_LANG = Lang.TTL;
    public static final Lang IMPORT_LANG = Lang.RDFXML;
    protected static final String OWL_DIR = "./src/main/resources/dat/owl";
    protected static final String RDF_DIR = "./src/main/resources/dat/rdf";
    protected static final String TDB_DIR = "./src/main/resources/dat/tdb";

    protected static void init() {
        Logger.getRootLogger().setLevel(Level.OFF);
        new File(OWL_DIR).mkdirs();
        new File(RDF_DIR).mkdirs();
        new File(TDB_DIR).mkdirs();
    }

}
