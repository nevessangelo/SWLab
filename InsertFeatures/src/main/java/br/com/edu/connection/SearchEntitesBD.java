/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.connection;

import br.com.edu.objects.ClassObject;
import br.com.edu.objects.Entites;
import br.com.edu.objects.PropretyPartitionObject;
import br.com.edu.objects.Types;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author angelo
 */
public class SearchEntitesBD {
    
 public static ArrayList<Types> searchtypes() throws ClassNotFoundException, SQLException{
        ArrayList<Types> list_types = new ArrayList<>();
        Connection conn = ConnectionMySql.Conectar();
         if(conn != null){
             java.sql.Statement stmt = conn.createStatement();
             String query = "SELECT name_dataset, type_name, type_frequen FROM Types where type_frequen != 0";
             java.sql.ResultSet rs = stmt.executeQuery(query);
             while(rs.next()){
                  Types types = new Types();
                  types.setName(rs.getString("name_dataset"));
                  types.setEntite(rs.getString("type_name"));
                  types.setFrequen(rs.getDouble("type_frequen"));
                  list_types.add(types);
             }  
         }
         conn.close();
         return list_types;
         
    }
    
 public static ArrayList<Entites> search() throws ClassNotFoundException, SQLException{
        ArrayList<Entites> list_entites = new ArrayList<>();
        Connection conn = ConnectionMySql.Conectar();
         if(conn != null){
             java.sql.Statement stmt = conn.createStatement();
             String query = "SELECT name_dataset, entite_name, entite_frequen FROM Entites";
             java.sql.ResultSet rs = stmt.executeQuery(query);
             while(rs.next()){
                  Entites entites = new Entites();
                  entites.setName(rs.getString("name_dataset"));
                  entites.setEntite(rs.getString("entite_name"));
                  entites.setFrequen(rs.getInt("entite_frequen"));
                  list_entites.add(entites);
             }  
         }
         conn.close();
         return list_entites;
         
    }
    
     public static ArrayList<ClassObject> searchClass() throws ClassNotFoundException, SQLException{
        ArrayList<ClassObject> list_class = new ArrayList<>();
        Connection conn = ConnectionMySql.Conectar();
         if(conn != null){
             java.sql.Statement stmt = conn.createStatement();
             String query = "SELECT name_dataset, name_class, frequen FROM Class WHERE name_class != 'null' ";
             java.sql.ResultSet rs = stmt.executeQuery(query);
             while(rs.next()){
                  ClassObject classo = new ClassObject();
                  classo.setDataset(rs.getString("name_dataset"));
                  classo.setName(rs.getString("name_class"));
                  classo.setFrequen(rs.getInt("frequen"));
                  list_class.add(classo);
             }
            
         }
         
         conn.close();
         return list_class;
         
    }
     
      public static ArrayList<PropretyPartitionObject> searchProprety() throws ClassNotFoundException, SQLException{
        ArrayList<PropretyPartitionObject> list_proprety = new ArrayList<>();
        Connection conn = ConnectionMySql.Conectar();
         if(conn != null){
             java.sql.Statement stmt = conn.createStatement();
             String query = "SELECT name_dataset, name_proprety, frequen FROM Proprety WHERE name_proprety != 'null'";
             java.sql.ResultSet rs = stmt.executeQuery(query);
             while(rs.next()){
                  PropretyPartitionObject proprety = new PropretyPartitionObject();
                  proprety.setDataset(rs.getString("name_dataset"));
                  proprety.setName(rs.getString("name_proprety"));
                  proprety.setFrequen(rs.getInt("frequen"));
                  list_proprety.add(proprety);
             }
            
         }
         
         conn.close();
         return list_proprety;
         
    }
     
     
    
}
