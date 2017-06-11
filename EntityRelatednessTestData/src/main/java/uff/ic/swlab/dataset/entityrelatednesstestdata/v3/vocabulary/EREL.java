package uff.ic.swlab.dataset.entityrelatednesstestdata.v3.vocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class EREL {

    private static final Model m = ModelFactory.createDefaultModel();

    public static final String NS = "http://swlab.ic.uff.br/ontology/EntityRelatednessTestData_v1/#";
    public static final Resource NAMESPACE = m.createResource(NS);

    public static Resource Category = m.createResource(NS + "Category");
    public static Resource Entity = m.createResource(NS + "Entity");
    public static Resource EntityPair = m.createResource(NS + "EntityPair");
    public static Resource Path = m.createResource(NS + "Path");
    public static Resource ListOfPathElements = m.createResource(NS + "ListOfPathElements");
    public static Resource PathElement = m.createResource(NS + "PathElement");
    public static Resource EntityElement = m.createResource(NS + "EntityElement");
    public static Resource PropertyElement = m.createResource(NS + "PropertyElement");

    public static final Property type = m.createProperty(NS + "type");
    public static final Property entity1 = m.createProperty(NS + "entity1");
    public static final Property entity2 = m.createProperty(NS + "entity2");
    public static final Property hasPath = m.createProperty(NS + "hasPath");
    public static final Property rankPosition = m.createProperty(NS + "rankPosition");
    public static final Property score = m.createProperty(NS + "score");
    public static final Property expression = m.createProperty(NS + "expression");
    public static final Property first = m.createProperty(NS + "first");
    public static final Property rest = m.createProperty(NS + "rest");
    public static final Property position = m.createProperty(NS + "position");
    public static final Property hasListOfPathElements = m.createProperty(NS + "hasListOfPathElements");
    public static final Property property = m.createProperty(NS + "property");
    public static final Property entity = m.createProperty(NS + "entity");

}
