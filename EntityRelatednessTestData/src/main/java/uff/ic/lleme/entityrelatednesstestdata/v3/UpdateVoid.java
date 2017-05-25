package uff.ic.lleme.entityrelatednesstestdata.v3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.log4j.PropertyConfigurator;
import uff.ic.swlab.commons.util.Host;
import uff.ic.swlab.dataset.SWLABVoID;

public class UpdateVoid {

    public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
        PropertyConfigurator.configure("./resources/conf/log4j.properties");
        Config.configure("./resources/conf/entityrelatednesstestdata.properties");

        Model void_ = (new SWLABVoID()).getModel();

        (new File(Config.RDF_ROOT)).mkdirs();
        try (OutputStream out = new FileOutputStream(Config.LOCAL_VOID_NAME);) {
            RDFDataMgr.write(out, void_, Lang.TURTLE);
        } finally {
        }
        try (InputStream in = new FileInputStream(Config.LOCAL_VOID_NAME)) {
            Host.uploadViaFTP(Config.HOST_ADDR, Config.USERNAME, Config.PASSWORD, "/", Config.REMOTE_VOID_NAME, in);
        } finally {
        }
    }
}
