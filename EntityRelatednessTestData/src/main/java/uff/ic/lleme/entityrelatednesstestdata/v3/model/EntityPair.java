package uff.ic.lleme.entityrelatednesstestdata.v3.model;

import java.util.ArrayList;
import java.util.List;

public class EntityPair {

    private Entity entity1 = null;
    private Entity entity2 = null;
    private List<Path> rank = new ArrayList<>();

    private EntityPair() {

    }

    public EntityPair(Entity entity1, Entity entity2) throws Exception {
        if (entity1 != null && entity2 != null && entity1 != entity2) {
            this.entity1 = entity1;
            this.entity2 = entity2;
        } else
            throw new Exception();
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
