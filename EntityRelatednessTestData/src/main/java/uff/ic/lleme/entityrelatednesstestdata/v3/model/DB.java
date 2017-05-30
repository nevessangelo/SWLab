package uff.ic.lleme.entityrelatednesstestdata.v3.model;

import java.util.HashMap;
import java.util.Map;

public class DB {

    public static class Resources {

        private static Resources setOfResources = null;
        private final Map<String, _Resource> RESOURCES = new HashMap<>();

        private Resources() {
            super();
        }

        private static Resources getInstance() {
            if (setOfResources == null)
                setOfResources = new Resources();
            return setOfResources;
        }

        public static _Resource addResource(String uriString) throws Exception {
            if (getInstance().RESOURCES.containsKey(uriString))
                return getInstance().RESOURCES.get(uriString);
            else {
                _Resource resource = new _Resource(uriString);
                getInstance().RESOURCES.put(resource.getURI(), resource);
                return resource;
            }
        }

        public static _Resource getResource(String uriString) {
            return getInstance().RESOURCES.get(uriString);
        }
    }

    public static class Categories {

        private static Categories setOfCategories = null;
        private final Map<String, _Category> CATEGORIES = new HashMap<>();

        private Categories() {
            super();
        }

        private static Categories getInstance() {
            if (setOfCategories == null)
                setOfCategories = new Categories();
            return setOfCategories;
        }

        public static _Category addCategory(String label) throws Exception {
            label = label.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");

            if (getInstance().CATEGORIES.containsKey(label))
                return getInstance().CATEGORIES.get(label);
            else {
                _Category category = new _Category(label);
                getInstance().CATEGORIES.put(category.getLabel(), category);
                return category;
            }
        }

        public static _Category getCategory(String label) {
            label = label.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");
            return getInstance().CATEGORIES.get(label);
        }
    }

    public static class Entities {

        private static Entities setOfEntities = null;
        private final Map<String, _Entity> ENTITIES = new HashMap<>();

        private Entities() {
            super();
        }

        private static Entities getInstance() {
            if (setOfEntities == null)
                setOfEntities = new Entities();
            return setOfEntities;
        }

        public static _Entity addEntity(String label, String category) throws Exception {
            label = label.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");

            if (getInstance().ENTITIES.containsKey(label))
                return getInstance().ENTITIES.get(label);
            else {
                _Entity entity = new _Entity(label, category);
                getInstance().ENTITIES.put(entity.getLabel(), entity);
                return entity;
            }
        }

        public static _Entity getEntity(String label) {
            return getInstance().ENTITIES.get(label);
        }
    }

    public static class Properties {

        private static Properties setOfProperties = null;
        private final Map<String, _Property> PROPERTIES = new HashMap<>();

        private Properties() {
            super();
        }

        private static Properties getInstance() {
            if (setOfProperties == null)
                setOfProperties = new Properties();
            return setOfProperties;
        }

        public static _Property addProperty(String label, double score) throws Exception {
            label = label.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");

            if (getInstance().PROPERTIES.containsKey(label))
                return getInstance().PROPERTIES.get(label);
            else {
                _Property property = new _Property(label, score);
                getInstance().PROPERTIES.put(property.getLabel(), property);
                return property;
            }
        }

        public static _Property getPropety(String label) {
            label = label.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");
            return getInstance().PROPERTIES.get(label);
        }
    }

    public static class EntityPairs {

        private static EntityPairs setOfEntityPairs = null;
        private final Map<String, _EntityPair> ENTITY_PAIRS = new HashMap<>();

        private EntityPairs() {
            super();
        }

        private static EntityPairs getInstance() {
            if (setOfEntityPairs == null)
                setOfEntityPairs = new EntityPairs();
            return setOfEntityPairs;
        }

        public static _EntityPair addEntityPair(String entity1, String entity2) throws Exception {
            entity1 = entity1.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");
            entity2 = entity2.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");

            if (getInstance().ENTITY_PAIRS.containsKey(entity1 + "-" + entity2))
                return getInstance().ENTITY_PAIRS.get(entity1 + "-" + entity2);
            else {
                _EntityPair entityPair = new _EntityPair(entity1, entity2);
                getInstance().ENTITY_PAIRS.put(entity1 + "-" + entity2, entityPair);
                return entityPair;
            }
        }

        public static _EntityPair getEntityPair(String entity1, String entity2) {
            entity1 = entity1.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");
            entity2 = entity2.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");

            return getInstance().ENTITY_PAIRS.get(entity1 + "-" + entity2);
        }
    }
}
