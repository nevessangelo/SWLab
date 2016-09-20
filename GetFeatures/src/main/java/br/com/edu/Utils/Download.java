/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Utils;

import java.util.ArrayList;

/**
 *
 * @author angelo
 */
public class Download {
    
    public static void DownloadDump(ArrayList Datasets_Dump, ArrayList Datasets_difdump){
        for(int i = 0; i < Datasets_Dump.size(); i++){
            System.out.println(Datasets_Dump.get(i));
        }
    }
    
}
