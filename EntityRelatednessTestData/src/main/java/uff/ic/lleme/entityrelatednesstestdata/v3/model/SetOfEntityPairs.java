package uff.ic.lleme.entityrelatednesstestdata.v3.model;

import java.util.HashMap;
import java.util.Map;

public class SetOfEntityPairs {

    private static SetOfEntityPairs setOfEntityPairs = null;
    private final Map<String, EntityPair> entityPairs = new HashMap<>();

    private SetOfEntityPairs() {

    }

    public static SetOfEntityPairs getInstance() {
        if (setOfEntityPairs == null)
            setOfEntityPairs = new SetOfEntityPairs();
        return setOfEntityPairs;
    }

    public EntityPair addEntityPair(String entity1, String entity2) throws Exception {
        if (entityPairs.containsKey(entity1 + "-" + entity2))
            return entityPairs.get(entity1 + "-" + entity2);
        else {
            EntityPair entityPair = new EntityPair(entity1, entity2);
            entityPairs.put(entity1 + "-" + entity2, entityPair);
            return entityPair;
        }
    }

    public EntityPair getEntityPair(String entity1, String entity2) {
        return entityPairs.get(entity1 + "-" + entity2);
    }

}
