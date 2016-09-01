/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author angelo
 */
public class ConnectionMySql {

    Connection connection = null;

    public Connection Conectar() throws ClassNotFoundException {
        String serverName = "localhost";    //caminho do servidor do BD
        String mydatabase = "Base_Datasets";        //nome do seu banco de dados
        String url = "jdbc:mysql://" + serverName + "/" + mydatabase;
        String username = "root";        //nome de um usuário de seu BD      
        String password = "123";      //sua senha de acesso

        try {
            Class.forName("com.mysql.jdbc.Driver");
            return connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("Problemas na conexao com o banco de dados." + e);
            return null;
        }

    }
}