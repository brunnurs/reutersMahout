package com.zuehlke.reuters.mahout.features;

import java.util.HashSet;
import java.util.Set;

import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;

public class FeatureCollector {
	public static final int VECTOR_SIZE = 1000;
	private static Set<Feature> features = new HashSet<Feature>();
	
	static{
		features.add( new NumberCountFeature() );
		features.add( new WordCountFeature() );
		features.add( new BiasFeature() );
		features.add( new CurrencyCountFeature() );
		features.add( new AdaptativeWordCountFeature() );
	}

	public FeatureCollector(){
	}
	
	public Vector extractFeatures(String text){
		Vector vector = new RandomAccessSparseVector(VECTOR_SIZE);
		for(Feature feature : features){
			feature.extract(text, vector);
		}
		return vector;
	}
	
	public int getFeatureCount() {
		return VECTOR_SIZE;
	}
}
