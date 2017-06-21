/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Connection;

import br.com.edu.Objects.ClassPartition;
import br.com.edu.Objects.Entites;
import br.com.edu.Objects.PropretyPartition;
import br.com.edu.Objects.Types_;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author angelo
 */
public class InsertFeaturesBD {
    
     public static Double VerificaCount(String name_dataset, Connection conn) throws SQLException {

        Double verifica = 0.0;
        if (conn != null) {
            java.sql.Statement stmt = conn.createStatement();
            String query = "SELECT DISTINCT nome_dataset, dataset_size  FROM Features` WHERE dataset_size = 0 AND nome_dataset = '" + name_dataset + "';";
            java.sql.ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                verifica = rs.getDouble("dataset_size");

            }

        }
        return verifica;

    }

    public static int VerificaType(String name_dataset, Connection conn) throws SQLException {

        int verifica = 0;
        if (conn != null) {
            java.sql.Statement stmt = conn.createStatement();
            String query = "SELECT DISTINCT name_dataset FROM Types WHERE name_dataset ='" + name_dataset + "' ";
            java.sql.ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                verifica = verifica + 1;

            }

        }
        return verifica;

    }

    public static void UpdateTypes(String name_dataset, double frequencia, String type, Connection conn) throws ClassNotFoundException, SQLException {
        //Connection conn = ConnectionMySql.Conectar();
        if (conn != null) {
            String query = "UPDATE Types set type_frequen = ? WHERE name_dataset = ? AND type_name = ?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setDouble(1, frequencia);
            preparedStmt.setString(2, name_dataset);
            preparedStmt.setString(3, type);
            preparedStmt.executeUpdate();
        }
      //  conn.close();

    }
    
    public static void UpdateTriples(String name_dataset, int total, Connection conn) throws ClassNotFoundException, SQLException {
        //Connection conn = ConnectionMySql.Conectar();
        if (conn != null) {
            String query = "UPDATE Features set type_frequen = ? WHERE nome_dataset = ?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, total);
            preparedStmt.setString(2, name_dataset);
            preparedStmt.executeUpdate();
        }
      //  conn.close();

    }

    public static double GetFrequenType(String name_dataset, String entite, Connection conn) throws ClassNotFoundException, SQLException {
        double frequen = 0;
        //Connection conn = ConnectionMySql.Conectar();
        if (conn != null) {
            java.sql.Statement stmt = conn.createStatement();
            String query = "SELECT entite_frequen FROM Entites WHERE name_dataset ='" + name_dataset + "' AND entite_name = '" + entite + "' ";
            java.sql.ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                frequen = rs.getDouble("entite_frequen");
            }
        }
        // conn.close();
        return frequen;
    }

    public static void InsertTypeVerifica(String nome_dataset, String entite, String type, Connection conn) throws ClassNotFoundException, SQLException {
        // Connection conn = ConnectionMySql.Conectar();
        if (conn != null) {
            String query = "INSERT INTO Types_verifica VALUES (?,?,?,?) ";
            PreparedStatement stm = conn.prepareStatement(query);
            stm.setInt(1, 0);
            stm.setString(2, type);
            stm.setString(3, entite);
            stm.setString(4, nome_dataset);
            stm.executeUpdate();
        }
        //   conn.close();
    }

    public static double UpdateFrequencia(String name_dataset, String entite, Connection conn) throws ClassNotFoundException, SQLException {
        double entite_frequen = 0;
        //Connection conn = ConnectionMySql.Conectar();
        if (conn != null) {
            java.sql.Statement stmt = conn.createStatement();
            String query = "SELECT entite_frequen FROM Entites WHERE name_dataset ='" + name_dataset + "' AND entite_name = '" + entite + "' ";
            java.sql.ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                entite_frequen = rs.getDouble("entite_frequen");
            }

        }

        // conn.close();
        return entite_frequen;
    }

    public static double VerificaUpdateTypes(String name_dataset, String types, Connection conn) throws ClassNotFoundException, SQLException {
        double type_frequen = 0;
        // Connection conn = ConnectionMySql.Conectar();
        if (conn != null) {
            java.sql.Statement stmt = conn.createStatement();
            String query = "SELECT type_frequen FROM Types WHERE name_dataset ='" + name_dataset + "' AND type_name = '" + types + "' ";
            java.sql.ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                type_frequen = rs.getDouble("type_frequen");

            }
        }
        //conn.close();
        return type_frequen;
    }

    public static int VerificaTypeEntite(String nome_dataset, String type, String entite, Connection conn) throws ClassNotFoundException, SQLException {
        int cont = 0;
        //Connection conn = ConnectionMySql.Conectar();
        if (conn != null) {
            java.sql.Statement stmt = conn.createStatement();
            String query = "SELECT nome_type_verifica FROM `Types_verifica` WHERE nome_dataset_type_verifica ='" + nome_dataset + "' AND nome_type_verifica = '" + type + "' AND entite_type_verifica = '" + entite + "'";
            java.sql.ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                cont++;
            }
        }
        // conn.close();
        return cont;

    }

    public static ArrayList<String> GetEntites(String name_dataset, Connection conn) throws ClassNotFoundException, SQLException {
        ArrayList<String> list_entites = new ArrayList<>();
        // Connection conn = ConnectionMySql.Conectar();
        if (conn != null) {
            java.sql.Statement stmt = conn.createStatement();
            String query = "SELECT entite_name FROM `Entites` WHERE name_dataset ='" + name_dataset + "'";
            java.sql.ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String nome = rs.getString("entite_name");
                list_entites.add(nome);
            }

        }
        //conn.close();
        return list_entites;

    }

    public static ArrayList<String> GetDatasets(Connection conn) throws SQLException {
        ArrayList<String> list_datasets = new ArrayList<>();
        if (conn != null) {
            java.sql.Statement stmt = conn.createStatement();
            String query = "SELECT DISTINCT name_dataset FROM `Entites` ";
            java.sql.ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String nome = rs.getString("name_dataset");
                list_datasets.add(nome);
            }

        }
        return list_datasets;
    }

    public static int VerificaClass(String nome_dataset, Connection conn) throws SQLException {
        int verifica = 0;
        if (conn != null) {
            java.sql.Statement stmt = conn.createStatement();
            String query = "SELECT DISTINCT name_dataset FROM Class WHERE name_dataset ='" + nome_dataset + "' ";
            java.sql.ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                verifica = verifica + 1;

            }

        }
        return verifica;

    }

    public static int VerificaProprety(String nome_dataset, Connection conn) throws SQLException {
        int verifica = 0;
        if (conn != null) {
            java.sql.Statement stmt = conn.createStatement();
            String query = "SELECT DISTINCT name_dataset FROM Proprety WHERE name_dataset ='" + nome_dataset + "' ";
            java.sql.ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                verifica = verifica + 1;

            }

        }
        return verifica;

    }

    public static int VerificaEntites(String nome_dataset, Connection conn) throws SQLException {
        int verifica = 0;
        if (conn != null) {
            java.sql.Statement stmt = conn.createStatement();
            String query = "SELECT DISTINCT name_dataset FROM Entites WHERE name_dataset ='" + nome_dataset + "' ";
            java.sql.ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                verifica = verifica + 1;

            }

        }
        return verifica;

    }

    public static void InsertEntites(Entites entites, Connection conn) throws ClassNotFoundException, SQLException {

        // Connection conn = ConnectionMySql.Conectar();
        if (conn != null) {
            com.mysql.jdbc.Statement stm = (com.mysql.jdbc.Statement) conn.createStatement();
            String query = "INSERT INTO Entites VALUES ('0','" + entites.getName_dataset() + "','" + entites.getFeature() + "','" + entites.getFrequen() + "','" + entites.getType() + "');";
            boolean res = stm.execute(query);
            if (!res) {
                // System.out.println("Entidades Inseridas do dataSet: " + entites.getName_dataset());
            } else {
                System.out.println("Erro");
            }
        } else {
            System.out.println("Erro na Conexão");
        }
        //conn.close();
    }

    public static void Update(String name_dataset, double frequencia, String entite, Connection conn) throws ClassNotFoundException, SQLException {
        double frequencia_nova = 0;
        frequencia_nova = frequencia + 1;
        //Connection conn = ConnectionMySql.Conectar();
        if (conn != null) {
            String query = "UPDATE Entites set entite_frequen = ? WHERE name_dataset = ? AND entite_name = ?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setDouble(1, frequencia_nova);
            preparedStmt.setString(2, name_dataset);
            preparedStmt.setString(3, entite);

            preparedStmt.executeUpdate();

        }
        //conn.close();

    }

  //  public static double UpdateFrequencia(String name_dataset, String entite, Connection conn) throws ClassNotFoundException, SQLException {
    //     double entite_frequen = 0;
    //Connection conn = ConnectionMySql.Conectar();
    //     if (conn != null) {
    //       java.sql.Statement stmt = conn.createStatement();
    //        String query = "SELECT entite_frequen FROM Entites WHERE name_dataset ='" + name_dataset + "' AND entite_name = '" + entite + "' ";
    //       java.sql.ResultSet rs = stmt.executeQuery(query);
    //      while (rs.next()) {
    //         entite_frequen = rs.getDouble("entite_frequen");
    //     }
       // }
        //conn.close();
    //   return entite_frequen;
    // }
    public static void InsertTypes(Types_ type, Connection conn) throws ClassNotFoundException, SQLException {

        // Connection conn = ConnectionMySql.Conectar();
        if (conn != null) {
            com.mysql.jdbc.Statement stm = (com.mysql.jdbc.Statement) conn.createStatement();
            String query = "INSERT INTO Types VALUES ('0','" + type.getName_dataset() + "','" + type.getFeature() + "','" + type.getFrequen() + "','" + type.getType() + "');";
            boolean res = stm.execute(query);
            if (!res) {
                System.out.println("Categorias Inseridas do dataSet: " + type.getName_dataset());
            } else {
                System.out.println("Erro");
            }
        } else {
            System.out.println("Erro na Conexão");
        }
        //conn.close();
    }

    public static void InsertClass(ClassPartition classp, Connection conn) throws ClassNotFoundException, SQLException {
        if (classp != null) {
            // Connection conn = ConnectionMySql.Conectar();
            if (conn != null) {
                String query = "INSERT INTO Class VALUES (?,?,?,?,?) ";
                PreparedStatement stm = conn.prepareStatement(query);
                stm.setInt(1, classp.getId());
                stm.setString(2, classp.getName_dataset());
                stm.setString(3, classp.getFeature());
                stm.setDouble(4, classp.getFrequen());
                stm.setString(5, classp.getType());
                stm.executeUpdate();
            }
            //  conn.close();

        }
    }

    public static void UpdateClass(String name_dataset, double frequencia, String classpartition, Connection conn) throws ClassNotFoundException, SQLException {
        //Connection conn = ConnectionMySql.Conectar();
        if (conn != null) {
            String query = "UPDATE Class set frequen = ? WHERE name_dataset = ? AND name_class = ?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setDouble(1, frequencia);
            preparedStmt.setString(2, name_dataset);
            preparedStmt.setString(3, classpartition);

            preparedStmt.executeUpdate();

        }
        //conn.close();

    }

    public static double VerificaUpdateClass(String name_dataset, String classpartition, Connection conn) throws ClassNotFoundException, SQLException {
        double entite_frequen = 0;
        //Connection conn = ConnectionMySql.Conectar();
        if (conn != null) {
            java.sql.Statement stmt = conn.createStatement();
            String query = "SELECT frequen FROM Class WHERE name_dataset ='" + name_dataset + "' AND name_class = '" + classpartition + "' ";
            java.sql.ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                entite_frequen = rs.getDouble("frequen");
            }

        }

        // conn.close();
        return entite_frequen;
    }

    public static void InsertProprety(PropretyPartition proprety, Connection conn) throws ClassNotFoundException, SQLException {
        if (proprety != null) {
            //Connection conn = ConnectionMySql.Conectar();
            if (conn != null) {
                String query = "INSERT INTO Proprety VALUES (?,?,?,?,?) ";
                PreparedStatement stm = conn.prepareStatement(query);
                stm.setInt(1, proprety.getId());
                stm.setString(2, proprety.getName_dataset());
                stm.setString(3, proprety.getFeature());
                stm.setDouble(4, proprety.getFrequen());
                stm.setString(5, proprety.getType());
                stm.executeUpdate();

            }
            // conn.close();
        }

    }

    public static void UpdateProprety(String name_dataset, double frequencia, String proprety, Connection conn) throws ClassNotFoundException, SQLException {
        //Connection conn = ConnectionMySql.Conectar();
        if (conn != null) {
            String query = "UPDATE Proprety set frequen = ? WHERE name_dataset = ? AND name_proprety = ?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setDouble(1, frequencia);
            preparedStmt.setString(2, name_dataset);
            preparedStmt.setString(3, proprety);

            preparedStmt.executeUpdate();

        }

        //conn.close();
    }

    public static double VerificaUpdateProprety(String name_dataset, String proprety, Connection conn) throws ClassNotFoundException, SQLException {
        double entite_frequen = 0;
        //Connection conn = ConnectionMySql.Conectar();
        if (conn != null) {
            java.sql.Statement stmt = conn.createStatement();
            String query = "SELECT frequen FROM Proprety WHERE name_dataset ='" + name_dataset + "' AND name_proprety = '" + proprety + "' ";

            java.sql.ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                entite_frequen = rs.getDouble("frequen");
            }

        }

        //conn.close();
        return entite_frequen;
    }

}
