/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author angelo
 */
public class MetodosMysql {
    
    public static int VerificaNome(String nome_dataset) throws ClassNotFoundException, SQLException{
        int cont = 0;
        Connection conn = ConnectionMysql.Conectar();
        if(conn != null){
            java.sql.Statement stmt = conn.createStatement();
            String query = "SELECT DISTINCT nome_dataset FROM Features WHERE nome_dataset = '" + nome_dataset +"'";
            java.sql.ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                cont++;
            }
        }
        conn.close();
        return cont;
    }
    
    public static void InsertGroups(String nome_dataset, String group) throws SQLException, ClassNotFoundException{
        Connection conn = ConnectionMysql.Conectar();
        if(conn != null){
            String query = "INSERT INTO Groups VALUES (?, ?, ?)";
            PreparedStatement stm = conn.prepareStatement(query);
            stm.setInt(1, 0);
            stm.setString(2, nome_dataset);
            stm.setString(3, group);
            stm.executeUpdate();
            
        }
        conn.close();
    }
    
}
