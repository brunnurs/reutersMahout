package com.zuehlke.reuters.mahout.features;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;

import com.zuehlke.reuters.mahout.ReutersMessage;

public class FeatureCollector {
	public static final int VECTOR_SIZE = 1000;
	private static Set<Feature> features = new HashSet<Feature>();
	
	public FeatureCollector(Map<String, Set<String>> categoryWords){
		features.add( new NumberCountFeature() );
		features.add( new WordCountFeature(categoryWords) );
		features.add( new BiasFeature() );
		features.add( new CurrencyCountFeature() );
		features.add( new AdaptativeWordCountFeature() );
	}
	
	public Vector extractFeatures(ReutersMessage message){
		
		Vector vector = new RandomAccessSparseVector(VECTOR_SIZE);
		for(Feature feature : features){
			feature.extract(message, vector);
		}
		return vector;
	}
	
	public int getFeatureCount() {
		return VECTOR_SIZE;
	}

	public Vector extractFeatures(String text) {
		return extractFeatures(new ReutersMessage(null, null, text));
	}
}
