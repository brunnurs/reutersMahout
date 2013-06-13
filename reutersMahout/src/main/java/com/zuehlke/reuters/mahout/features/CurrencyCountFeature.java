package com.zuehlke.reuters.mahout.features;

import org.apache.commons.lang.StringUtils;
import org.apache.mahout.math.Vector;
import org.apache.mahout.vectorizer.encoders.FeatureVectorEncoder;
import org.apache.mahout.vectorizer.encoders.StaticWordValueEncoder;

public class CurrencyCountFeature extends AbstractFeature {

	public void extract(String text, Vector vector) {
		
		FeatureVectorEncoder encoder = new StaticWordValueEncoder("text");
		
		countSymbol(text, "£", vector, encoder);
		countSymbol(text, "€", vector, encoder);
		countSymbol(text, "¥", vector, encoder);
			
	}

	private void countSymbol(String text, String symbol, Vector vector, FeatureVectorEncoder encoder) {
		for (int i = 0 ; i < StringUtils.countMatches(text, symbol) ; i++) {
			encoder.addToVector(symbol, 1, vector);     
		}
	}
	

}
