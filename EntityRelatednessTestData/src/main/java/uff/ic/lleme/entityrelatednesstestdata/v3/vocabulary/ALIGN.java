package uff.ic.lleme.entityrelatednesstestdata.v3.vocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class ALIGN {

    private static final Model m = ModelFactory.createDefaultModel();

    public static final String NS = "http://knowledgeweb.semanticweb.org/heterogeneity/alignment#";
    public static final Resource NAMESPACE = m.createResource(NS);

    public static Resource Alignment = m.createResource(NS + "Alignment");
    public static Resource Cell = m.createResource(NS + "Cell");

    public static final Property entity1 = m.createProperty(NS + "entity1");
    public static final Property entity2 = m.createProperty(NS + "entity2");
    public static final Property relation = m.createProperty(NS + "relation");
    public static final Property map = m.createProperty(NS + "map");

}
