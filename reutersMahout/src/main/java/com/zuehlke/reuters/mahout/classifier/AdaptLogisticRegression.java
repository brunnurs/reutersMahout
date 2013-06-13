package com.zuehlke.reuters.mahout.classifier;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.apache.mahout.classifier.sgd.AdaptiveLogisticRegression;
import org.apache.mahout.classifier.sgd.L1;
import org.apache.mahout.math.Vector;

import com.zuehlke.reuters.mahout.DataPoint;
import com.zuehlke.reuters.mahout.features.FeatureCollector;
import com.zuehlke.reuters.mahout.util.CategoriesExtractor;

public class AdaptLogisticRegression implements Classifier {
	private AdaptiveLogisticRegression learningAlgorithm;
	private List<String> categories;
	private List<DataPoint> trainingData;
	
	public AdaptLogisticRegression(List<DataPoint> trainingData){
		this.trainingData = trainingData;
		Collections.shuffle(this.trainingData);
		categories = new CategoriesExtractor().extract(trainingData);
		learningAlgorithm = new AdaptiveLogisticRegression(categories.size(), FeatureCollector.VECTOR_SIZE, new L1());
	}

	@Override
	public String classify(Vector features) {
		Vector classify = learningAlgorithm.getBest().getPayload().getLearner().classifyFull(features);
		return categories.get(classify.maxValueIndex());
	}

	@Override
	public void writeToFile(String path) throws IOException {
		throw new NotImplementedException();
	}

	@Override
	public void train() {
		for(DataPoint dataPoint : trainingData){
			int category = categories.indexOf(dataPoint.getCategory());
			learningAlgorithm.train(category, dataPoint.getFeatures());
		}
		learningAlgorithm.close();
	}
}