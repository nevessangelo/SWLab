package ic.uff.tic10086.examples.jena;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.DC;

public class teste {

    public static void main(String[] args) {

        Model modelProv = ModelFactory.createDefaultModel();
        modelProv.read("http://www.w3.org/ns/prov");
        modelProv.setNsPrefix("prov", "http://www.w3.org/ns/prov#");

        Resource keyValueProperty = modelProv.getResource("http://www.w3.org/ns/prov#KeyValuePair");
        Property key = modelProv.getProperty("http://www.w3.org/ns/prov#pairKey");
        Property value = modelProv.getProperty("http://www.w3.org/ns/prov#pairValue");

        Model model = ModelFactory.createDefaultModel();
        String lakPaperURI = "http://swlab.ic.uff.br/id/0123456789";

        // Veja em https://www.w3.org/TR/2013/WD-prov-dictionary-20130312/#KeyValuePair
        Resource lakPaper = model.createResource(lakPaperURI)
            .addProperty(DC.subject, model.createResource(keyValueProperty)
                .addProperty(key, "topico1")
                .addProperty(value, "5.8"))
            .addProperty(DC.subject, model.createResource(keyValueProperty)
                .addProperty(key, "topico2")
                .addProperty(value, "1.2"))
            .addProperty(DC.subject, model.createResource(keyValueProperty)
                .addProperty(key, "topico3")
                .addProperty(value, "7.6"));

        StmtIterator iter = model.listStatements();
        while (iter.hasNext())
            System.out.println(iter.next().toString());
    }
}
