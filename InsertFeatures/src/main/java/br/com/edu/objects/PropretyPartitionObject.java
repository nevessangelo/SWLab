/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.objects;

/**
 *
 * @author angelo
 */
public class PropretyPartitionObject {
    private String name;
    private String dataset;
    private int frequen;
    
    public PropretyPartitionObject(){
        
    }
    
    public PropretyPartitionObject(String name, String dataset, int frequen){
        this.name = name;
        this.dataset = dataset;
        this.frequen = frequen;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the dataset
     */
    public String getDataset() {
        return dataset;
    }

    /**
     * @param dataset the dataset to set
     */
    public void setDataset(String dataset) {
        this.dataset = dataset;
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
