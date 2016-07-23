package ic.uff.tic10086.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.AutoIRIMapper;

public class OWLAPIOntology {

    private static final String DIRECTORY = "./src/main/resources/dat/owl";
    private static OWLOntologyManager MANAGER = null;

    private static OWLOntologyManager getOntologyManager() {
        if (MANAGER == null) {
            MANAGER = OWLManager.createOWLOntologyManager();
            MANAGER.getIRIMappers().add(new AutoIRIMapper(new File(DIRECTORY), true));
        }
        return MANAGER;
    }

    public static OWLOntology getOntology(String urlString) throws MalformedURLException, IOException, CompressorException, OWLOntologyCreationException {
        OWLOntology ontology;
        URL url = new URL(urlString);
        try (BufferedInputStream bis = new BufferedInputStream(url.openStream());) {

            ontology = getOntologyManager().loadOntologyFromOntologyDocument(bis);
        } catch (Exception ex) {
            try (
                    BufferedInputStream bis = new BufferedInputStream(url.openStream());
                    CompressorInputStream input = new CompressorStreamFactory().createCompressorInputStream(bis);) {

                ontology = getOntologyManager().loadOntologyFromOntologyDocument(input);
            }
        }
        return ontology;
    }
}
