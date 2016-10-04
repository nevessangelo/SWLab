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
public class ClassObject {
    private String name;
    private int frequen;
    private String dataset;
    
    public ClassObject(){
        
    }
    
    public ClassObject(String name, int frequen, String dataset){
        this.name = name;
        this.frequen = frequen;
        this.dataset = dataset;
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
    
     public String getDataset() {
        return dataset;
    }

    /**
     * @param dataset the frequen to set
     */
    public void setDataset(String dataset) {
        this.dataset = dataset;
    }
    
    
}
