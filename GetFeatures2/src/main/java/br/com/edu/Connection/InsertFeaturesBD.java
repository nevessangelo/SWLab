/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Connection;

import br.com.edu.Objects.ClassPartition;
import br.com.edu.Objects.Entites;
import br.com.edu.Objects.PropretyPartition;
import br.com.edu.Objects.Types;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author angelo
 */
public class InsertFeaturesBD {

    public static double SomaFrequenEntites(String name_dataset) throws ClassNotFoundException, SQLException {
        double soma = 0;
        Connection conn = ConnectionMySql.Conectar();
        if (conn != null) {
            java.sql.Statement stmt = conn.createStatement();
            String query = "SELECT SUM(entite_frequen) as soma FROM `Entites` WHERE name_dataset ='" + name_dataset + "'";
            java.sql.ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                soma = rs.getDouble("soma");
            }

        }
        return soma;

    }

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

    public static void Update(String name_dataset, double frequencia, String entite) throws ClassNotFoundException, SQLException {
        double frequencia_nova = 0;
        frequencia_nova = frequencia + 1;
        Connection conn = ConnectionMySql.Conectar();
        if (conn != null) {
            String query = "UPDATE Entites set entite_frequen = ? WHERE name_dataset = ? AND entite_name = ?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setDouble(1, frequencia_nova);
            preparedStmt.setString(2, name_dataset);
            preparedStmt.setString(3, entite);

            preparedStmt.executeUpdate();

        }
        conn.close();

    }

    public static double VerificaUpdateClass(String name_dataset, String classpartition) throws ClassNotFoundException, SQLException {
        double entite_frequen = 0;
        Connection conn = ConnectionMySql.Conectar();
        if (conn != null) {
            java.sql.Statement stmt = conn.createStatement();
            String query = "SELECT frequen FROM Class WHERE name_dataset ='" + name_dataset + "' AND name_class = '" + classpartition + "' ";
            java.sql.ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                entite_frequen = rs.getDouble("frequen");
            }

        }

        conn.close();
        return entite_frequen;
    }

    public static void UpdateClass(String name_dataset, double frequencia, String classpartition) throws ClassNotFoundException, SQLException {
        Connection conn = ConnectionMySql.Conectar();
        if (conn != null) {
            String query = "UPDATE Class set frequen = ? WHERE name_dataset = ? AND name_class = ?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setDouble(1, frequencia);
            preparedStmt.setString(2, name_dataset);
            preparedStmt.setString(3, classpartition);

            preparedStmt.executeUpdate();

        }
        conn.close();

    }
    
    
    
    public static double VerificaUpdateTypes(String name_dataset, String types) throws ClassNotFoundException, SQLException{
        double type_frequen = 0;
        Connection conn = ConnectionMySql.Conectar();
        if (conn != null) {
            java.sql.Statement stmt = conn.createStatement();
            String query = "SELECT type_frequen FROM Types WHERE name_dataset ='" + name_dataset + "' AND type_name = '" + types + "' ";
            System.out.println(query);
            java.sql.ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                type_frequen = rs.getDouble("type_frequen");
                
            }
        }
        conn.close();
        return type_frequen;
    }

    public static double VerificaUpdateProprety(String name_dataset, String proprety) throws ClassNotFoundException, SQLException {
        double entite_frequen = 0;
        Connection conn = ConnectionMySql.Conectar();
        if (conn != null) {
            java.sql.Statement stmt = conn.createStatement();
            String query = "SELECT frequen FROM Proprety WHERE name_dataset ='" + name_dataset + "' AND name_proprety = '" + proprety + "' ";

            java.sql.ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                entite_frequen = rs.getDouble("frequen");
            }
        }

        conn.close();

        return entite_frequen;
    }
    
    public static void UpdateTypes(String name_dataset, double frequencia, String type) throws ClassNotFoundException, SQLException{
        Connection conn = ConnectionMySql.Conectar();
        if (conn != null) {
            String query = "UPDATE Types set type_frequen = ? WHERE name_dataset = ? AND type_name = ?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setDouble(1, frequencia);
            preparedStmt.setString(2, name_dataset);
            preparedStmt.setString(3, type);
            preparedStmt.executeUpdate();
        }
        conn.close();
         
    }

    public static void UpdateProprety(String name_dataset, double frequencia, String proprety) throws ClassNotFoundException, SQLException {
        Connection conn = ConnectionMySql.Conectar();
        if (conn != null) {
            String query = "UPDATE Proprety set frequen = ? WHERE name_dataset = ? AND name_proprety = ?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setDouble(1, frequencia);
            preparedStmt.setString(2, name_dataset);
            preparedStmt.setString(3, proprety);

            preparedStmt.executeUpdate();

        }
        conn.close();

    }
    
    public static double GetFrequenType(String name_dataset, String type) throws ClassNotFoundException, SQLException{
        double frequen = 0;
        Connection conn = ConnectionMySql.Conectar();
        if (conn != null) {
            java.sql.Statement stmt = conn.createStatement();
            String query = "SELECT type_frequen FROM Types WHERE name_dataset ='" + name_dataset + "' AND type_name = '" + type + "' ";
            java.sql.ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                frequen = rs.getDouble("type_frequen");
            }
        }
        conn.close();
        return frequen;
    }
    
    public static double UpdateFrequencia(String name_dataset, String entite) throws ClassNotFoundException, SQLException {
        double entite_frequen = 0;
        Connection conn = ConnectionMySql.Conectar();
        if (conn != null) {
            java.sql.Statement stmt = conn.createStatement();
            String query = "SELECT entite_frequen FROM Entites WHERE name_dataset ='" + name_dataset + "' AND entite_name = '" + entite + "' ";
            java.sql.ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                entite_frequen = rs.getDouble("entite_frequen");
            }

        }

        conn.close();
        return entite_frequen;
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

//        Connection conn = ConnectionMySql.Conectar();
//        if(conn != null){
//               Statement stm = (Statement) conn.createStatement();
//               String query = "INSERT INTO Class VALUES ('0','"+classp.getName_dataset()+"','"+classp.getFeature()+"','"+classp.getFrequen()+"','"+classp.getType()+"');";
//               System.out.println(query);
//               boolean res = stm.execute(query);
//               if(!res){
//                   System.out.println("Class Inseridos do dataSet: "+classp.getName_dataset());
//               }else{
//                   System.out.println("Erro");
//               }
//        }else{
//            System.out.println("Erro na Conexão");
//        }
//            conn.close();
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

//        Connection conn = ConnectionMySql.Conectar();
//        if(conn != null){
//               Statement stm = (Statement) conn.createStatement();
//               String query = "INSERT INTO Proprety VALUES ('0','"+proprety.getName_dataset()+"','"+proprety.getFeature()+"','"+proprety.getFrequen()+"','"+proprety.getType()+"');";
//               boolean res = stm.execute(query);
//               if(!res){
//                   System.out.println("Proprety Inseridos do dataSet: "+proprety.getName_dataset());
//               }else{
//                   System.out.println("Erro");
//               }
//        }else{
//            System.out.println("Erro na Conexão");
//        }
//            conn.close();
//    }
    public static void InsertEntites(Entites entites) throws ClassNotFoundException, SQLException {

        Connection conn = ConnectionMySql.Conectar();
        if (conn != null) {
            com.mysql.jdbc.Statement stm = (com.mysql.jdbc.Statement) conn.createStatement();
            String query = "INSERT INTO Entites VALUES ('0','" + entites.getName_dataset() + "','" + entites.getFeature() + "','" + entites.getFrequen() + "','" + entites.getType() + "');";
            boolean res = stm.execute(query);
            if (!res) {
                System.out.println("Entidades Inseridas do dataSet: " + entites.getName_dataset());
            } else {
                System.out.println("Erro");
            }
        } else {
            System.out.println("Erro na Conexão");
        }
        conn.close();
    }

    public static void InsertTypes(Types types) throws ClassNotFoundException, SQLException {

        Connection conn = ConnectionMySql.Conectar();
        if (conn != null) {
            com.mysql.jdbc.Statement stm = (com.mysql.jdbc.Statement) conn.createStatement();
            String query = "INSERT INTO Types VALUES ('0','" + types.getName_dataset() + "','" + types.getFeature() + "','" + types.getFrequen() + "','" + types.getType() + "');";
            boolean res = stm.execute(query);
            if (!res) {
                System.out.println("Categorias Inseridas do dataSet: " + types.getName_dataset());
            } else {
                System.out.println("Erro");
            }
        } else {
            System.out.println("Erro na Conexão");
        }
        conn.close();
    }

}
