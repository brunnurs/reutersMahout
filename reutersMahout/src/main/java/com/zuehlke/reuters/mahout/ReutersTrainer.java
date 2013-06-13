package com.zuehlke.reuters.mahout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
		Map<String, List<String>> categoryWords = new WordCategoryMapper().map(messages);

		List<DataPoint> trainingData = new ArrayList<DataPoint>();
		for (ReutersMessage message : messages) {
			if (!message.getTopic().isEmpty() && message.getBody() != null) {
				Vector features = new FeatureCollector(categoryWords).extractFeatures(message.getBody());
				trainingData.add(new DataPoint(features, message.getTopic()));
			}
		}
		
		Classifier classifier = new LogisticRegression(trainingData);
		classifier.train();

		File modelDir = new File("/home/cloudera/workspace/reuters/reutersMahout/models");
		modelDir.mkdirs();
		classifier.writeToFile(modelDir.getAbsolutePath());
	}

}
