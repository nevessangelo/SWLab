package uff.ic.swlab.draft;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

public class NewClass3 {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        Model m = ModelFactory.createDefaultModel();
        Dataset ds = RDFDataMgr.loadDataset("./src/main/resources/dat/rdf/void_2016-09-23_20-26-12.nq.gz");
        RDFDataMgr.write(System.out, ds, Lang.TRIG);
    }
}
