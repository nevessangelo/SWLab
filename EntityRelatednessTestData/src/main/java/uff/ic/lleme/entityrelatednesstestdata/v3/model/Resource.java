package uff.ic.lleme.entityrelatednesstestdata.v3.model;

import org.apache.commons.validator.routines.UrlValidator;

public class Resource {

    private String uri = null;

    private Resource() {

    }

    public Resource(String uriString) throws Exception {
        if (!(new UrlValidator()).isValid(uriString)) {
            System.out.println(String.format("Resource error: invalid uriString. (uri -> %1s)", uriString));
            throw new Exception();
        } else
            this.uri = uriString;

    }

    public String getURI() {
        return uri;
    }
}
