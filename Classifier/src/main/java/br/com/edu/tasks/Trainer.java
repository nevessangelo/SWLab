/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.tasks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import weka.classifiers.rules.JRip;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/**
 *
 * @author angelo
 */
public class Trainer {
    
    public static void Trainer_JRIP(String file) throws Exception{
        
        JRip jrip = new JRip();
        String[] options2 = {"-F", "3", "-N", "2.0", "-O", "2", "-S", "1"};
        jrip.setOptions(options2);

        Instances train = new Instances(new BufferedReader(new FileReader(file)));
        int lastIndex = train.numAttributes() - 1;
        train.setClassIndex(lastIndex);
        jrip.buildClassifier(train);
        System.out.println(jrip.toString());
        
        String file_test = new File("/home/angelo/Área de Trabalho/test.arff").toString();
        ArffLoader loader;
        loader = new ArffLoader();
        loader.setFile(new File(String.format(file_test)));
        loader.getStructure();
        Instances testset = loader.getDataSet();
        testset.setClassIndex(testset.numAttributes() - 1);
        for (Instance instance : testset) {
            
            double index = jrip.classifyInstance(instance);
            
            System.out.println(index);
            double[] percentage = jrip.distributionForInstance(instance);
            //String prediction = train.attribute(lastIndex).value((int)index);
            String prediction=train.classAttribute().value((int)index); 
            
            System.out.println(prediction);
            String distribution="";
            for(int i=0; i <percentage.length; i=i+1){
                System.out.println(percentage[i]);
                //if(i==index)
                //    distribution=distribution+"*"+Double.toString(percentage[i])+",";
                //else
                //    distribution=distribution+Double.toString(percentage[i])+",";       
            }
             //distribution=distribution.substring(0, distribution.length()-1);
             //System.out.println("Distribution:"+ distribution);
            //System.out.println(index);
            
            
        }
        

        //try {
        //    for (int j = 0; j < 1e6; j++) {
        //        jrip.getRuleStats(j).countData();
        //    }
       // } catch (Exception e) {
       // }
        //
        //
        //
        //
        //
        //
        //
        //
            
        //}
        
        //Classifier classifier = new Classifier(jrip, train);
	
        //return classifier;
       
        
    }

    

}
