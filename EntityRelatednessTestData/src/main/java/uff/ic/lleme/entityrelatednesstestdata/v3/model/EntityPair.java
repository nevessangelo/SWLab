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
