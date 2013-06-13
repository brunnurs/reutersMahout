package com.zuehlke.reuters.mahout.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.mahout.classifier.ConfusionMatrix;

public class ConfusionMatrixFormatter {

	private ConfusionMatrix confusionMatrix;

	private boolean withMatrix;
	private boolean withAccurency;

	public ConfusionMatrixFormatter(ConfusionMatrix confusionMatrix) {
		this.confusionMatrix = confusionMatrix;
	}

	public ConfusionMatrixFormatter withMatrix() {
		withMatrix = true;
		return this;
	}

	public ConfusionMatrixFormatter withAccurency() {
		withAccurency = true;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (withMatrix) {
			sb.append(confusionMatrix.toString());
			sb.append("\n");
		}
		if (withAccurency) {
			sb.append("Accurancy:\n");
			List<String> labels = new ArrayList<String>(confusionMatrix.getLabels());
			Collections.sort(labels);
			for (String label : labels) {
				sb.append(label).append(": ")
						.append(confusionMatrix.getAccuracy(label))
						.append("\n");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

}
