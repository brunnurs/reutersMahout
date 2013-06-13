package com.zuehlke.reuters.mahout.features;

import org.apache.mahout.math.Vector;
import org.apache.mahout.vectorizer.encoders.ConstantValueEncoder;
import org.apache.mahout.vectorizer.encoders.FeatureVectorEncoder;

import com.zuehlke.reuters.mahout.ReutersMessage;

public class BiasFeature implements Feature {

	@Override
	public void extract(ReutersMessage message, Vector vector) {
		FeatureVectorEncoder bias = new ConstantValueEncoder( "intercept" );
		bias.addToVector((String)null,  1, vector);
	}
}
