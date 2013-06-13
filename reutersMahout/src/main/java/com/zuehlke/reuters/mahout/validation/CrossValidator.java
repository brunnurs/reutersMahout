package com.zuehlke.reuters.mahout.validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.mahout.classifier.ConfusionMatrix;
import org.apache.mahout.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zuehlke.reuters.mahout.DataPoint;
import com.zuehlke.reuters.mahout.MessageExtractor;
import com.zuehlke.reuters.mahout.ReutersMessage;
import com.zuehlke.reuters.mahout.classifier.Classifier;
import com.zuehlke.reuters.mahout.classifier.LogisticRegression;
import com.zuehlke.reuters.mahout.features.FeatureCollector;
import com.zuehlke.reuters.mahout.importer.ParseException;
import com.zuehlke.reuters.mahout.preprocess.WordCategoryMapper;
import com.zuehlke.reuters.mahout.util.CategoriesExtractor;

public class CrossValidator {
	private static final Logger LOG = LoggerFactory
			.getLogger(CrossValidator.class);

	private double validationDataPercentage;

	private boolean logConfusionMatrix = true;

	public CrossValidator() {
		this(0.1);
	}

	public CrossValidator(double validationDataPercentage) {
		this.validationDataPercentage = validationDataPercentage;
	}

	public void execute(List<ReutersMessage> messages) {
		List<DataPoint> data = new ArrayList<DataPoint>();

		int validationDataSize = (int) Math.floor(messages.size()
				* validationDataPercentage);
		int runs = messages.size() / validationDataSize;
		LOG.info(
				"validationDataPercentage={}, messages={}, validationDataSize={}, runs={}",
				new Object[] { validationDataPercentage, messages.size(),
						validationDataSize, runs });

		Map<String, List<String>> categoryWords = new WordCategoryMapper()
				.map(messages);
		FeatureCollector featureCollector = new FeatureCollector(categoryWords);
		for (ReutersMessage message : messages) {
			if (!message.getTopic().isEmpty() && message.getBody() != null) {
				Vector features = featureCollector.extractFeatures(message);
				data.add(new DataPoint(features, message.getTopic()));
			}
		}

		
		List<List<DataPoint>> validationDataSets = new ArrayList<List<DataPoint>>();
		List<DataPoint> trainingData = new ArrayList<DataPoint>(data);
		Random random = new Random();
		for (int r = 0; r < runs; r++) {
			List<DataPoint> validationData = new ArrayList<DataPoint>();
			for (int v = 0; v < validationDataSize; v++) {
				int index = random.nextInt(trainingData.size());
				validationData.add(trainingData.remove(index));
			}
			validationDataSets.add(validationData);
			LOG.info("ValidationData size={}", validationData.size());
		}
		
		List<String> categories = new CategoriesExtractor().extract(data);
		ConfusionMatrix overallMatrix = new ConfusionMatrix(categories,
				"unknown");
		List<ConfusionMatrix> confusionMatrixes = new ArrayList<ConfusionMatrix>();
		for (List<DataPoint> validationDataSet : validationDataSets) {
			confusionMatrixes.add(execute(data, validationDataSet,
					categories, overallMatrix));
		}

		/*
		 * System.out
		 * .println("#####################################################");
		 * for (ConfusionMatrix cf : confusionMatrixes) { System.out.println(new
		 * ConfusionMatrixFormatter(cf).withMatrix()); System.out
		 * .println("#####################################################"); }
		 */
		System.out.println(new ConfusionMatrixFormatter(overallMatrix)
				.withMatrix().withAccurency());
		int totalOccurrency = 0;
		int totalCorrect = 0;
		for(String label : overallMatrix.getLabels()){
			totalCorrect += overallMatrix.getCorrect(label);
			totalOccurrency += overallMatrix.getTotal(label);
		}
		System.out.println("Total accuracy = " + (double)totalCorrect/totalOccurrency);

	}

	private ConfusionMatrix execute(List<DataPoint> data,
			List<DataPoint> validationDataSet, List<String> categories,
			ConfusionMatrix overallMatrix) {
		List<DataPoint> trainingData = new ArrayList<DataPoint>(data);
		trainingData.removeAll(validationDataSet);
		LOG.info("Training dataset={}, validaton dataset={}", trainingData.size(), validationDataSet.size());
		Classifier classifier = new LogisticRegression(trainingData);
		LOG.info("Starting training.");
		classifier.train();
		LOG.info("Training done.");

		ConfusionMatrix confusionMatrix = new ConfusionMatrix(categories,
				"unknown");
		LOG.info("");
		for (DataPoint dp : validationDataSet) {
			String expectedTarget = dp.getCategory();
			String actualTarget = classifier.classify(dp.getFeatures());
			confusionMatrix.addInstance(expectedTarget, actualTarget);
			overallMatrix.addInstance(expectedTarget, actualTarget);
		}
		if (logConfusionMatrix) {
			System.out.println(new ConfusionMatrixFormatter(confusionMatrix).withAccurency());
		}
		return confusionMatrix;

	}

	public static void main(String[] args) throws ParseException, IOException {
		String dataDir = null;
		if (args.length == 0) {
			dataDir = "/home/cloudera/workspace/reuters/reutersMahout/Data";
		} else {
			dataDir = args[0];
		}
		List<ReutersMessage> messages = new MessageExtractor().extract(dataDir);
		new CrossValidator(0.1).execute(messages);
	}
}
