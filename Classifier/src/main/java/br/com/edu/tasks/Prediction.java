/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.tasks;

/**
 *
 * @author angelo
 */
public class Prediction implements Comparable<Prediction> {

	public String prediction = null;
	public String rule = null;
	public Double confidence = 0d;

	public Prediction(String prediction, String rule, double confidence) {
		this.rule = rule;
		this.prediction = prediction;
		this.confidence = confidence;
	}

	@Override
	public int compareTo(Prediction o) {
		return confidence.compareTo((Double) o.confidence);
	}

}
