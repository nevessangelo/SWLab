package uff.ic.lleme.entityrelatednesstestdata.v3.model;

import java.util.HashMap;
import java.util.Map;

public class SetOfResources {

    private static SetOfResources setOfResources = null;
    private Map<String, Resource> resources = new HashMap<>();

    private SetOfResources() {

    }

    public static SetOfResources getInstance() {
        if (setOfResources == null)
            setOfResources = new SetOfResources();
        return setOfResources;
    }

    public Resource addResource(String uriString) throws Exception {
        if (resources.containsKey(uriString))
            return resources.get(uriString);
        else {
            Resource resource = new Resource(uriString);
            resources.put(resource.getURI(), resource);
            return resource;
        }
    }

    public Resource getResource(String label) {
        return resources.get(label);
    }
}
