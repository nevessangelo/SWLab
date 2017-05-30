package uff.ic.lleme.entityrelatednesstestdata.v3.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import uff.ic.lleme.entityrelatednesstestdata.v3.Config;

public class EntityPair_ {

    private final String localName = "id-" + UUID.randomUUID().toString();
    private final String uri = Config.DATA_NS + localName;
    private Entity_ entity1 = null;
    private Entity_ entity2 = null;
    private List<Path_> rank = new ArrayList<>();

    private EntityPair_() {

    }

    public EntityPair_(String entity1, String entity2) throws Exception {
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

    public String getLocalName() {
        return localName;
    }

    public String getUri() {
        return uri;
    }

    public Entity_ getEntity1() {
        return entity1;
    }

    public Entity_ getEntity2() {
        return entity2;
    }

    public List<Path_> listPaths() {
        return rank;
    }

    public boolean add(Path_ path) throws Exception {
        path.entityPair = this;
        if (path != null)
            return rank.add(path);
        else
            throw new Exception();
    }

    public List<Path_> getRank() {
        return rank;
    }

    public static class Path_ {

        private EntityPair_ entityPair = null;
        private String localName = "id-" + UUID.randomUUID().toString();
        private String uri = Config.DATA_NS + localName;
        private int rankPosition = 0;
        private double score = 0;
        private String expression = null;
        private List<PathElement_> elements = new ArrayList<>();

        public Path_(Entity_ entity) {
            super();
        }

        public Path_(int position, Double[] score, String expression) throws Exception {
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

        public EntityPair_ getEntityPair() {
            return entityPair;
        }

        public int getRankPosition() {
            return rankPosition;
        }

        public double getScore() {
            return score;
        }

        public String getExpression() {
            return expression;
        }

        public String getLocalName() {
            return localName;
        }

        public String getUri() {
            return uri;
        }

        public List<PathElement_> listElements() {
            return elements;
        }

        public boolean addPathElement(PathElement_ element) {
            element.path = this;
            return elements.add(element);
        }

        public static abstract class PathElement_ {

            private String localName = "id-" + UUID.randomUUID().toString();
            private String uri = Config.DATA_NS + localName;
            private EntityPair_.Path_ path = null;

            public EntityPair_.Path_ getPath() {
                return path;
            }

            public String getLocalName() {
                return localName;
            }

            public String getUri() {
                return uri;
            }

            public abstract String getLabel();

            public abstract Double getScore();
        }

        public static class EntityElement_ extends EntityPair_.Path_.PathElement_ {

            private Entity_ entity = null;
            private double score = 0;

            public EntityElement_(String entity, Double[] score) throws Exception {
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

        public static class PropertyElement_ extends EntityPair_.Path_.PathElement_ {

            private Property_ property = null;

            private PropertyElement_() {
                super();
            }

            public PropertyElement_(String property) throws Exception {
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
