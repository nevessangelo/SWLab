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
public class Entites {
    
    private String name;
    private String entite;
    private int frequen;

    public Entites() {
    }
    
    public Entites(String name, String entite, int frequen){
        this.name = name;
        this.entite = entite;
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
     * @return the entite
     */
    public String getEntite() {
        return entite;
    }

    /**
     * @param entite the entite to set
     */
    public void setEntite(String entite) {
        this.entite = entite;
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
