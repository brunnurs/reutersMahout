package com.zuehlke.reuters.mahout.features;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.junit.Test;
import static org.junit.Assert.*;

public class WordCountFeatureTest {

	@Test
	public void test() {
		WordCountFeature wordCountFeature = new WordCountFeature();
		Vector v1 = new RandomAccessSparseVector(10);                   
		wordCountFeature.extract("two words", v1);
		assertEquals("{3:1.0,2:2.0,1:1.0}", v1.toString());
	}
		
}
