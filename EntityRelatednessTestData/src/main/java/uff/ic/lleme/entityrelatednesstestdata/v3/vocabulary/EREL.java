package uff.ic.lleme.entityrelatednesstestdata.v3.vocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class EREL {

    private static final Model m = ModelFactory.createDefaultModel();

    public static final String NS = "http://swlab.ic.uff.br/ontology/EntityRelatednessTestData_v1.rdf#";
    public static final Resource NAMESPACE = m.createResource(NS);

    public static Resource Entity = m.createResource(NS + "Entity");
    public static Resource EntityPair = m.createResource(NS + "EntityPair");
    public static Resource Path = m.createResource(NS + "Path");
    public static Resource PathElement = m.createResource(NS + "PathElement");
    public static Resource ListOfPathElement = m.createResource(NS + "ListOfPathElement");

    public static final Property first = m.createProperty(NS + "first");
    public static final Property firstpathElement = m.createProperty(NS + "firstPathElement");
    public static final Property restOfpathElement = m.createProperty(NS + "restOfPathElement");
    public static final Property second = m.createProperty(NS + "second");
    public static final Property hasPath = m.createProperty(NS + "hasPath");
    public static final Property haslistOfPathElements = m.createProperty(NS + "haslistOfPathElements");
    public static final Property rank = m.createProperty(NS + "rank");
    public static final Property score = m.createProperty(NS + "score");
    public static final Property expression = m.createProperty(NS + "expression");
    public static final Property category = m.createProperty(NS + "category");

}
