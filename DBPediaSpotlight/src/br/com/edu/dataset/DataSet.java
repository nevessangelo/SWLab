/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.dataset;


/**
 *
 * @author angelo
 */
public class DataSet {
    
    private String nome;
    private String entrada;
    private Topicos topicos; 

    public DataSet(String nome, String entrada, Topicos topicos){
        this.nome = nome;
        this.entrada = entrada;
        this.topicos = topicos;
    }
    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the entrada
     */
    public String getEntrada() {
        return entrada;
    }

    /**
     * @param entrada the entrada to set
     */
    public void setEntrada(String entrada) {
        this.entrada = entrada;
    }

    /**
     * @return the topicos
     */
    public Topicos getTopicos() {
        return topicos;
    }

    /**
     * @param topicos the topicos to set
     */
    public void setTopicos(Topicos topicos) {
        this.topicos = topicos;
    }
    
    
    
}
