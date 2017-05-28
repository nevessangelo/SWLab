package uff.ic.lleme.entityrelatednesstestdata.v3.model;

import java.util.ArrayList;
import java.util.List;

public class EntityPair {

    private Entity entity1 = null;
    private Entity entity2 = null;
    private List<Path> rank = new ArrayList<>();

    private EntityPair() {

    }

    public EntityPair(String entity1, String entity2) throws Exception {
        this.entity1 = DB.Entities.getEntity(entity1);
        this.entity2 = DB.Entities.getEntity(entity2);
        if (this.entity1 == null && this.entity2 == null) {
            System.out.println(String.format("EntityPair error: undefined entity (label1 -> %1s, label2 -> %1s).", entity1, entity2));
            throw new Exception();
        } else if (this.entity1 == null) {
            System.out.println(String.format("EntityPair error: undefined entity (label1 -> %1s).", entity1));
            throw new Exception();
        } else if (this.entity2 == null) {
            System.out.println(String.format("EntityPair error: undefined entity (label2 -> %1s).", entity2));
            throw new Exception();
        }
    }

    public String getLabel() {
        return (entity1.getLabel() + "-" + entity2.getLabel());
    }

    public Entity getEntity1() {
        return entity1;
    }

    public Entity getEntity2() {
        return entity2;
    }

    public boolean add(Path path) throws Exception {
        path.entityPair = this;
        if (path != null)
            return rank.add(path);
        else
            throw new Exception();
    }

    public List<Path> getRank() {
        return rank;
    }

    public static class Path {

        private EntityPair entityPair = null;
        private int rankPosition = 0;
        private double score = 0;
        private String expression = null;
        private List<PathElement> elements = new ArrayList<>();

        public Path(Entity entity) {
            super();
        }

        public Path(int position, Double[] score, String expression) throws Exception {
            this.rankPosition = position;

            if (score.length == 0) {
                System.out.println(String.format("Path error: missing score."));
                throw new Exception();
            } else if (score.length > 1) {
                System.out.println(String.format("Path error: duplicate scores."));
                throw new Exception();
            } else
                this.score = score[0];

            if (expression.equals("")) {
                System.out.println(String.format("Path error: missing expression."));
                throw new Exception();
            } else
                this.expression = expression;
        }

        public EntityPair getEntityPair() {
            return entityPair;
        }

        public boolean addPathElement(PathElement element) {
            element.path = this;
            return elements.add(element);
        }

        public List<PathElement> getElements() {
            return elements;
        }

        public static abstract class PathElement {

            private EntityPair.Path path = null;

            public EntityPair.Path getPath() {
                return path;
            }

            public abstract String getLabel();

            public abstract Double getScore();
        }

        public static class EntityElement extends EntityPair.Path.PathElement {

            private Entity entity = null;
            private double score = 0;

            public EntityElement(String entity, Double[] score) throws Exception {
                this.entity = DB.Entities.getEntity(entity);
                if (this.entity == null) {
                    System.out.println(String.format("Error: null entity."));
                    throw new Exception();
                }

                if (score.length == 0) {
                    System.out.println(String.format("Error: missing score."));
                    throw new Exception();
                } else if (score.length > 1) {
                    System.out.println(String.format("Error: duplicate scores."));
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

        }

        public static class PropertyElement extends EntityPair.Path.PathElement {

            private Property property = null;

            private PropertyElement() {
                super();
            }

            public PropertyElement(String property) throws Exception {
                this.property = DB.Properties.getPropety(property);
                if (this.property == null) {
                    System.out.println(String.format("Error: null entity."));
                    throw new Exception();
                }
            }

            @Override
            public Double getScore() {
                return property.getScore();
            }

            @Override
            public String getLabel() {
                return property.getLabel();
            }
        }
    }
}
