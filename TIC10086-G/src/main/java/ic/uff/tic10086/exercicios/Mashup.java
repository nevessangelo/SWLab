package ic.uff.tic10086.exercicios;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.tdb.TDBFactory;

public class Mashup extends MyDataset {

    public static final String SOURCE_ASSEMBLER_FILE = "./src/main/resources/conf/dbpedia.ttl";
    public static final String TARGET_ASSEMBLER_FILE = "./src/main/resources/conf/turismoECultura.ttl";
    public static final String REFERENCE_LINKS_ASSEMBLER_FILE = "./src/main/resources/conf/referenceLinks.ttl";
    public static final String REFERENCE_LINKS_FILENAME = "./src/main/resources/dat/rdf/referenceLinks.xml";
    public static final String REFERENCE_LINKS_FUSEKI_DATA_URL = "http://localhost:3030/referenceLinks/data";

    public static final String ALIGNMENT_TO_SAME_AS_RULES = "./src/main/resources/dat/rdf/alignmentToSameAs.rules";

    public static void main(String[] args) {
        try {
            init();

            FILENAME = "mashup";
            TDB_ASSEMPLER_FILE = "./src/main/resources/conf/mashup.ttl";
            FUSEKI_UPDATE_URL = "http://localhost:3030/mashup/update";
            FUSEKI_DATA_URL = "http://localhost:3030/mashup/data";

            Dataset source = TDBFactory.assembleDataset(SOURCE_ASSEMBLER_FILE);
            Dataset target = TDBFactory.assembleDataset(TARGET_ASSEMBLER_FILE);
            Dataset referenceLinks = TDBFactory.assembleDataset(REFERENCE_LINKS_ASSEMBLER_FILE);
            Dataset mashup = TDBFactory.assembleDataset(TDB_ASSEMPLER_FILE);

            try {

                prepareReferenceLinks(referenceLinks);
                mergeDatasets(source, target, referenceLinks, mashup);
                exportResources(mashup);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                source.close();
                target.close();
                referenceLinks.close();
                mashup.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void mergeDatasets(Dataset source, Dataset target, Dataset referenceLinks, Dataset mashup) throws FileNotFoundException {

        BufferedReader br = new BufferedReader(new FileReader(ALIGNMENT_TO_SAME_AS_RULES));
        List rules = Rule.parseRules(Rule.rulesParserFromReader(br));

    }

    private static void prepareReferenceLinks(Dataset referenceLinks) throws FileNotFoundException {
        referenceLinks.begin(ReadWrite.WRITE);
        Model model = referenceLinks.getDefaultModel();
        model.getNsPrefixMap().clear();
        model.removeAll();
        model.read(REFERENCE_LINKS_FILENAME);
        DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(REFERENCE_LINKS_FUSEKI_DATA_URL);
        accessor.putModel(model);
        referenceLinks.commit();
    }

}
