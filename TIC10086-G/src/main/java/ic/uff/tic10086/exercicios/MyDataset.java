package ic.uff.tic10086.exercicios;

import org.apache.jena.riot.Lang;

public abstract class MyDataset {

    public static final Lang EXPORT_LANG = Lang.TTL;
    public static final Lang IMPORT_LANG = Lang.RDFXML;
    protected static final String OWL_DIR = "./src/main/resources/dat/owl";
    protected static final String RDF_DIR = "./src/main/resources/dat/rdf";

}
