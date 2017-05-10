/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.tasks;

import java.util.ArrayList;
import weka.classifiers.rules.JRip;
import weka.classifiers.rules.Rule;
import weka.classifiers.rules.RuleStats;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author angelo
 */
public class Classifier {

    private JRip classifier = null;
    private Instances structure = null;

    public Classifier(JRip classifier, Instances trainingset) {
        this.classifier = classifier;
        this.structure = new Instances(trainingset, 0);
    }
    
    public Prediction classifyInstance(Instance instance) throws Exception {
        double clsLabel = classifier.classifyInstance(instance);
        String prediction = structure.classAttribute().value((int) clsLabel);
        
        String rule = "";
        double coverage = 0;
        try {
            outerloop: for (int j = 0; j < 1e6; j++) {
                RuleStats rs = classifier.getRuleStats(j);
                ArrayList<Rule> rules = rs.getRuleset();
                for (int k = 0; k < rules.size(); k++)
                    if (rules.get(k).covers(instance)) {
                        double[] simStats = rs.getSimpleStats(k);
                        coverage = simStats[0];
                        rule = ((JRip.RipperRule) rules.get(k)).toString(structure.classAttribute()) + " (" + simStats[0] + "/"
								+ simStats[4] + ")";
                        break outerloop;
                    }
            }
        }catch (Exception e) {

	}
        return new Prediction(prediction, rule, coverage);
    }

}
