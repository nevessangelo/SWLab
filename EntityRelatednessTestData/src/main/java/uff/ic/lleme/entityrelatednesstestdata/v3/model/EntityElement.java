package uff.ic.lleme.entityrelatednesstestdata.v3.model;

public class EntityElement extends PathElement {

    private Entity entity = null;

    private Path path = null;

    private double score = 0;

    public EntityElement(Entity entity, Path path, Double[] score) throws Exception {
        if (entity == null) {
            System.out.println(String.format("Error: null entity."));
            throw new Exception();
        } else
            this.entity = entity;

        if (path == null) {
            System.out.println(String.format("Error: missing path. (entity -> %1s)", entity.getLabel()));
            throw new Exception();
        } else
            this.path = path;

        if (score.length == 0) {
            System.out.println(String.format("Error: missing score. (entity -> %1s)", entity.getLabel()));
            throw new Exception();
        } else if (score.length > 1) {
            System.out.println(String.format("Error: duplicate scores. (entity -> %1s)", entity.getLabel()));
            throw new Exception();
        } else
            this.score = score[0];
    }

    @Override
    public Double getScore() {
        return score;
    }

    @Override
    public String getLabel() {
        return entity.getLabel();
    }

    @Override
    public Path getPath() {
        return path;
    }

}
