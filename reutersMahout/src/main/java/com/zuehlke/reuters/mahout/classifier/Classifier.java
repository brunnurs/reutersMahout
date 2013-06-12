package com.zuehlke.reuters.mahout.classifier;

import java.util.List;

import org.apache.mahout.math.Vector;

import com.zuehlke.reuters.mahout.DataPoint;

public interface Classifier {
	public void train(List<DataPoint> trainingData);
	public String classify(Vector features);
}
