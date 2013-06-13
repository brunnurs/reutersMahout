package com.zuehlke.reuters.mahout.features;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.mahout.math.Vector;
import org.apache.mahout.vectorizer.encoders.FeatureVectorEncoder;
import org.apache.mahout.vectorizer.encoders.StaticWordValueEncoder;

import com.zuehlke.reuters.mahout.ReutersMessage;

public class WordCountFeature extends AbstractFeature {

	private Set<String> topWords = new HashSet<String>();

	public WordCountFeature(Map<String, Set<String>> categoryWords) {
		for (Set<String> words : categoryWords.values()) {
			topWords.addAll(words);
		}
	}

	public void extract(ReutersMessage message, Vector vector) {
		FeatureVectorEncoder encoder = new StaticWordValueEncoder("static word"); 

		StringReader in = new StringReader(message.getBody());
		TokenStream ts = analyzer.tokenStream("body", in);
		TermAttribute termAtt = ts.addAttribute(TermAttribute.class);

		try {
			while (ts.incrementToken()) {
			  char[] termBuffer = termAtt.termBuffer();
			  int termLen = termAtt.termLength();
			  String w = new String(termBuffer, 0, termLen);     
			  if (topWords.contains(w)) encoder.addToVector(w, 1, vector);                                 
			}
		} catch (IOException e) {
			System.out.println("IOError: " + WordCountFeature.class);
		}

	}

}
