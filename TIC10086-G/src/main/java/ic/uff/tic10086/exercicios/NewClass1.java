package ic.uff.tic10086.exercicios;

import ic.uff.tic10086.utils.DBpediaSearch;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;

public class NewClass1 {

    public static void main(String[] args) {
        Model model = ModelFactory.createDefaultModel();
        DBpediaSearch.search("Pedra da Gávea", 7, 0, model);
        model.write(System.out, Lang.TURTLE.getName());
    }
}
