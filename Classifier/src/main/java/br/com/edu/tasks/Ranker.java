/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.tasks;

import java.io.File;
import java.io.IOException;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import java.util.List;
import weka.classifiers.evaluation.Prediction;
import weka.core.Instance;
import br.com.edu.tasks.Classifier;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author angelo
 */
public class Ranker {
    private static List<br.com.edu.tasks.Prediction> classifyInstance(Instance instance, Classifier classifier) throws Exception {
		List<br.com.edu.tasks.Prediction> predictions = new ArrayList<br.com.edu.tasks.Prediction>();
		br.com.edu.tasks.Prediction prediction = null;
		//for (Classifier classifier : classifiers) {
		prediction = classifier.classifyInstance(instance);
		if (!prediction.prediction.equals("None"))
			predictions.add(prediction);
		//}
		Collections.sort(predictions);
		Collections.reverse(predictions);
                
		return predictions;
	}
    
    public static void RankerInstance(String File, Classifier classifier) throws IOException, Exception{
        ArffLoader loader;
        loader = new ArffLoader();
        loader.setFile(new File(String.format(File)));
        loader.getStructure();
        Instances testset = loader.getDataSet();
        testset.setClassIndex(testset.numAttributes() - 1);
        for (Instance instance : testset) {
            List<br.com.edu.tasks.Prediction> ranking = classifyInstance(instance, classifier);
            for (br.com.edu.tasks.Prediction value: ranking){
                System.out.println("O valor e : "+ value.confidence);
                System.out.println("Classe: "+ value.prediction);
            }
        }
        
        
    }
    
}
