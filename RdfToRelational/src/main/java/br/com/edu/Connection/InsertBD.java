/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Connection;

import br.com.edu.object.Features;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author angelo
 */
public class InsertBD {
    public static void InsertEntites(Features features) throws ClassNotFoundException, SQLException {
        Connection conn = ConnectionMySql.Conectar();
        if(conn != null){
            //Statement stm = (Statement) conn.createStatement();
            String query = "INSERT INTO Features (id_dataset, nome_dataset, features, tipo_feature, frequencia, dataset_size) VALUES (?,?,?,?,?,?) ";
            PreparedStatement stm = conn.prepareStatement(query);
            stm.setInt(1, features.getId_feature());
            stm.setString(2, features.getName_dataset());
            stm.setString(3, features.getFeature());
            stm.setString(4, features.getType_feature());
            stm.setInt(5, features.getFrequen());
            stm.setInt(6, features.getDatasetSize());
            stm.executeUpdate();
           
            
        }else{
            System.out.println("Erro na Conexão");
        }
        conn.close();
    }
    
}
