package com.zuehlke.reuters.storm.bolt;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class ExtractionBoltTest {
	@Test
	public void testExtraction(){
		ExtractionBolt bolt = new ExtractionBolt();
		String input = " 7-APR-1987 16:19:28.00\n\nJAPANESE CRUSHERS BUY CANADIAN RAPESEED\n\nJapanese crushers bought 5,000 tonnes of Canadian rapeseed in export business overnight for late May/early June shipment, trade sources said.  Reuter &#3;\n\n";
		assertEquals("Japanese crushers bought 5,000 tonnes of Canadian rapeseed in export business overnight for late May/early June shipment, trade sources said.  Reuter &#3;", bolt.extractBody(input));
	}
}
