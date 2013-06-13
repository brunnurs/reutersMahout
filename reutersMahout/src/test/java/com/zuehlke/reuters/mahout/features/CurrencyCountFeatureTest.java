package com.zuehlke.reuters.mahout.features;
import static org.junit.Assert.assertEquals;

import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.junit.Test;


public class CurrencyCountFeatureTest {

	@Test
	public void testTextWithNoCurrency() {
		CurrencyCountFeature feature = new CurrencyCountFeature();
		Vector v1 = new RandomAccessSparseVector(10);                   
		feature.extract("to to two words", v1);
		assertEquals("{}", v1.toString());
	}

	@Test
	public void testTextWithCurrency() {
		CurrencyCountFeature feature = new CurrencyCountFeature();
		Vector v1 = new RandomAccessSparseVector(10);                   
		feature.extract("to 23 two £ 56 words €", v1);
		assertEquals("{7:1.0,6:1.0,5:1.0,2:1.0}", v1.toString());
	}

}
