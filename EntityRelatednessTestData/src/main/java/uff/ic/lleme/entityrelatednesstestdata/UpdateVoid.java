package uff.ic.lleme.entityrelatednesstestdata;

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
        MyConfig.configure("./resources/conf/entityrelatednesstestdata.properties");

        Model void_ = (new SWLABVoID()).getModel();

        (new File(MyConfig.RDF_ROOT)).mkdirs();
        try (OutputStream out = new FileOutputStream(MyConfig.LOCAL_VOID_NAME);) {
            RDFDataMgr.write(out, void_, Lang.TURTLE);
        } finally {
        }
        try (InputStream in = new FileInputStream(MyConfig.LOCAL_VOID_NAME)) {
            Host.uploadViaFTP(MyConfig.HOST_ADDR, MyConfig.USERNAME, MyConfig.PASSWORD, MyConfig.REMOTE_VOID_NAME, in);
        } finally {
        }
    }
}
