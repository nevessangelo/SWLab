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
public class ClassPartition {
    
    private int id;
    private String name;
    private int frequen;
    private String name_dataset;
    private String type;
    
    public ClassPartition(){
        
    }
    
    public ClassPartition(int id, String name, int frequen, String name_dataset, String type){
        this.id = id;
        this.name = name;
        this.frequen = frequen;
        this.name_dataset = name_dataset;
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
