/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Connection;

import br.com.edu.Objects.ClassPartition;
import br.com.edu.Objects.Entites;
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
    
 
    
     public static int VerificaClass(String name_dataset) throws ClassNotFoundException, SQLException{
         int cont = 0;
         Connection conn = ConnectionMySql.Conectar();
         if(conn != null){
             java.sql.Statement stmt = conn.createStatement();
             String query = "SELECT name_dataset FROM `Class` WHERE name_dataset ='"+name_dataset+"'";
             java.sql.ResultSet rs = stmt.executeQuery(query);
             while(rs.next()){
                    cont++;
             }
             
             
         }
         
         conn.close();
         return cont;
         
    }
      
       public static int VerificaProprety(String name_dataset) throws ClassNotFoundException, SQLException{
         int cont = 0;
         Connection conn = ConnectionMySql.Conectar();
         if(conn != null){
             java.sql.Statement stmt = conn.createStatement();
             String query = "SELECT name_dataset FROM `Proprety` WHERE name_dataset ='"+name_dataset+"'";
             java.sql.ResultSet rs = stmt.executeQuery(query);
             while(rs.next()){
                    cont++;
             }
         }
         conn.close();
         return cont;
         
    }
    
    public static void Update(String name_dataset, int frequencia, String entite) throws ClassNotFoundException, SQLException{
        int frequencia_nova = 0;
        frequencia_nova = frequencia + 1;
        Connection conn = ConnectionMySql.Conectar();
        if(conn != null){
                 String query = "UPDATE Entites set entite_frequen = ? WHERE name_dataset = ? AND entite_name = ?";
                 PreparedStatement preparedStmt = conn.prepareStatement(query);
                 preparedStmt.setInt(1,frequencia_nova);
                 preparedStmt.setString(2, name_dataset);
                 preparedStmt.setString(3, entite);
                 
                 preparedStmt.executeUpdate();
            
        }
        conn.close();
        
    }
    
    
     public static int VerificaUpdateClass(String name_dataset, String classpartition) throws ClassNotFoundException, SQLException{
         int entite_frequen = 0;
         Connection conn = ConnectionMySql.Conectar();
         if(conn != null){
             java.sql.Statement stmt = conn.createStatement();
             String query = "SELECT frequen FROM Class WHERE name_dataset ='"+name_dataset+"' AND name_class = '"+classpartition+"' ";
             java.sql.ResultSet rs = stmt.executeQuery(query);
             while(rs.next()){
                  entite_frequen = rs.getInt("frequen");
             }
             
         }
         
         conn.close();
         return entite_frequen;   
    }
     
     
     public static void UpdateClass(String name_dataset, int frequencia, String classpartition) throws ClassNotFoundException, SQLException{
        Connection conn = ConnectionMySql.Conectar();
        if(conn != null){
                 String query = "UPDATE Class set frequen = ? WHERE name_dataset = ? AND name_class = ?";
                 PreparedStatement preparedStmt = conn.prepareStatement(query);
                 preparedStmt.setInt(1,frequencia);
                 preparedStmt.setString(2, name_dataset);
                 preparedStmt.setString(3, classpartition);
                 
                 preparedStmt.executeUpdate();
            
        }
        conn.close();
        
    }
     
       public static int VerificaUpdateProprety(String name_dataset, String proprety) throws ClassNotFoundException, SQLException{
         int entite_frequen = 0;
         Connection conn = ConnectionMySql.Conectar();
         if(conn != null){
             java.sql.Statement stmt = conn.createStatement();
             String query = "SELECT frequen FROM Proprety WHERE name_dataset ='"+name_dataset+"' AND name_proprety = '"+proprety+"' ";
             java.sql.ResultSet rs = stmt.executeQuery(query);
             while(rs.next()){
                  entite_frequen = rs.getInt("frequen");
             }
             
         }
         
         conn.close();
         return entite_frequen;   
    }
     
     
     public static void UpdateProprety(String name_dataset, int frequencia, String proprety) throws ClassNotFoundException, SQLException{
        Connection conn = ConnectionMySql.Conectar();
        if(conn != null){
                 String query = "UPDATE Proprety set frequen = ? WHERE name_dataset = ? AND name_proprety = ?";
                 PreparedStatement preparedStmt = conn.prepareStatement(query);
                 preparedStmt.setInt(1,frequencia);
                 preparedStmt.setString(2, name_dataset);
                 preparedStmt.setString(3, proprety);
                 
                 preparedStmt.executeUpdate();
            
        }
        conn.close();
        
    }
    
  
    
    public static int UpdateFrequencia(String name_dataset, String entite) throws ClassNotFoundException, SQLException{
         int entite_frequen = 0;
         Connection conn = ConnectionMySql.Conectar();
         if(conn != null){
             java.sql.Statement stmt = conn.createStatement();
             String query = "SELECT entite_frequen FROM Entites WHERE name_dataset ='"+name_dataset+"' AND entite_name = '"+entite+"' ";
             java.sql.ResultSet rs = stmt.executeQuery(query);
             while(rs.next()){
                  entite_frequen = rs.getInt("entite_frequen");
             }
             
         }
         
         conn.close();
         return entite_frequen;   
    }
       
   
      public static void InsertClass(ClassPartition classp) throws ClassNotFoundException, SQLException{
          Connection conn = ConnectionMySql.Conectar();
           if(conn != null){
              String query = "INSERT INTO Class VALUES (?,?,?,?,?) ";
              PreparedStatement stm = conn.prepareStatement(query);
              stm.setInt(1,classp.getId());
              stm.setString(2, classp.getName_dataset());
              stm.setString(3, classp.getFeature());
              stm.setInt(4, classp.getFrequen());
              stm.setString(5, classp.getType());
              stm.executeUpdate();
              System.out.println("Class Inseridos do dataSet: "+classp.getName_dataset());

           }
         conn.close();
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
      
     public static void InsertProprety(PropretyPartition proprety) throws ClassNotFoundException, SQLException{
         Connection conn = ConnectionMySql.Conectar();
           if(conn != null){
              String query = "INSERT INTO Proprety VALUES (?,?,?,?,?) ";
              PreparedStatement stm = conn.prepareStatement(query);
              stm.setInt(1,proprety.getId());
              stm.setString(2, proprety.getName_dataset());
              stm.setString(3, proprety.getFeature());
              stm.setInt(4, proprety.getFrequen());
              stm.setString(5, proprety.getType());
              stm.executeUpdate();
              System.out.println("Proprety Inseridos do dataSet: "+proprety.getName_dataset());

           }
        conn.close();
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
        if(conn != null){
               com.mysql.jdbc.Statement stm = (com.mysql.jdbc.Statement) conn.createStatement();
               String query = "INSERT INTO Entites VALUES ('0','"+entites.getName_dataset()+"','"+entites.getFeature()+"','"+entites.getFrequen()+"','"+entites.getType()+"');";
               boolean res = stm.execute(query);
               if(!res){
                   System.out.println("Entidades Inseridas do dataSet: "+entites.getName_dataset());
               }else{
                   System.out.println("Erro");
               }
        }else{
            System.out.println("Erro na Conexão");
        }
            conn.close();
    }
    
}
