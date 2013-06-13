package com.zuehlke.reuters.mahout.validation;

import java.io.IOException;
import java.util.ArrayList;
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
				Vector features = featureCollector.extractFeatures(message
						.getBody());
				data.add(new DataPoint(features, message.getTopic()));
			}
		}

		List<String> categories = new CategoriesExtractor().extract(data);
		ConfusionMatrix overallMatrix = new ConfusionMatrix(categories,
				"unknown");
		List<ConfusionMatrix> confusionMatrixes = new ArrayList<ConfusionMatrix>();
		for (int i = 0; i < runs; i++) {
			confusionMatrixes.add(execute(data, validationDataSize, i,
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

	}

	private ConfusionMatrix execute(List<DataPoint> data,
			int validationDataSize, int run, List<String> categories,
			ConfusionMatrix overallMatrix) {
		int validationDataStart = validationDataSize * run;
		int validationDataEnd = validationDataSize * (run + 1);
		LOG.info("Run {}. Validation data {}-{}", new Object[] { run,
				validationDataStart, validationDataEnd });
		Random r = new Random();
		List<DataPoint> trainingData = new ArrayList<DataPoint>(data);
		List<DataPoint> validationData = new ArrayList<DataPoint>();
		for (int i = 0; i < validationDataSize; i++) {
			int index = r.nextInt(trainingData.size());
			validationData.add(trainingData.remove(index));
		}
		LOG.info("trainingData={}, validationData={}", trainingData.size(),
				validationData.size());
		Classifier classifier = new LogisticRegression(trainingData);
		LOG.info("Starting training.");
		classifier.train();
		LOG.info("Training done.");

		ConfusionMatrix confusionMatrix = new ConfusionMatrix(categories,
				"unknown");
		LOG.info("");
		for (DataPoint dp : validationData) {
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
			dataDir = "/home/cloudera/reutersMahout/Data";
		} else {
			dataDir = args[0];
		}
		List<ReutersMessage> messages = new MessageExtractor().extract(dataDir);
		new CrossValidator(0.1).execute(messages);
	}
}
