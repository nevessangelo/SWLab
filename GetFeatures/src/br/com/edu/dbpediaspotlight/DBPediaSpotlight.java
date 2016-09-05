/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.dbpediaspotlight;

import br.com.edu.Connection.ConnectionMySql;
import java.sql.Connection;
import java.util.List;


/**
 * @author angelo
 */
public class DBPediaSpotlight {

    /**
     * @param args the command line arguments
     */
    
    public List<String> getEntity(String text) throws Exception{
        db c = new db();
        c.configiration(0.0, 0, "non", "CoOccurrenceBasedSelector", "Default", "yes");
        c.evaluate(text);
        System.out.println("resource : " + c.getResu());
        return c.getResu();
        
    }
   
        
        
       
       
//        ConnectionMySql conexao = new ConnectionMySql();
//        Connection resultado = conexao.Conectar();
//        System.out.println(resultado);
    
}