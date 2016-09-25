package uff.ic.swlab.datasetcrawler;

import org.apache.jena.rdf.model.Model;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import uff.ic.swlab.commons.util.DCConf;
import uff.ic.swlab.commons.util.adapter.FusekiServer;
import uff.ic.swlab.commons.util.helper.VoIDHelper;
import uff.ic.swlab.datasetcrawler.model.Dataset;

public class GetVoIDTask implements Runnable {

    private final Dataset dataset;
    private final FusekiServer server;
    private final String graphURI;

    private static final InstanceCounter INSTANCE_COUNTER = new InstanceCounter(DCConf.TASK_INSTANCES);

    private static class InstanceCounter {

        private int instances;

        public InstanceCounter(int instances) {
            this.instances = instances;
        }

        public synchronized void startInstance() {
            while (true)
                if (instances > 0) {
                    instances--;
                    break;
                } else
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                    }
        }

        public synchronized void finilizeInstance() {
            instances++;
            notifyAll();
        }
    }

    public GetVoIDTask(Dataset dataset, String graphURI, FusekiServer server) {
        INSTANCE_COUNTER.startInstance();
        this.dataset = dataset;
        this.server = server;
        this.graphURI = graphURI;
    }

    @Override
    public final void run() {
        runTask();
        INSTANCE_COUNTER.finilizeInstance();
    }

    private void runTask() {
        try {
            String[] urls = dataset.getURLs(dataset);
            String[] sparqlEndPoints = dataset.getSparqlEndPoints();

            Model void_ = dataset.makeVoID();
            Model void__ = VoIDHelper.getContent(urls, sparqlEndPoints);

            if (void_.size() == 0)
                Logger.getLogger("info").log(Level.INFO, String.format("Empty default VoID (<%1s>).", graphURI));
            if (void__.size() == 0)
                Logger.getLogger("info").log(Level.INFO, String.format("Empty crawled VoID (<%1s>).", graphURI));

            if (void_.add(void__).size() > 5)
                server.putModel(graphURI, void_);
            else
                Logger.getLogger("info").log(Level.INFO, String.format("Dataset discarded (<%1s>).", graphURI));
        } catch (Throwable e) {
            Logger.getLogger("error").log(Level.ERROR, String.format("Task error (<%1s>). Msg: %2s", graphURI, e.getMessage()));
        }
    }
}
