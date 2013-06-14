package com.zuehlke.reuters.mahout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.mahout.math.Vector;

import com.zuehlke.reuters.mahout.classifier.Classifier;
import com.zuehlke.reuters.mahout.classifier.LogisticRegression;
import com.zuehlke.reuters.mahout.features.FeatureCollector;
import com.zuehlke.reuters.mahout.importer.ParseException;
import com.zuehlke.reuters.mahout.preprocess.WordCategoryMapper;

public class ReutersTrainer {

	public static void main(String[] args) throws ParseException, IOException {
		String dataDir = null;
		if (args.length == 0) {
			dataDir = "/home/cloudera/workspace/reuters/reutersMahout/Data";
		} else {
			dataDir = args[0];
		}

		List<ReutersMessage> messages = new MessageExtractor().extract(dataDir);
		Map<String, Set<String>> categoryWords = new WordCategoryMapper().map(messages);
		ObjectOutputStream out =  new ObjectOutputStream(new FileOutputStream("/home/cloudera/workspace/reuters/reutersMahout/models/categoryWords"));
	    out.writeObject(categoryWords);
	    out.flush();

		System.out.println("-- Extract features");
		FeatureCollector featureCollector = new FeatureCollector(categoryWords);
		List<DataPoint> trainingData = new ArrayList<DataPoint>();
		for (ReutersMessage message : messages) {
			if (!message.getTopic().isEmpty() && message.getBody() != null) {
				Vector features = featureCollector.extractFeatures(message);
				trainingData.add(new DataPoint(features, message.getTopic()));
			}
		}
		
		System.out.println("-- Train");
		
		Classifier classifier = new LogisticRegression(trainingData);
		classifier.train();

		File modelDir = new File("/home/cloudera/workspace/reuters/reutersMahout/models");
		modelDir.mkdirs();
		classifier.writeToFile(modelDir.getAbsolutePath());
	}

}
