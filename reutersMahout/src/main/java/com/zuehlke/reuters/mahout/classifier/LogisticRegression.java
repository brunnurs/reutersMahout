package com.zuehlke.reuters.mahout.classifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.mahout.classifier.sgd.L1;
import org.apache.mahout.classifier.sgd.OnlineLogisticRegression;
import org.apache.mahout.math.Vector;

import com.zuehlke.reuters.mahout.DataPoint;

public class LogisticRegression implements Classifier {

	private OnlineLogisticRegression learningAlgorithm;
	private List<String> categories;
	
	@Override
	public void train(List<DataPoint> trainingData) {
		extractCategories(trainingData);
		
		learningAlgorithm =
				new OnlineLogisticRegression(
				categories.size(), trainingData.size(), new L1())
				.alpha(1).stepOffset(1000)
				.decayExponent(0.9)
				.lambda(3.0e-5)
				.learningRate(20);
		
		for(DataPoint dataPoint : trainingData){
			int category = categories.indexOf(dataPoint.getCategory());
			learningAlgorithm.train(category, dataPoint.getFeatures());
		}
	}

	private void extractCategories(List<DataPoint> trainingData) {
		categories = new ArrayList<String>();
		for(DataPoint dataPoint : trainingData){
			if(!categories.contains(dataPoint.getCategory())){
				categories.add(dataPoint.getCategory());
			}
		}
	}

	@Override
	public String classify(Vector features) {
		Vector classify = learningAlgorithm.classify(features);
		return categories.get(classify.maxValueIndex());
	}

}
