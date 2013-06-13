package com.zuehlke.reuters.mahout.classifier;

import java.io.IOException;

import org.apache.mahout.math.Vector;

public interface Classifier {
	String classify(Vector features);
	void writeToFile(String path)  throws IOException;
	void train();
}
