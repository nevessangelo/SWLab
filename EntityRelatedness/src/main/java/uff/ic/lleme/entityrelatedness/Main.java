package uff.ic.lleme.entityrelatedness;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        MusicEntityPairs musicEntityPairs = new MusicEntityPairs();
        MovieEntityPairs movieEntityPairs = new MovieEntityPairs();
        MovieClassMapping movieClassMapping = new MovieClassMapping();
        MusicClassMapping musicClassMapping = new MusicClassMapping();

        MovieEntityMappings movieEntityMapping = new MovieEntityMappings();
        MoviePropertyRelevanceScore moviePropertyRelevanceScore = new MoviePropertyRelevanceScore();
        MovieRankedPaths movieRankedPaths = new MovieRankedPaths();
        MovieScores movieScores = new MovieScores();

        MusicEntityMappings musicEntityMapping = new MusicEntityMappings();
        MusicPropertyRelevanceScore musicPropertyRelevanceScore = new MusicPropertyRelevanceScore();
        MusicRankedPaths musicRankedPaths = new MusicRankedPaths();
        MusicScores musicScores = new MusicScores();

        String align = "http://knowledgeweb.semanticweb.org/heterogeneity/alignment#";
        String pucrio = "http://inf.puc-rio.br/";
        Model model = ModelFactory.createDefaultModel();
        model.setNsPrefix("", pucrio);
        model.setNsPrefix("align", align);

        Resource alignmentClass = model.createResource(align + "Alignment");
        Resource alignment = model.createResource(alignmentClass);

        for (Map.Entry<String, ArrayList<Pair>> entry : movieEntityMapping.entrySet())
            for (Pair pair : entry.getValue()) {
                Resource cell = model.createResource(pucrio + pair.label, model.createResource(align + "Cell"));
                cell.addProperty(model.createProperty(align + "entity1"), pair.entity1);
                cell.addProperty(model.createProperty(align + "entity2"), pair.entity2);
                alignment.addProperty(model.createProperty(align + "map"), cell.as(RDFNode.class));
            }

        for (Map.Entry<String, ArrayList<Pair>> entry : musicEntityMapping.entrySet())
            for (Pair pair : entry.getValue()) {
                Resource cell = model.createResource(pucrio + pair.label, model.createResource(align + "Cell"));
                cell.addProperty(model.createProperty(align + "entity1"), pair.entity1);
                cell.addProperty(model.createProperty(align + "entity2"), pair.entity2);
                alignment.addProperty(model.createProperty(align + "map"), cell.as(RDFNode.class));
            }

        OutputStream out = new FileOutputStream(new File("./data/EntityRelatednessTestDataset/EntityRelatednessTestData.ttl"));
        RDFDataMgr.write(out, model, Lang.TURTLE);

        OutputStream out2 = new FileOutputStream(new File("./data/EntityRelatednessTestDataset/EntityRelatednessTestData.rdf"));
        RDFDataMgr.write(out2, model, Lang.RDFXML);

        OutputStream out3 = new FileOutputStream(new File("./data/EntityRelatednessTestDataset/EntityRelatednessTestData.json"));
        RDFDataMgr.write(out3, model, Lang.JSONLD);

        DatasetAccessor accessor = DatasetAccessorFactory.createHTTP("http://swlab.ic.uff.br/fuseki/EntityRelatedness/data");
        accessor.putModel(model);

        System.out.println("Fim.");
    }
}
