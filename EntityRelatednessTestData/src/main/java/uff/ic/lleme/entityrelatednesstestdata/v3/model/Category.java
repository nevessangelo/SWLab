package uff.ic.lleme.entityrelatednesstestdata.v3.model;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.validator.routines.UrlValidator;
import uff.ic.lleme.entityrelatednesstestdata.v3.Config;

public class Category {

    private String label = null;
    private String localName = null;
    private String uri = null;
    private List<Resource> sameAS = new ArrayList<>();

    private Category() {

    }

    public Category(String label) throws Exception {
        if (!label.equals("")) {
            this.label = label.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");
            String uriString = Config.DATA_NS + this.label;
            if ((new UrlValidator()).isValid(uriString)) {
                this.uri = uriString;
                this.localName = this.label;
            } else {
                this.uri = Config.DATA_NS + URLEncoder.encode(this.label, "UTF-8");
                this.localName = URLEncoder.encode(this.label, "UTF-8");
            }
        } else
            throw new Exception();
    }

    public boolean addSameAs(Resource resource) throws Exception {
        if (resource != null)
            return sameAS.add(resource);
        else
            throw new Exception();
    }

    public String getLabel() {
        return label;
    }

    public String getLocalName() {
        return localName;
    }

    public String getUri() {
        return uri;
    }

    public List<Resource> getSameAS() {
        return sameAS;
    }

}
