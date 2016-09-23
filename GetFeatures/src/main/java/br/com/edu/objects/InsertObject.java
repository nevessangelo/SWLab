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
public class InsertObject {
    
    private int id;
    private String name_dataset;
    private Entites entites;
    private String type;
    
    public InsertObject(){
        
    }
    
    public InsertObject(int id, String name_dataset, Entites entites, String type){
        this.id = id;
        this.name_dataset = name_dataset;
        this.entites = entites;
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
     * @return the entites
     */
    public Entites getEntites() {
        return entites;
    }

    /**
     * @param entites the entites to set
     */
    public void setEntites(Entites entites) {
        this.entites = entites;
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
