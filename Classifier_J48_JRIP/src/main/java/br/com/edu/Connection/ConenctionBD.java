/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Connection;

/**
 *
 * @author angelo
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConenctionBD {
    
     public static Connection Conectar() throws ClassNotFoundException {
        Connection connection = null;
        String serverName = "localhost";    
        String mydatabase = "Features_Completo2";        
        String url = "jdbc:mysql://" + serverName + "/" + mydatabase;
        String username = "SemanticWeb";        
        String password = "123";      

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            return connection; 
        } catch (SQLException e) {
            System.out.println("Problemas na conexao com o banco de dados." + e);
            return null;
        }

    }
    
}

