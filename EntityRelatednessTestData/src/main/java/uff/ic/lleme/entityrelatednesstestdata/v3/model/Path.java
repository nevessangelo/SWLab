package uff.ic.lleme.entityrelatednesstestdata.v3.model;

import java.util.ArrayList;
import java.util.List;

public class Path {

    private int rank = 0;
    private double score = 0;
    private String expression = null;
    private EntityPair entityPair = null;
    private List<PathElement> elements = new ArrayList<>();

    public Path(Entity entity) {

    }

    public Path(EntityPair entityPair, int position, Double[] score, String expression) throws Exception {
        this.rank = position;

        if (entityPair == null) {
            System.out.println(String.format("Error: null entity pair."));
            throw new Exception();
        } else
            this.entityPair = entityPair;

        if (score.length == 0) {
            System.out.println(String.format("Error: missing score. (pair -> %1s, position -> %1s)", entityPair.getEntity1().getLabel() + "-" + entityPair.getEntity2().getLabel(), position));
            throw new Exception();
        } else if (score.length > 1) {
            System.out.println(String.format("Error: duplicate scores. (pair -> %1s, position -> %1s)", entityPair.getEntity1().getLabel() + "-" + entityPair.getEntity2().getLabel(), position));
            throw new Exception();
        } else
            this.score = score[0];

        if (expression.equals("")) {
            System.out.println(String.format("Error: missing score. (pair -> %1s, position -> %1s)", entityPair.getEntity1().getLabel() + "-" + entityPair.getEntity2().getLabel(), position));
            throw new Exception();
        } else
            this.expression = expression;

    }

    public boolean addPathElement(PathElement element) {
        return elements.add(element);
    }

    public List<PathElement> getElements() {
        return elements;
    }

}
