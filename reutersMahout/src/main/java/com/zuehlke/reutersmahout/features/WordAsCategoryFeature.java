package com.zuehlke.reutersmahout.features;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.Version;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.vectorizer.encoders.AdaptiveWordValueEncoder;
import org.apache.mahout.vectorizer.encoders.FeatureVectorEncoder;
import org.apache.mahout.vectorizer.encoders.StaticWordValueEncoder;

public class WordAsCategoryFeature implements Feature {

	public void extract( String text, Vector vector ) {
		FeatureVectorEncoder encoder = new AdaptiveWordValueEncoder("text");
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_31);     

		StringReader in = new StringReader( text );
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
