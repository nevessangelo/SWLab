package ic.uff.tic10086.examples.jena;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.sdb.SDBFactory;
import org.apache.jena.sdb.Store;
import org.apache.jena.sdb.StoreDesc;
import org.apache.jena.sdb.sql.SDBConnection;
import org.apache.jena.sdb.util.StoreUtils;

public class ConnectingSDBStore {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        String jdbcurl = "jdbc:postgresql://localhost:5432/swlab";
        String username = "postgres";
        String password = "fluminense";
        Class.forName("org.postgresql.Driver");
        Connection jdbcConnection = DriverManager.getConnection(jdbcurl, username, password);
        jdbcConnection.setSchema("rdf");

        StoreDesc storeDesc = StoreDesc.read("./conf/sdb.ttl");
        SDBConnection conn = SDBFactory.createConnection(jdbcConnection);
        Store store = SDBFactory.connectStore(conn, storeDesc);

        Dataset ds = SDBFactory.connectDataset(store);

        Model model = SDBFactory.connectDefaultModel(store);

        if (!StoreUtils.isFormatted(store))
            store.getTableFormatter().create();

        store.close();
    }
}
