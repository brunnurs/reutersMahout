package com.zuehlke.reuters.mahout.features;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.mahout.math.Vector;
import org.apache.mahout.vectorizer.encoders.FeatureVectorEncoder;
import org.apache.mahout.vectorizer.encoders.StaticWordValueEncoder;

public class NumberCountFeature extends AbstractFeature {

	public void extract(String text, Vector vector) {
		FeatureVectorEncoder encoder = new StaticWordValueEncoder("text");

		StringReader in = new StringReader(text);
		TokenStream ts = analyzer.tokenStream("body", in);
		TermAttribute termAtt = ts.addAttribute(TermAttribute.class);

		try {
			while (ts.incrementToken()) {
				TypeAttribute type = ts.getAttribute(TypeAttribute.class);
				if (type != null && "<NUM>".equals(type.type())) {
					char[] termBuffer = termAtt.termBuffer();
					int termLen = termAtt.termLength();
					String w = new String(termBuffer, 0, termLen);
					encoder.addToVector(w, 1, vector);
				}
			}
		} catch (IOException e) {
			System.out.println("IOError: " + NumberCountFeature.class);
		}
	}

	private boolean isNumeric(char[] word) {
		for (char c : word) {
			if (c != 0 && !Character.isDigit(c))
				return false;
		}
		return true;
	}

}
