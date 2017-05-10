/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.tasks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import weka.classifiers.rules.JRip;
import weka.core.Instances;

/**
 *
 * @author angelo
 */
public class Trainer {
    
    public static  Classifier Trainer_JRIP(String file) throws Exception{
        JRip jrip = new JRip();
        String[] options2 = {"-F", "3", "-N", "2.0", "-O", "2", "-S", "1"};
        jrip.setOptions(options2);

        Instances train = new Instances(new BufferedReader(new FileReader(file)));
        int lastIndex = train.numAttributes() - 1;
        train.setClassIndex(lastIndex);
        jrip.buildClassifier(train);
        

        try {
            for (int j = 0; j < 1e6; j++) {
                jrip.getRuleStats(j).countData();
            }
        } catch (Exception e) {
        }
        Classifier classifier = new Classifier(jrip, train);
	System.out.println(jrip.toString());
        return classifier;
       
        
    }

    

}
