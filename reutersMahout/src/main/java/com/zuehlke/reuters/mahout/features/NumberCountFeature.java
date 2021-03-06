package com.zuehlke.reuters.mahout.features;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.mahout.math.Vector;
import org.apache.mahout.vectorizer.encoders.FeatureVectorEncoder;
import org.apache.mahout.vectorizer.encoders.StaticWordValueEncoder;

import com.zuehlke.reuters.mahout.ReutersMessage;

public class NumberCountFeature extends AbstractFeature {

	public void extract(ReutersMessage message, Vector vector) {
		FeatureVectorEncoder encoder = new StaticWordValueEncoder("numberCount");

		StringReader in = new StringReader(message.getBody());
		TokenStream ts = analyzer.tokenStream("body", in);
		TermAttribute termAtt = ts.addAttribute(TermAttribute.class);

		try {
			while (ts.incrementToken()) {
				TypeAttribute type = ts.getAttribute(TypeAttribute.class);
				if (type != null && "<NUM>".equals(type.type()) && !isYear(termAtt.termBuffer())) {
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

	private boolean isYear(char[] termBuffer) {
		if (termBuffer.length==4 && (
									(termBuffer[0]=='1' && termBuffer[1]=='9') ||
									(termBuffer[0]=='2' && termBuffer[1]=='0')
									)) return true;
		return false;
	}

}
