package com.zuehlke.reuters.mahout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.math.Vector;

import com.zuehlke.reuters.mahout.classifier.Classifier;
import com.zuehlke.reuters.mahout.classifier.LogisticRegression;
import com.zuehlke.reuters.mahout.features.FeatureCollector;
import com.zuehlke.reuters.mahout.importer.ParseException;
import com.zuehlke.reuters.mahout.importer.ReutersMessageImporter;

public class ReutersTrainer {

	public static void main(String[] args) throws ParseException, IOException {
		ReutersMessageImporter importer = new ReutersMessageImporter();
		List<ReutersMessage> messages = importer.importData(new File(args[0]));
		List<DataPoint> trainingData = new ArrayList<DataPoint>();
		FeatureCollector featureCollector = new FeatureCollector();

		for (ReutersMessage message : messages) {
			if (!message.getTopic().isEmpty() && message.getBody() != null) {
				Vector features = featureCollector.extractFeatures(message.getBody());
				trainingData.add(new DataPoint(features, message.getTopic()));
			}
		}
		
		Classifier classifier = new LogisticRegression(trainingData);
		classifier.train();
		classifier.writeToFile("/home/cloudera/models");

	}

}
