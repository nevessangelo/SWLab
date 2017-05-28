package uff.ic.lleme.entityrelatednesstestdata.v3.model;

import java.util.HashMap;
import java.util.Map;

public class SetOfProperties {

    private static SetOfProperties setOfProperties = null;
    private final Map<String, Property> PROPERTIES = new HashMap<>();

    private SetOfProperties() {

    }

    public static SetOfProperties getInstance() {
        if (setOfProperties == null)
            setOfProperties = new SetOfProperties();
        return setOfProperties;
    }

    public Property addProperty(String label, double score) throws Exception {
        if (PROPERTIES.containsKey(label))
            return PROPERTIES.get(label);
        else {
            Property property = new Property(label, score);
            PROPERTIES.put(property.getLabel(), property);
            return property;
        }
    }

    public Property getPropety(String label) {
        return PROPERTIES.get(label);
    }
}
