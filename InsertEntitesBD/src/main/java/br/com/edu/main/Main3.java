/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.main;

import br.com.edu.rdf.Notes;
import java.sql.SQLException;

/**
 *
 * @author angelo
 */
public class Main3 {
    public static void main(String[] args) throws SQLException, Exception {
        Notes.getNotes();
        System.out.println("Fim do armazenamento das entidades dos NOTES");
    }
    
}
