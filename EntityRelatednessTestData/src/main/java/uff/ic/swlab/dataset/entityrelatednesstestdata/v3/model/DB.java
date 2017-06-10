package uff.ic.swlab.dataset.entityrelatednesstestdata.v3.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.validator.routines.UrlValidator;
import uff.ic.swlab.dataset.entityrelatednesstestdata.v3.Config;
import uff.ic.swlab.dataset.entityrelatednesstestdata.v3.vocabulary.EREL;

public class DB {

    public static class Resources {

        private static Resources setOfResources = null;
        private final Map<String, Resource> RESOURCES = new HashMap<>();

        private Resources() {
            super();
        }

        private static Resources getInstance() {
            if (setOfResources == null)
                setOfResources = new Resources();
            return setOfResources;
        }

        public static Resource addResource(String uriString) throws Exception {
            if (getInstance().RESOURCES.containsKey(uriString))
                return getInstance().RESOURCES.get(uriString);
            else {
                Resource resource = new Resource(uriString);
                getInstance().RESOURCES.put(resource.getURI(), resource);
                return resource;
            }
        }

        public static Resource getResource(String uriString) {
            return getInstance().RESOURCES.get(uriString);
        }
    }

    public static class Categories {

        private static Categories setOfCategories = null;
        private final Map<String, Category> CATEGORIES = new HashMap<>();

        private Categories() {
            super();
        }

        private static Categories getInstance() {
            if (setOfCategories == null)
                setOfCategories = new Categories();
            return setOfCategories;
        }

        public static Category addCategory(String label) throws Exception {
            label = label.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");

            if (getInstance().CATEGORIES.containsKey(label))
                return getInstance().CATEGORIES.get(label);
            else {
                Category category = new Category(label);
                getInstance().CATEGORIES.put(category.getLabel(), category);
                return category;
            }
        }

        public static Category getCategory(String label) {
            label = label.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");
            return getInstance().CATEGORIES.get(label);
        }

        public static Collection<Category> listCategories() {
            return getInstance().CATEGORIES.values();
        }
    }

    public static class Entities {

        private static Entities setOfEntities = null;
        private final Map<String, Entity> ENTITIES = new HashMap<>();

        private Entities() {
            super();
        }

        private static Entities getInstance() {
            if (setOfEntities == null)
                setOfEntities = new Entities();
            return setOfEntities;
        }

        public static Entity addEntity(String label, String category) throws Exception {
            label = label.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");

            if (getInstance().ENTITIES.containsKey(label))
                return getInstance().ENTITIES.get(label);
            else {
                Entity entity = new Entity(label, category);
                getInstance().ENTITIES.put(entity.getLabel(), entity);
                return entity;
            }
        }

        public static Entity getEntity(String label) {
            label = label.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");

            return getInstance().ENTITIES.get(label);
        }

        public static Collection<Entity> listEntities() {
            return getInstance().ENTITIES.values();
        }
    }

    public static class Properties {

        private static Properties setOfProperties = null;
        private final Map<String, Property> PROPERTIES = new HashMap<>();

        private Properties() {
            super();
        }

        private static Properties getInstance() {
            if (setOfProperties == null)
                setOfProperties = new Properties();
            return setOfProperties;
        }

        public static Property addProperty(String label, double score) throws Exception {
            label = label.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");

            if (getInstance().PROPERTIES.containsKey(label))
                return getInstance().PROPERTIES.get(label);
            else {
                Property property = new Property(label, score);
                getInstance().PROPERTIES.put(property.getLabel(), property);
                return property;
            }
        }

        public static Property getPropety(String label) {
            label = label.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");
            return getInstance().PROPERTIES.get(label);
        }
    }

    public static class EntityPairs {

        private static EntityPairs setOfEntityPairs = null;
        private final Map<String, EntityPair> ENTITY_PAIRS = new HashMap<>();

        private EntityPairs() {
            super();
        }

        private static EntityPairs getInstance() {
            if (setOfEntityPairs == null)
                setOfEntityPairs = new EntityPairs();
            return setOfEntityPairs;
        }

        public static EntityPair addEntityPair(String entity1, String entity2) throws Exception {
            entity1 = entity1.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");
            entity2 = entity2.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");

            if (getInstance().ENTITY_PAIRS.containsKey(entity1 + "-" + entity2))
                return getInstance().ENTITY_PAIRS.get(entity1 + "-" + entity2);
            else {
                EntityPair entityPair = new EntityPair(entity1, entity2);
                getInstance().ENTITY_PAIRS.put(entity1 + "-" + entity2, entityPair);
                return entityPair;
            }
        }

        public static EntityPair getEntityPair(String entity1, String entity2) {
            entity1 = entity1.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");
            entity2 = entity2.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");

            return getInstance().ENTITY_PAIRS.get(entity1 + "-" + entity2);
        }

        public static Collection<EntityPair> listEntityPairs() {
            return getInstance().ENTITY_PAIRS.values();
        }
    }

    public static class Resource {

        private String uri = null;

        private Resource() {
            super();
        }

        public Resource(String uriString) throws Exception {
            super();
            if (!(new UrlValidator()).isValid(uriString)) {
                System.out.println(String.format("Resource error: invalid uriString. (uri -> %1s)", uriString));
                throw new Exception();
            } else
                this.uri = uriString;
        }

        public String getURI() {
            return uri;
        }
    }

    public static class Category {

        private String label = null;
        private String localName = null;
        private String uri = null;
        private Set<Resource> sameAS = new HashSet<>();

        private Category() {
        }

        public Category(String label) throws Exception {
            if (label == null || label.equals("")) {
                System.out.println(String.format("Category error: invalid label (label -> %1s).", label));
                throw new Exception();
            } else {
                this.label = label.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");
                String uriString = EREL.NS + this.label;
                if ((new UrlValidator()).isValid(uriString)) {
                    this.localName = this.label;
                    this.uri = uriString;
                } else
                    try {
                        this.localName = URLEncoder.encode(this.label, "UTF-8");
                        this.uri = EREL.NS + URLEncoder.encode(this.label, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        System.out.println(String.format("Category error: unsupported label encoding (label -> %1s).", label));
                        throw e;
                    }
            }
        }

        public boolean addSameAs(String resource) throws Exception {
            if (DB.Resources.getResource(resource) != null)
                return sameAS.add(DB.Resources.getResource(resource));
            else
                return sameAS.add(DB.Resources.addResource(resource));
        }

        public String getLabel() {
            return label;
        }

        public String getLocalName() {
            return localName;
        }

        public String getUri() {
            return uri;
        }

        public Set<Resource> listSameAS() {
            return sameAS;
        }
    }

    public static class Entity {

        private String label = null;
        private String localName = null;
        private String uri = null;
        private double score = 0;
        private DB.Category category = null;
        private Set<DB.Resource> sameAS = new HashSet<>();

        private Entity() {
            super();
        }

        public Entity(String label, String category) throws Exception {
            super();
            if (label == null || label.equals("")) {
                System.out.println(String.format("Entity error: invalid label (label -> %1s).", label));
                throw new Exception();
            } else {
                this.label = label.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");
                String uriString = Config.DATA_NS + this.label;
                if ((new UrlValidator()).isValid(uriString)) {
                    this.localName = this.label;
                    this.uri = uriString;
                } else
                    try {
                        this.localName = URLEncoder.encode(this.label, "UTF-8");
                        this.uri = Config.DATA_NS + URLEncoder.encode(this.label, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        System.out.println(String.format("Entity error: unsupported label encoding (label -> %1s).", label));
                        throw e;
                    }
            }
            this.category = DB.Categories.addCategory(category);
        }

        public String getLabel() {
            return label;
        }

        public String getLocalName() {
            return localName;
        }

        public String getUri() {
            return uri;
        }

        public Double getScore() {
            return score;
        }

        public DB.Category getCategory() {
            return category;
        }

        public Set<DB.Resource> listSameAS() {
            return sameAS;
        }

        public boolean addSameAs(String resource) throws Exception {
            if (resource != null)
                if (DB.Resources.getResource(resource) != null)
                    return sameAS.add(DB.Resources.getResource(resource));
                else
                    return sameAS.add(DB.Resources.addResource(resource));
            else
                return false;
        }
    }

    public static class Property {

        private String label = null;
        private String localName = null;
        private String uri = null;
        private String inverseURI = null;
        private double score = 0;
        private List<DB.Resource> sameAS = new ArrayList<>();

        private Property() {
            super();
        }

        public Property(String label, double score) throws Exception {
            super();
            if (!label.equals("")) {
                this.label = label.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");
                String uriString = Config.DATA_NS + this.label;
                if ((new UrlValidator()).isValid(uriString)) {
                    this.localName = this.label;
                    this.uri = uriString;
                    this.inverseURI = Config.DATA_NS + "inverseOf_" + this.label;
                } else
                    try {
                        this.localName = URLEncoder.encode(this.label, "UTF-8");
                        this.uri = Config.DATA_NS + "inverseOf_" + this.localName;
                    } catch (UnsupportedEncodingException e) {
                        System.out.println(String.format("Entity error: unsupported label encoding (label -> %1s).", label));
                        throw e;
                    }
                this.score = score;
            } else
                throw new Exception();
        }

        public boolean addSameAs(DB.Resource resource) throws Exception {
            if (resource != null)
                return sameAS.add(resource);
            else
                throw new Exception();
        }

        public String getLabel() {
            return label;
        }

        public String getLocalName() {
            return localName;
        }

        public String getUri() {
            return uri;
        }

        public String getUriOfInverse() {
            return inverseURI;
        }

        public Double getScore() {
            return score;
        }

        public List<DB.Resource> getSameAS() {
            return sameAS;
        }
    }

    public static class EntityPair {

        private final String localName = "id-" + UUID.randomUUID().toString();
        private final String uri = Config.DATA_NS + localName;
        private DB.Entity entity1 = null;
        private DB.Entity entity2 = null;
        private List<Path> rank = new ArrayList<>();

        private EntityPair() {
            super();
        }

        public EntityPair(String entity1, String entity2) throws Exception {
            super();
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
            return entity1.getLabel() + "-" + entity2.getLabel();
        }

        public String getLocalName() {
            return localName;
        }

        public String getUri() {
            return uri;
        }

        public DB.Entity getEntity1() {
            return entity1;
        }

        public DB.Entity getEntity2() {
            return entity2;
        }

        public List<Path> listPaths() {
            return rank;
        }

        public boolean addPath(Path path) throws Exception {
            if (path != null) {
                path.entityPair = this;
                return rank.add(path);
            } else
                throw new Exception();
        }

        public List<Path> getRank() {
            return rank;
        }

    }

    public static class Path {

        private EntityPair entityPair = null;
        private String localName = "id-" + UUID.randomUUID().toString();
        private String uri = Config.DATA_NS + localName;
        private int rankPosition = 0;
        private double score = 0;
        private String expression = null;
        private List<PathElement> elements = new ArrayList<>();

        private Path() {
        }

        public Path(String entity1, String entity2, int position, double score, String expression) throws Exception {
            this.rankPosition = position;
            this.score = score;

            if (expression.equals("")) {
                System.out.println(String.format("Path error: missing expression."));
                throw new Exception();
            } else {
                this.expression = expression;
                addPathElemente(expression);
            }

            try {
                this.entityPair = DB.EntityPairs.getEntityPair(entity1, entity2);
                this.entityPair.addPath(this);
            } catch (Exception e) {
                System.out.println(String.format("Path error: undefined entity pair (entity1 -> %1s, entity2 -> %1s).", entity1, entity2));
                throw e;
            }
        }

        public EntityPair getEntityPair() {
            return entityPair;
        }

        public String getLocalName() {
            return localName;
        }

        public String getUri() {
            return uri;
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

        public List<PathElement> listElements() {
            return elements;
        }

        private boolean addPathElemente(String expression) {
            String[] elements = expression.split(" ");
            int count = 0;
            for (String element : elements) {
                PathElement pe = null;
                try {
                    if (count % 2 == 0)
                        pe = new EntityElement(element, 0);
                    else if (element.contains("@"))
                        pe = new PropertyElement(element.replaceAll("@", ""), true);
                    else
                        pe = new PropertyElement(element.replaceAll("@", ""), false);

                    this.elements.add(pe);
                    pe.path = this;
                } catch (Exception e) {
                }
                count++;
            }
            return true;
        }

    }

    public static abstract class PathElement {

        private Path path = null;
        private String localName = "id-" + UUID.randomUUID().toString();
        private String uri = Config.DATA_NS + this.localName;

        public Path getPath() {
            return path;
        }

        public abstract String getLabel();

        public String getLocalName() {
            return localName;
        }

        public String getUri() {
            return uri;
        }

        public abstract double getScore();

        public abstract void setScore(double score);

        public abstract String getReferencedElementURI();
    }

    public static class EntityElement extends PathElement {

        private DB.Entity entity = null;
        private double score = 0;

        private EntityElement() {

        }

        public EntityElement(String entity, double score) throws Exception {
            this.entity = DB.Entities.getEntity(entity);
            if (this.entity == null) {
                System.out.println(String.format("Entity element error: undefined entity (entity -> %1s).", entity));
                throw new Exception();
            }

            this.score = score;
        }

        @Override
        public String getLabel() {
            return entity.getLabel();
        }

        @Override
        public double getScore() {
            return score;
        }

        @Override
        public void setScore(double score) {
            this.score = score;
        }

        @Override
        public String getReferencedElementURI() {
            return entity.getUri();
        }

        public DB.Entity getEntity() {
            return entity;
        }
    }

    public static class PropertyElement extends PathElement {

        private DB.Property property = null;
        private boolean inverse = false;

        private PropertyElement() {
        }

        public PropertyElement(String property, boolean inverse) throws Exception {
            this.property = DB.Properties.getPropety(property);
            this.inverse = inverse;
            if (this.property == null) {
                System.out.println(String.format("Property element error: undefined property (property -> %1s).", property));
                throw new Exception();
            }
        }

        @Override
        public String getLabel() {
            return property.getLabel();
        }

        @Override
        public double getScore() {
            return property.getScore();
        }

        @Override
        public void setScore(double score) {
            property.score = score;
        }

        @Override
        public String getReferencedElementURI() {
            if (inverse)
                return property.getUriOfInverse();
            else
                return property.getUri();
        }

        public DB.Property getProperty() {
            return property;
        }
    }
}
