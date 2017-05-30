package uff.ic.lleme.entityrelatednesstestdata.v3.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DB {

    public static class Resources {

        private static Resources setOfResources = null;
        private final Map<String, Resource_> RESOURCES = new HashMap<>();

        private Resources() {
            super();
        }

        private static Resources getInstance() {
            if (setOfResources == null)
                setOfResources = new Resources();
            return setOfResources;
        }

        public static Resource_ addResource(String uriString) throws Exception {
            if (getInstance().RESOURCES.containsKey(uriString))
                return getInstance().RESOURCES.get(uriString);
            else {
                Resource_ resource = new Resource_(uriString);
                getInstance().RESOURCES.put(resource.getURI(), resource);
                return resource;
            }
        }

        public static Resource_ getResource(String uriString) {
            return getInstance().RESOURCES.get(uriString);
        }
    }

    public static class Categories {

        private static Categories setOfCategories = null;
        private final Map<String, Category_> CATEGORIES = new HashMap<>();

        private Categories() {
            super();
        }

        private static Categories getInstance() {
            if (setOfCategories == null)
                setOfCategories = new Categories();
            return setOfCategories;
        }

        public static Category_ addCategory(String label) throws Exception {
            label = label.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");

            if (getInstance().CATEGORIES.containsKey(label))
                return getInstance().CATEGORIES.get(label);
            else {
                Category_ category = new Category_(label);
                getInstance().CATEGORIES.put(category.getLabel(), category);
                return category;
            }
        }

        public static Category_ getCategory(String label) {
            label = label.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");
            return getInstance().CATEGORIES.get(label);
        }

        public static Collection<Category_> listCategories() {
            return getInstance().CATEGORIES.values();
        }
    }

    public static class Entities {

        private static Entities setOfEntities = null;
        private final Map<String, Entity_> ENTITIES = new HashMap<>();

        private Entities() {
            super();
        }

        private static Entities getInstance() {
            if (setOfEntities == null)
                setOfEntities = new Entities();
            return setOfEntities;
        }

        public static Entity_ addEntity(String label, String category) throws Exception {
            label = label.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");

            if (getInstance().ENTITIES.containsKey(label))
                return getInstance().ENTITIES.get(label);
            else {
                Entity_ entity = new Entity_(label, category);
                getInstance().ENTITIES.put(entity.getLabel(), entity);
                return entity;
            }
        }

        public static Entity_ getEntity(String label) {
            label = label.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");

            return getInstance().ENTITIES.get(label);
        }

        public static Collection<Entity_> listEntities() {
            return getInstance().ENTITIES.values();
        }
    }

    public static class Properties {

        private static Properties setOfProperties = null;
        private final Map<String, Property_> PROPERTIES = new HashMap<>();

        private Properties() {
            super();
        }

        private static Properties getInstance() {
            if (setOfProperties == null)
                setOfProperties = new Properties();
            return setOfProperties;
        }

        public static Property_ addProperty(String label, double score) throws Exception {
            label = label.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");

            if (getInstance().PROPERTIES.containsKey(label))
                return getInstance().PROPERTIES.get(label);
            else {
                Property_ property = new Property_(label, score);
                getInstance().PROPERTIES.put(property.getLabel(), property);
                return property;
            }
        }

        public static Property_ getPropety(String label) {
            label = label.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");
            return getInstance().PROPERTIES.get(label);
        }
    }

    public static class EntityPairs {

        private static EntityPairs setOfEntityPairs = null;
        private final Map<String, EntityPair_> ENTITY_PAIRS = new HashMap<>();

        private EntityPairs() {
            super();
        }

        private static EntityPairs getInstance() {
            if (setOfEntityPairs == null)
                setOfEntityPairs = new EntityPairs();
            return setOfEntityPairs;
        }

        public static EntityPair_ addEntityPair(String entity1, String entity2) throws Exception {
            entity1 = entity1.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");
            entity2 = entity2.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");

            if (getInstance().ENTITY_PAIRS.containsKey(entity1 + "-" + entity2))
                return getInstance().ENTITY_PAIRS.get(entity1 + "-" + entity2);
            else {
                EntityPair_ entityPair = new EntityPair_(entity1, entity2);
                getInstance().ENTITY_PAIRS.put(entity1 + "-" + entity2, entityPair);
                return entityPair;
            }
        }

        public static EntityPair_ getEntityPair(String entity1, String entity2) {
            entity1 = entity1.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");
            entity2 = entity2.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");

            return getInstance().ENTITY_PAIRS.get(entity1 + "-" + entity2);
        }

        public static Collection<EntityPair_> listEntityPairs() {
            return getInstance().ENTITY_PAIRS.values();
        }
    }
}
