package com.zuehlke.reuters.mahout.classifier;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.classifier.sgd.L1;
import org.apache.mahout.classifier.sgd.ModelSerializer;
import org.apache.mahout.classifier.sgd.OnlineLogisticRegression;
import org.apache.mahout.math.Vector;

import com.zuehlke.reuters.mahout.DataPoint;
import com.zuehlke.reuters.mahout.features.FeatureCollector;

public class LogisticRegression implements Classifier {

	private OnlineLogisticRegression learningAlgorithm;
	private List<String> categories;
	private List<DataPoint> trainingData;
	
	public LogisticRegression(List<DataPoint> trainingData){
		this.trainingData = trainingData;
		extractCategories(trainingData);
		learningAlgorithm =
				new OnlineLogisticRegression(
				categories.size(), FeatureCollector.VECTOR_SIZE, new L1())
				.alpha(1).stepOffset(1000)
				.decayExponent(0.9)
				.lambda(3.0e-5)
				.learningRate(20);
	}
	
	private LogisticRegression(OnlineLogisticRegression learningAlgorithm){
		this.learningAlgorithm = learningAlgorithm;
	}
	
	@Override
	public void train() {
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

	@Override
	public void safeToFile(String path) throws IOException {
		ModelSerializer.writeBinary(path, learningAlgorithm);
	}

	public static Classifier loadFromFile(String path) throws IOException {
		OnlineLogisticRegression model = ModelSerializer.readBinary(new FileInputStream(path), OnlineLogisticRegression.class);
		LogisticRegression classifier = new LogisticRegression(model);
		return classifier;
	}
}
