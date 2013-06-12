package com.zuehlke.reuters.mahout.features;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.Version;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.vectorizer.encoders.AdaptiveWordValueEncoder;
import org.apache.mahout.vectorizer.encoders.ContinuousValueEncoder;
import org.apache.mahout.vectorizer.encoders.FeatureVectorEncoder;
import org.apache.mahout.vectorizer.encoders.StaticWordValueEncoder;

public class TFIDFFeature implements Feature {
	
	private static final String WORD_COUNT_FILE = "wordCount.txt";
	Map<String, Double> idf;
	
	public TFIDFFeature() throws IOException{
		idf = new HashMap<String, Double>();
		BufferedReader reader = new BufferedReader( new InputStreamReader( getClass().getResourceAsStream(WORD_COUNT_FILE) ) );
		for( String line = reader.readLine(); line != null; line = reader.readLine() ){
			String[] parts = line.split(" ");
			idf.put(parts[0], Double.valueOf(parts[1]));
		}
	}

	public void extract( String text, Vector vector ) {
		ContinuousValueEncoder encoder = new ContinuousValueEncoder("td-idf");
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_31);     
		Map<String, Integer> wordCounts = new HashMap<String, Integer>();
		
		StringReader in = new StringReader( text );
		TokenStream ts = analyzer.tokenStream("body", in);
		TermAttribute termAtt = ts.addAttribute(TermAttribute.class);

		try {
			while (ts.incrementToken()) {
			  char[] termBuffer = termAtt.termBuffer();
			  int termLen = termAtt.termLength();
			  String word = new String(termBuffer, 0, termLen);
			  if(!word.isEmpty() && idf.containsKey(word) ){
				  if(wordCounts.containsKey(word)){
					  wordCounts.put(word, wordCounts.get(word)+1);
				  } else {
					  wordCounts.put(word, 1);
				  }
			  }
			}
			double tfidf = 0;
			for( String word : idf.keySet() ){
				if( wordCounts.containsKey(word) ){
					tfidf += wordCounts.get(word)*idf.get(word);
				}
			}
			encoder.addToVector("tfidf", tfidf, vector);   
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
