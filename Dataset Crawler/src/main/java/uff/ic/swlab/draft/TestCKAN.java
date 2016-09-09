package uff.ic.swlab.draft;

import eu.trentorise.opendata.jackan.CkanClient;
import java.util.List;

public class TestCKAN {

    public static void main(String[] args) {
        CkanClient cc = new CkanClient("http://linkeddatacatalog.dws.informatik.uni-mannheim.de");
        int limit = 1000, offset = 100000;
        List<String> datasets;
        while ((datasets = cc.getDatasetList(limit, offset)).size() > 0) {
            datasets.stream().forEach((dataset) -> {
                System.out.println(dataset);
            });
            offset += limit;
        }

    }
}
