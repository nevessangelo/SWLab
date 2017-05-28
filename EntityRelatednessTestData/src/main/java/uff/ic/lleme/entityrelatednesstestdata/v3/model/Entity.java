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
            System.out.println(String.format("Entity error: invalid label (label -> %1s).", label));
            throw new Exception();
        } else {
            this.label = label.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "_");
            String uriString = Config.DATA_NS + this.label;
            if ((new UrlValidator()).isValid(uriString)) {
                this.localName = this.label;
                this.uri = uriString;
            } else
                try {
                    this.localName = URLEncoder.encode(this.label, "UTF-8");
                    this.uri = Config.DATA_NS + URLEncoder.encode(this.label, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    System.out.println(String.format("Entity error: unsupported label encoding (label -> %1s).", label));
                    throw new Exception();
                }
        }
        this.category = DB.Categories.addCategory(category);

    }

    public boolean addSameAs(String resource) throws Exception {
        if (DB.Resources.getResource(resource) != null)
            return sameAS.add(DB.Resources.getResource(resource));
        else
            return sameAS.add(DB.Resources.addResource(resource));
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
