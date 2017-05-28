package uff.ic.lleme.entityrelatednesstestdata.v3.model;

import java.util.HashMap;
import java.util.Map;

public class SetOfEntities {

    private static SetOfEntities setOfEntities = null;
    private final Map<String, Entity> ENTITIES = new HashMap<>();

    private SetOfEntities() {

    }

    public static SetOfEntities getInstance() {
        if (setOfEntities == null)
            setOfEntities = new SetOfEntities();
        return setOfEntities;
    }

    public Entity addEntity(String label, String category) throws Exception {
        if (ENTITIES.containsKey(label))
            return ENTITIES.get(label);
        else {
            Entity entity = new Entity(label, category);
            ENTITIES.put(entity.getLabel(), entity);
            return entity;
        }
    }

    public Entity getEntity(String label) {
        return ENTITIES.get(label);
    }
}
