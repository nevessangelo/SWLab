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
        this.entity1 = SetOfEntityPairs.getInstance().addEntityPair(entity1, entity2);
        this.entity2 = SetOfEntityPairs.getInstance().addCategory(entity2);
    }

    public boolean add(Path path) throws Exception {
        if (path != null)
            return rank.add(path);
        else
            throw new Exception();
    }

    public List<Path> getRank() {
        return rank;
    }

    public Entity getEntity1() {
        return entity1;
    }

    public Entity getEntity2() {
        return entity2;
    }

}
