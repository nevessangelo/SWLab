/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.partition;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.RecursiveAction;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.rules.JRip;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/**
 *
 * @author angelo
 */
public class Tranning {
   
    
    public static Double Tranning_JRIP(String test, String tranning) throws IOException, Exception{
        Double result_ = null;
        ArffLoader loader;
        loader = new ArffLoader();
        loader.setFile(new File(tranning));
        loader.getStructure();
        
        Instances trainingset = loader.getDataSet();
        int classIndex = trainingset.numAttributes() - 1;
        trainingset.setClassIndex(classIndex);
        
        J48 j48 = new J48();
        //JRip jRip = new JRip();
        //String[] options2 = {"-F", "3", "-N", "2.0", "-O", "2", "-S", "1"};
        //jRip.setOptions(options2);
        //jRip.buildClassifier(trainingset);
        j48.buildClassifier(trainingset);
        
        loader = new ArffLoader();
        loader.setFile(new File(test));
        loader.getStructure();
        
        Instances testset = loader.getDataSet();
        testset.setClassIndex(testset.numAttributes() - 1);
        for (Instance instance : testset) {
            //double[] result = jRip.distributionForInstance(instance);
            double[] result = j48.distributionForInstance(instance);
            result_ = result[1];
            //System.out.println(test + " " + result[1] + " " + tranning);
        }
        return result_;
        
    }


    
}
