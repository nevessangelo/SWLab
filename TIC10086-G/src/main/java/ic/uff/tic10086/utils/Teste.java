package ic.uff.tic10086.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.jena.riot.Lang;

public class Teste {

    public static void main(String[] args) throws IOException, MalformedURLException, CompressorException {
        JenaSchema.getSchema("http://dbpedia.org/describe/?url=http%3A%2F%2Fdbpedia.org%2Fresource%2FCidade_das_Artes&sid=30934", null, "teste", Lang.RDFXML);
    }
}
