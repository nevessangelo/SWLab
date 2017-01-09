/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Main;

import br.com.edu.Connection.Methods;
import br.com.edu.Utils.Utils;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author angelo
 */
public class Main {
    
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ArrayList<String> datasets;
        ArrayList<String> nomes;
        datasets = Methods.Datasets();
        nomes = Utils.Datasets(datasets);
        Utils.VerificaDataset(nomes);
        
    }
    
}
