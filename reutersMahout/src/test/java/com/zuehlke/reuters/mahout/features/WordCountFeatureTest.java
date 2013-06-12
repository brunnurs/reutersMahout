package com.zuehlke.reuters.mahout.features;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.junit.Test;
import static org.junit.Assert.*;

public class WordCountFeatureTest {

	@Test
	public void testTextWithNoNumbers() {
		NumberCountFeature feature = new NumberCountFeature();
		Vector v1 = new RandomAccessSparseVector(10);                   
		feature.extract("to to two words", v1);
		assertEquals("{}", v1.toString());
	}

	@Test
	public void testTextWithNumbers() {
		NumberCountFeature feature = new NumberCountFeature();
		Vector v1 = new RandomAccessSparseVector(10);                   
		feature.extract("to 23 two     56 words", v1);
		assertEquals("{4:1.0,3:1.0,2:1.0,0:1.0}", v1.toString());
	}
	
}