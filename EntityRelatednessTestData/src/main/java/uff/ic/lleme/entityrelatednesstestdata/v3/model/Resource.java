package uff.ic.lleme.entityrelatednesstestdata.v3.model;

import org.apache.commons.validator.routines.UrlValidator;

public class Resource {

    private String uri = null;

    private Resource() {

    }

    public Resource(String uriString) throws Exception {
        if ((new UrlValidator()).isValid(uriString))
            this.uri = uriString;
        else
            throw new Exception();

    }

    private String getURI() {
        return uri;
    }
}
