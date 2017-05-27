package uff.ic.lleme.entityrelatednesstestdata.v3.model;

import java.util.List;

public abstract class PathElement {

    public abstract String getLabel();

    public abstract String getLocalName();

    public abstract String getUri();

    public abstract Double getScore();

    public abstract List<Resource> getSameAS();
}
