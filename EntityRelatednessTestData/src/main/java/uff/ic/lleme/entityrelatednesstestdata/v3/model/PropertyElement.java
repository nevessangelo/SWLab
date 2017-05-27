package uff.ic.lleme.entityrelatednesstestdata.v3.model;

public class PropertyElement extends PathElement {

    private Property property = null;

    private Path path = null;

    private PropertyElement() {

    }

    public PropertyElement(Property property, Path path) throws Exception {
        if (property == null) {
            System.out.println(String.format("Error: null property."));
            throw new Exception();
        } else
            this.property = property;

        if (path == null) {
            System.out.println(String.format("Error: missing path. (entity -> %1s)", property.getLabel()));
            throw new Exception();
        } else
            this.path = path;

    }

    @Override
    public Double getScore() {
        return property.getScore();
    }

    @Override
    public String getLabel() {
        return property.getLabel();
    }

    @Override
    public Path getPath() {
        return path;
    }
}
