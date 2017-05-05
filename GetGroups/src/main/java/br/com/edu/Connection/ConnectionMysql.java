/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Connection;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author angelo
 */
public class ConnectionMysql {
    public static Connection Conectar() throws ClassNotFoundException{
        Connection connection = null;
        String servename = "localhost";
        String mydatabase = "Features_Completo2";
        String url = "jdbc:mysql://" + servename + "/" + mydatabase;
        String username = "root";
        String password = "123";
        
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (SQLException e){
            System.out.println("Problemas na conexao com Banco de Dados." + e);
            return null;
        }
        
    }
    
}
