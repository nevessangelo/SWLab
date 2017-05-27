package uff.ic.lleme.entityrelatednesstestdata.v3.model;

import java.util.ArrayList;
import java.util.List;

public class Path {

    private int rank = 0;
    private double score = 0;
    private String expression = null;
    private List<PathElement> elements = new ArrayList<>();

    private Path() {

    }

    public Path(int rank, double score, String expression) throws Exception {
        this.rank = rank;
        this.score = score;
        if (!expression.equals(""))
            this.expression = expression;
        else
            throw new Exception();
    }

    public boolean addPathElement(PathElement element) {
        return elements.add(element);
    }

    public List<PathElement> getElements() {
        return elements;
    }

}
