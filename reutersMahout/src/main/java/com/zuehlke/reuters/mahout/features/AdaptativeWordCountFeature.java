package com.zuehlke.reuters.mahout.features;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.mahout.math.Vector;
import org.apache.mahout.vectorizer.encoders.AdaptiveWordValueEncoder;
import org.apache.mahout.vectorizer.encoders.FeatureVectorEncoder;

public class AdaptativeWordCountFeature extends AbstractFeature {

	public void extract(String text, Vector vector) {
		FeatureVectorEncoder encoder = new AdaptiveWordValueEncoder("adaptive words"); 

		StringReader in = new StringReader(text);
		TokenStream ts = analyzer.tokenStream("body", in);
		TermAttribute termAtt = ts.addAttribute(TermAttribute.class);

		try {
			while (ts.incrementToken()) {
			  char[] termBuffer = termAtt.termBuffer();
			  int termLen = termAtt.termLength();
			  String w = new String(termBuffer, 0, termLen);                 
			  encoder.addToVector(w, 1, vector);                                 
			}
		} catch (IOException e) {
			System.out.println("IOError: " + AdaptativeWordCountFeature.class);
		}

	}

}
