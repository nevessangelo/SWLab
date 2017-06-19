package uff.ic.swlab.dataset.ertd.v3.util;

public class Pair {

    public String label = null;
    public String type = null;
    public String entity1 = null;
    public String entity2 = null;

    public Pair(String label, String type, String entity1, String entity2) {
        //System.out.println("\\u" + Integer.toHexString(id.charAt(1) | 0x10000).substring(1));
        if (label != null)
            this.label = label.replace('\u00a0', ' ').trim();
        if (type != null)
            this.type = type.replace('\u00a0', ' ').trim();
        if (entity1 != null)
            this.entity1 = entity1.replace('\u00a0', ' ').trim();
        if (entity2 != null)
            this.entity2 = entity2.replace('\u00a0', ' ').trim();
    }

}
