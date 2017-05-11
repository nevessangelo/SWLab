/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Main;

import br.com.edu.tasks.Classifier;
import br.com.edu.tasks.Ranker;
import br.com.edu.tasks.Trainer;
import java.io.File;

/**
 *
 * @author angelo
 */
public class Main {
    public static void main(String[] args) throws Exception {
        String file = new File("/home/angelo/Área de Trabalho/treinamento.arff").toString();
        Classifier classifier;
        Trainer.Trainer_JRIP(file);
        
        
        //String file_test = new File("/home/angelo/Área de Trabalho/teste.arff").toString();
        //Ranker.RankerInstance(file_test, classifier);
        
        //System.out.println(classifier);
    }
    
}
