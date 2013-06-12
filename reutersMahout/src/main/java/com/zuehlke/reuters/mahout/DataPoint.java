package com.zuehlke.reuters.mahout;

import org.apache.mahout.math.Vector;

public class DataPoint {
	private final Vector features;
	private final String category;
	
	public DataPoint(Vector features, String category){
		this.features = features;
		this.category = category;
	}
	
	public Vector getFeatures(){
		return features;
	}
	
	public String getCategory(){
		return category;
	}
}
