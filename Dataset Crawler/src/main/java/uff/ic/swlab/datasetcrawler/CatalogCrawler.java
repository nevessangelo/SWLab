package uff.ic.swlab.datasetcrawler;

import eu.trentorise.opendata.jackan.CkanClient;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CatalogCrawler extends Crawler<Dataset> {

    private final CkanClient cc;
    private int offset = 0;
    private int limit = 1000;
    private List<String> names;
    private Iterator<String> iterator;

    public CatalogCrawler(String url) {
        cc = new CkanClient(url);
        try {
            names = cc.getDatasetList(limit, offset);
            iterator = names.iterator();
        } catch (Throwable e) {
            names = new ArrayList<>();
            iterator = names.iterator();
        }
    }

    @Override
    public boolean hasNext() {
        try {
            boolean hasNext = iterator.hasNext();
            if (!hasNext) {
                offset += limit;
                names = cc.getDatasetList(limit, offset);
                iterator = names.iterator();
                hasNext = iterator.hasNext();
            }
            return hasNext;
        } catch (Throwable e) {
            return false;
        }
    }

    @Override
    public Dataset next() {
        try {
            return new Dataset(cc, cc.getDataset(iterator.next()));
        } catch (Throwable e) {
            return null;
        }
    }

    @Override
    public void close() {

    }

}
