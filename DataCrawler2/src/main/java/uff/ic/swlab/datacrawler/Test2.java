package uff.ic.swlab.datacrawler;

import org.apache.jena.riot.Lang;

public class Test2 {

    public static void main(String[] args) {
        Lang[] langs = {Lang.TURTLE, Lang.RDFXML, Lang.NTRIPLES, Lang.JSONLD,
            Lang.NQUADS, Lang.TRIG, Lang.TRIX, Lang.RDFJSON, Lang.RDFTHRIFT};
        for (Lang l : langs)
            System.out.println(l.getContentType());
    }
}
