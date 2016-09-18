package uff.ic.swlab.datasetcrawler;

import org.apache.jena.rdf.model.Model;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import uff.ic.swlab.common.util.Config;
import uff.ic.swlab.common.util.SparqlServer;
import uff.ic.swlab.common.util.VoID;

public class RetrieveVoIDTask implements Runnable {

    private final Model void_;
    private final String[] urls;
    private final String[] sparqlEndPoints;
    private final SparqlServer server;
    private final String graphURI;

    private static final InstanceCounter INSTANCE_COUNTER = new InstanceCounter(Config.TASK_INSTANCES);

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

    public RetrieveVoIDTask(Model void_, String[] urls, String[] sparqlEndPoints, String graphURI, SparqlServer server) {
        INSTANCE_COUNTER.startInstance();
        this.void_ = void_;
        this.urls = urls;
        this.sparqlEndPoints = sparqlEndPoints;
        this.server = server;
        this.graphURI = graphURI;
    }

    @Override
    public final void run() {
        runTask();
        INSTANCE_COUNTER.finilizeInstance();
    }

    private void runTask() {
        Model void__ = VoID.retrieveVoID(urls, sparqlEndPoints);

        if (void__.size() > 0)
            this.void_.add(void__);
        else
            Logger.getLogger("empty").log(Level.INFO, String.format("Empty crawled VoID (<%1s>).", graphURI));

        if (this.void_ != null && this.void_.size() > 5 && VoID.isVoID(this.void_))
            server.putModel(graphURI, this.void_);
        else
            Logger.getLogger("datacrawler").log(Level.INFO, String.format("Dataset discarded (<%1s>).", graphURI));
    }
}
