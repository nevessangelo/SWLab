/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Objects;

/**
 *
 * @author angelo
 */
public class Atributos {
    
    private int id;
    private String name_dataset;
    private String feature;
    private double frequen;
    private String type;
    
    public Atributos(){
        
    }
    
    public Atributos(int id, String name_dataset, String feature, double frequen, String type){
        this.id = id;
        this.name_dataset = name_dataset;
        this.feature = feature;
        this.frequen = frequen;
        this.type = type;
        
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name_dataset
     */
    public String getName_dataset() {
        return name_dataset;
    }

    /**
     * @param name_dataset the name_dataset to set
     */
    public void setName_dataset(String name_dataset) {
        this.name_dataset = name_dataset;
    }

    /**
     * @return the feature
     */
    public String getFeature() {
        return feature;
    }

    /**
     * @param feature the feature to set
     */
    public void setFeature(String feature) {
        this.feature = feature;
    }

    /**
     * @return the frequen
     */
    public double getFrequen() {
        return frequen;
    }

    /**
     * @param frequen the frequen to set
     */
    public void setFrequen(double frequen) {
        this.frequen = frequen;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    
    
    
    
}
