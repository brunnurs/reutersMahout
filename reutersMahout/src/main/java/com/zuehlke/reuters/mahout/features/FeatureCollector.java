package com.zuehlke.reuters.mahout.features;

import java.util.HashSet;
import java.util.Set;

import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;

public class FeatureCollector {
	private static final int VECTOR_SIZE = 1000;
	private static Set<Feature> features;
	
	static{
		features.add( new NumberCountFeature() );
		features.add( new WordCountFeature() );
		features.add( new BiasFeature() );
	}

	public FeatureCollector(){
		features = new HashSet<Feature>();
	}
	
	public Vector extractFeatures(String text){
		Vector vector = new RandomAccessSparseVector(VECTOR_SIZE);
		for(Feature feature : features){
			feature.extract(text, vector);
		}
		return vector;
	}
}
