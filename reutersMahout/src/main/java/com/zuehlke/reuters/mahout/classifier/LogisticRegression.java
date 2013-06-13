package com.zuehlke.reuters.mahout.classifier;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.classifier.sgd.L1;
import org.apache.mahout.classifier.sgd.ModelSerializer;
import org.apache.mahout.classifier.sgd.OnlineLogisticRegression;
import org.apache.mahout.math.Vector;

import com.zuehlke.reuters.mahout.DataPoint;
import com.zuehlke.reuters.mahout.features.FeatureCollector;
import com.zuehlke.reuters.mahout.util.CategoriesExtractor;

public class LogisticRegression implements Classifier {

	private OnlineLogisticRegression learningAlgorithm;
	private List<String> categories;
	private List<DataPoint> trainingData;
	
	public LogisticRegression(List<DataPoint> trainingData){
		this.trainingData = trainingData;
		categories = new CategoriesExtractor().extract(trainingData);
		learningAlgorithm =
				new OnlineLogisticRegression(
				categories.size(), FeatureCollector.VECTOR_SIZE, new L1())
				.alpha(1).stepOffset(1000)
				.decayExponent(0.9)
				.lambda(3.0e-5)
				.learningRate(20);
	}
	
	private LogisticRegression(OnlineLogisticRegression learningAlgorithm, List<String> categories){
		this.learningAlgorithm = learningAlgorithm;
		this.categories = categories;
	}
	
	@Override
	public void train() {

		OnTheFlyEvaluator onTheFlyEvaluator = new OnTheFlyEvaluator();
		
		for(DataPoint dataPoint : trainingData){
			onTheFlyEvaluator.recalculateMu();
			
			int category = categories.indexOf(dataPoint.getCategory());
			String predictedCategory = classify(dataPoint.getFeatures());

			onTheFlyEvaluator.calculateAndPrintCorrectness(predictedCategory, dataPoint.getCategory());
			
			learningAlgorithm.train(category, dataPoint.getFeatures());
		}
	}

	@Override
	public String classify(Vector features) {
		Vector classify = learningAlgorithm.classify(features);
		return categories.get(classify.maxValueIndex());
	}

	@Override
	public void writeToFile(String path) throws IOException {
		ModelSerializer.writeBinary(path + "/lrmodel", learningAlgorithm);
		FileOutputStream fileOut =  new FileOutputStream(path + "/classes");
	    ObjectOutputStream out =  new ObjectOutputStream(fileOut);
	    out.writeObject(categories);
	    out.flush();
	    out.close();
	}

	public static Classifier loadFromFile(String path) throws IOException, ClassNotFoundException {
		OnlineLogisticRegression model = ModelSerializer.readBinary(new FileInputStream(path + "/lrmodel"), OnlineLogisticRegression.class);
		 FileInputStream fileIn = new FileInputStream(path + "/classes");
	    ObjectInputStream in = new ObjectInputStream(fileIn);
		@SuppressWarnings("unchecked")
		List<String> categories = (ArrayList<String>) in.readObject();
		LogisticRegression classifier = new LogisticRegression(model, categories);
		in.close();
		return classifier;
	}
}
