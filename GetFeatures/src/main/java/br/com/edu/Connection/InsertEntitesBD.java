/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Connection;

import br.com.edu.objects.Entites;
import br.com.edu.objects.InsertObject;
import com.mysql.jdbc.Statement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author angelo
 */
public class InsertEntitesBD {
    
    public static void Insert(String name, Entites entites, String type) throws ClassNotFoundException, SQLException {
        
       InsertObject insert = new InsertObject();
       insert.setName_dataset(name);
       insert.setEntites(entites);
       insert.setType(type);
        
        Connection conn = ConnectionMySql.Conectar();
        if(conn != null){
               Statement stm = (Statement) conn.createStatement();
               String query = "INSERT INTO Entites VALUES ('','"+insert.getName_dataset()+"','"+insert.getEntites().getName()+"','"+insert.getEntites().getFrequen()+"','"+insert.getType()+"');";
               System.out.println(query);
               boolean res = stm.execute(query);
               if(!res){
                   System.out.println("Dados Inseridos do dataSet: "+insert.getName_dataset());
               }else{
                   System.out.println("Erro");
               }
        }else{
            System.out.println("Erro na Conexão");
        }
    }
    
}
