package uff.ic.lleme.entityrelatednesstestdata.v3.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.validator.routines.UrlValidator;
import uff.ic.lleme.entityrelatednesstestdata.v3.Config;

public class Entity {

    private String label = null;
    private String localName = null;
    private String uri = null;
    private double score = 0;
    private Category category = null;
    private List<Resource> sameAS = new ArrayList<>();

    private Entity() {

    }

    public Entity(String label, String category) throws Exception {
        if (label == null || label.equals("")) {
            System.out.println(String.format("Error: invalid entity label. (entity -> %1s)", label));
            throw new Exception();
        } else {
            this.label = label.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");
            String uriString = Config.DATA_NS + this.label;
            if ((new UrlValidator()).isValid(uriString)) {
                this.uri = uriString;
                this.localName = this.label;
            } else
                try {
                    this.uri = Config.DATA_NS + URLEncoder.encode(this.label, "UTF-8");
                    this.localName = URLEncoder.encode(this.label, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    System.out.println(String.format("Error: unsupported encoding entity label. (entity -> %1s)", label));
                    throw new Exception();
                }
        }

        if (category == null) {
            System.out.println(String.format("Error: null category."));
            throw new Exception();
        } else if (SetOfCategories.getInstance().getCategory(category) == null) {
            System.out.println(String.format("Error: invalid category."));
            throw new Exception();
        } else
            this.category = SetOfCategories.getInstance().getCategory(category);

    }

    public boolean addSameAs(String resource) throws Exception {
        if (resource == null) {
            System.out.println(String.format("Error: null sameAs resource. (entity -> %1s)", label));
            throw new Exception();
        } else if (SetOfResources.getInstance().getResource(resource) == null) {
            System.out.println(String.format("Error: invalid category."));
            throw new Exception();
        } else
            return sameAS.add(SetOfResources.getInstance().getResource(resource));
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

    public Double getScore() {
        return score;
    }

    public List<Resource> getSameAS() {
        return sameAS;
    }

}
