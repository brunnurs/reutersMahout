package com.zuehlke.reuters.mahout.features;

import org.apache.commons.lang.StringUtils;
import org.apache.mahout.math.Vector;
import org.apache.mahout.vectorizer.encoders.FeatureVectorEncoder;
import org.apache.mahout.vectorizer.encoders.StaticWordValueEncoder;

import com.zuehlke.reuters.mahout.ReutersMessage;

public class CurrencyCountFeature extends AbstractFeature {

	public void extract(ReutersMessage message, Vector vector) {
		
		FeatureVectorEncoder encoder = new StaticWordValueEncoder("Currency");
		
		String text = message.getBody();
		
		countSymbol(text, "£", vector, encoder);
		countSymbol(text, "€", vector, encoder);
		countSymbol(text, "¥", vector, encoder);
		countSymbol(text, "pct", vector, encoder);
			
	}

	private void countSymbol(String text, String symbol, Vector vector, FeatureVectorEncoder encoder) {
		for (int i = 0 ; i < StringUtils.countMatches(text, symbol) ; i++) {
			encoder.addToVector(symbol, 1, vector);     
		}
	}
	

}
