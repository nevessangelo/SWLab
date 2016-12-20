/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.object;

/**
 *
 * @author angelo
 */
public class Features {
    
    private int id_feature;
    private String name_dataset;
    private String feature;
    private String type_feature;
    private int frequen;
    private int DatasetSize;
    
    public Features(){
        
    }
    
    public Features (int id_feature, String name_dataset, String feature, String type_feature, int frequen, int DatasetSize){
        this.id_feature = id_feature;
        this.name_dataset = name_dataset;
        this.feature = feature;
        this.type_feature = type_feature;
        this.frequen = frequen;
        this.DatasetSize = DatasetSize;
    }
    
     public int getDatasetSize() {
        return DatasetSize;
    }

    /**
     * @param id_feature the id_feature to set
     */
    public void setDatasetSize(int DatasetSize) {
        this.DatasetSize = DatasetSize;
    }

    /**
     * @return the id_feature
     */
    public int getId_feature() {
        return id_feature;
    }

    /**
     * @param id_feature the id_feature to set
     */
    public void setId_feature(int id_feature) {
        this.id_feature = id_feature;
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
     * @return the type_feature
     */
    public String getType_feature() {
        return type_feature;
    }

    /**
     * @param type_feature the type_feature to set
     */
    public void setType_feature(String type_feature) {
        this.type_feature = type_feature;
    }

    /**
     * @return the frequen
     */
    public int getFrequen() {
        return frequen;
    }

    /**
     * @param frequen the frequen to set
     */
    public void setFrequen(int frequen) {
        this.frequen = frequen;
    }
    
}
