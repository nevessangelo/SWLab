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
public class DataSet {
    
    private int id;
    private String nome_dataset;
    private String features;
    private String tipo;
    private int frequencia;

    public DataSet() {
        
    }
    
    public DataSet(int id, String nome_dataset, String features, String tipo, int frequencia){
        this.id = id;
        this.nome_dataset = nome_dataset;
        this.tipo = tipo;
        this.id = id;
        
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
     * @return the nome_dataset
     */
    public String getNome_dataset() {
        return nome_dataset;
    }

    /**
     * @param nome_dataset the nome_dataset to set
     */
    public void setNome_dataset(String nome_dataset) {
        this.nome_dataset = nome_dataset;
    }

    /**
     * @return the features
     */
    public String getFeatures() {
        return features;
    }

    /**
     * @param features the features to set
     */
    public void setFeatures(String features) {
        this.features = features;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the frequencia
     */
    public int getFrequencia() {
        return frequencia;
    }

    /**
     * @param frequencia the frequencia to set
     */
    public void setFrequencia(int frequencia) {
        this.frequencia = frequencia;
    }
    
    
    
    
    
    
}
