package com.zuehlke.reuters.mahout.features;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.mahout.math.Vector;
import org.apache.mahout.vectorizer.encoders.FeatureVectorEncoder;
import org.apache.mahout.vectorizer.encoders.StaticWordValueEncoder;

public class WordCountFeature extends AbstractFeature {

	private Map<String, List<String>> categoryWords;

	public WordCountFeature(Map<String, List<String>> categoryWords) {
		this.categoryWords = categoryWords;
	}

	public void extract(String text, Vector vector) {
		FeatureVectorEncoder encoder = new StaticWordValueEncoder("static word"); 

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
			System.out.println("IOError: " + WordCountFeature.class);
		}

	}

}
