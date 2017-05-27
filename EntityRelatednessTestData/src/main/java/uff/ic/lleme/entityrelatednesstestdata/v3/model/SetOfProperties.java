package uff.ic.lleme.entityrelatednesstestdata.v3.model;

import java.util.HashMap;
import java.util.Map;

public class SetOfProperties {

    private static SetOfProperties setOfProperties = null;
    private Map<String, Property> properties = new HashMap<>();

    private SetOfProperties() {

    }

    public static SetOfProperties getInstance() {
        if (setOfProperties == null)
            setOfProperties = new SetOfProperties();
        return setOfProperties;
    }

    public Property addProperty(Property property) {
        return properties.put(property.getLabel(), property);
    }

    public Property getPropety(String label) {
        return properties.get(label);
    }
}
