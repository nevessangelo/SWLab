package uff.ic.lleme.entityrelatednesstestdata.v3.model;

import java.util.HashMap;
import java.util.Map;

public class SetOfEntities {

    private static SetOfEntities setOfEntities = null;
    private Map<String, Entity> entities = new HashMap<>();

    private SetOfEntities() {

    }

    public static SetOfEntities getInstance() {
        if (setOfEntities == null)
            setOfEntities = new SetOfEntities();
        return setOfEntities;
    }

    public Entity addEntity(String label, String category) throws Exception {
        if (entities.containsKey(label))
            return entities.get(label);
        else {
            Entity entity = new Entity(label, category);
            entities.put(entity.getLabel(), entity);
            return entity;
        }
    }

    public Entity getEntity(String label) {
        return entities.get(label);
    }
}
