/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Connection;


import br.com.edu.Objects.ClassPartition;
import br.com.edu.Objects.PropretyPartition;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author angelo
 */
public class InsertFeaturesBD {



    public static int VerificaClass(String name_dataset) throws ClassNotFoundException, SQLException {
        int cont = 0;
        Connection conn = ConnectionMySql.Conectar();
        if (conn != null) {
            java.sql.Statement stmt = conn.createStatement();
            String query = "SELECT name_dataset FROM `Class` WHERE name_dataset ='" + name_dataset + "'";
            java.sql.ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                cont++;
            }

        }

        conn.close();
        return cont;

    }

    public static int VerificaProprety(String name_dataset) throws ClassNotFoundException, SQLException {
        int cont = 0;
        Connection conn = ConnectionMySql.Conectar();
        if (conn != null) {
            java.sql.Statement stmt = conn.createStatement();
            String query = "SELECT name_dataset FROM `Proprety` WHERE name_dataset ='" + name_dataset + "'";
            java.sql.ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                cont++;
            }
        }
        conn.close();
        return cont;

    }
    
     public static void InsertClass(ClassPartition classp) throws ClassNotFoundException, SQLException {
        if (classp != null) {
            Connection conn = ConnectionMySql.Conectar();
            if (conn != null) {
                String query = "INSERT INTO Class VALUES (?,?,?,?,?) ";
                PreparedStatement stm = conn.prepareStatement(query);
                stm.setInt(1, classp.getId());
                stm.setString(2, classp.getName_dataset());
                stm.setString(3, classp.getFeature());
                stm.setDouble(4, classp.getFrequen());
                stm.setString(5, classp.getType());
                stm.executeUpdate();
                System.out.println("Class Inseridos do dataSet: " + classp.getName_dataset());

            }
            conn.close();

        }
     }
     
      public static void InsertProprety(PropretyPartition proprety) throws ClassNotFoundException, SQLException {
        if (proprety != null) {
            Connection conn = ConnectionMySql.Conectar();
            if (conn != null) {
                String query = "INSERT INTO Proprety VALUES (?,?,?,?,?) ";
                PreparedStatement stm = conn.prepareStatement(query);
                stm.setInt(1, proprety.getId());
                stm.setString(2, proprety.getName_dataset());
                stm.setString(3, proprety.getFeature());
                stm.setDouble(4, proprety.getFrequen());
                stm.setString(5, proprety.getType());
                stm.executeUpdate();
                System.out.println("Proprety Inseridos do dataSet: " + proprety.getName_dataset());

            }
            conn.close();
        }

    }
    
    
    

    

}
