/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author angelo
 */
public class Methods {
    
    public static ArrayList<String> Datasets() throws SQLException, ClassNotFoundException{
        ArrayList<String> name = new ArrayList<>();
        String dataset;
        Connection conn = ConnectionMySql.Conectar();
        if (conn != null) {
            java.sql.Statement stmt = conn.createStatement();
            String query = "SELECT DISTINCT nome_dataset FROM `Features` WHERE dataset_size = 0";
            java.sql.ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                dataset = rs.getString("nome_dataset");
                name.add(dataset);
            }
        }
        conn.close();
        return name;
    }
    
    public static void Update(int triples, String nome_dataset) throws ClassNotFoundException, SQLException{
        Connection conn = ConnectionMySql.Conectar();
        if (conn != null) {
            String query = "UPDATE Features set dataset_size = ? WHERE name_dataset = ?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, triples);
            preparedStmt.setString(2, nome_dataset);
            preparedStmt.executeUpdate();
        }
        conn.close();
        
    }
    
}
