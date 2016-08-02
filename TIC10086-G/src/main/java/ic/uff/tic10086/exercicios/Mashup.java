package ic.uff.tic10086.exercicios;

import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;

public class Mashup extends MyDataset {

    public static final String FILENAME = "mashup";
    public static final String TDB_ASSEMPLER_FILE = "./src/main/resources/conf/mashup.ttl";
    public static final String FUSEKI_UPDATE_URL = "http://localhost:3030/mashup/update";
    public static final String FUSEKI_DATA_URL = "http://localhost:3030/mashup/data";

    public static void main(String[] args) {
        try {
            init();

            Dataset dataset = TDBFactory.assembleDataset(TDB_ASSEMPLER_FILE);
            mergeDatasets(dataset);
            dataset.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void mergeDatasets(Dataset dataset) {
        Dataset referenceLinks = TDBFactory.assembleDataset(TDB_ASSEMPLER_FILE);
        Model model = referenceLinks.getDefaultModel();

        dataset.close();
    }

}
