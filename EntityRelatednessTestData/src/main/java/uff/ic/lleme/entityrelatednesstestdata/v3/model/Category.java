package uff.ic.lleme.entityrelatednesstestdata.v3.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.validator.routines.UrlValidator;
import uff.ic.lleme.entityrelatednesstestdata.v3.vocabulary.EREL;

public class Category {

    private String label = null;
    private String localName = null;
    private String uri = null;
    private Set<Resource> sameAS = new HashSet<>();

    private Category() {

    }

    public Category(String label) throws Exception {
        if (label == null || label.equals("")) {
            System.out.println(String.format("Category error: invalid label (label -> %1s).", label));
            throw new Exception();
        } else {
            this.label = label.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");
            String uriString = EREL.NS + this.label;
            if ((new UrlValidator()).isValid(uriString)) {
                this.localName = this.label;
                this.uri = uriString;
            } else
                try {
                    this.localName = URLEncoder.encode(this.label, "UTF-8");
                    this.uri = EREL.NS + URLEncoder.encode(this.label, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    System.out.println(String.format("Category error: unsupported label encoding (label -> %1s).", label));
                    throw new Exception();
                }
        }
    }

    public boolean addSameAs(String resource) throws Exception {
        if (uff.ic.lleme.entityrelatednesstestdata.v3.model.DB.Resources.getResource(resource) != null)
            return sameAS.add(uff.ic.lleme.entityrelatednesstestdata.v3.model.DB.Resources.getResource(resource));
        else
            return sameAS.add(uff.ic.lleme.entityrelatednesstestdata.v3.model.DB.Resources.addResource(resource));
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

    public Set<Resource> getSameAS() {
        return sameAS;
    }

}
