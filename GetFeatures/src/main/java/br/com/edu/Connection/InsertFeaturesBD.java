/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Connection;

import br.com.edu.objects.ClassPartition;
import br.com.edu.objects.Entites;
import br.com.edu.objects.InsertObject;
import br.com.edu.objects.PropretyPartition;
import com.mysql.jdbc.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.jena.query.ResultSet;

/**
 *
 * @author angelo
 */
public class InsertFeaturesBD {
    
    
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
               Statement stm = (Statement) conn.createStatement();
               String query = "INSERT INTO Class VALUES ('','"+classp.getName_dataset()+"','"+classp.getName()+"','"+classp.getFrequen()+"','"+classp.getType()+"');";
               boolean res = stm.execute(query);
               if(!res){
                   System.out.println("Class Inseridos do dataSet: "+classp.getName_dataset());
               }else{
                   System.out.println("Erro");
               }
        }else{
            System.out.println("Erro na Conexão");
        }
            conn.close();
    }
    
    public static void InsertProprety(PropretyPartition proprety) throws ClassNotFoundException, SQLException{
      
        Connection conn = ConnectionMySql.Conectar();
        if(conn != null){
               Statement stm = (Statement) conn.createStatement();
               String query = "INSERT INTO Proprety VALUES ('','"+proprety.getName_dataset()+"','"+proprety.getName()+"','"+proprety.getFrequen()+"','"+proprety.getType()+"');";
               boolean res = stm.execute(query);
               if(!res){
                   System.out.println("Class Inseridos do dataSet: "+proprety.getName_dataset());
               }else{
                   System.out.println("Erro");
               }
        }else{
            System.out.println("Erro na Conexão");
        }
            conn.close();
    }
        
          
    
    public static void InsertEntites(String name, Entites entites, String type) throws ClassNotFoundException, SQLException {
        
       InsertObject insert = new InsertObject();
       insert.setName_dataset(name);
       insert.setEntites(entites);
       insert.setType(type);
        
        Connection conn = ConnectionMySql.Conectar();
        if(conn != null){
               Statement stm = (Statement) conn.createStatement();
               String query = "INSERT INTO Entites VALUES ('','"+insert.getName_dataset()+"','"+insert.getEntites().getName()+"','"+insert.getEntites().getFrequen()+"','"+insert.getType()+"');";
               boolean res = stm.execute(query);
               if(!res){
                   System.out.println("Dados Inseridos do dataSet: "+insert.getName_dataset());
               }else{
                   System.out.println("Erro");
               }
        }else{
            System.out.println("Erro na Conexão");
        }
            conn.close();
    }
    
}
