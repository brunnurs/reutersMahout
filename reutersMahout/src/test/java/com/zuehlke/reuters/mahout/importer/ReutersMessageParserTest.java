package com.zuehlke.reuters.mahout.importer;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import com.zuehlke.reuters.mahout.ReutersMessage;

public class ReutersMessageParserTest {

	ReutersMessageParser importer = new ReutersMessageParser(new TopicStatistic());

	@Test
	public void testImportReutersSample() throws IOException, ParseException {

		/* prepare */
		InputStream resourceAsStream = getClass().getResourceAsStream(
				"/reutersDataExample1.sgm");

		/* perform */
		List<ReutersMessage> parsedMessages = importer
				.parseData(resourceAsStream);

		/* validate */
		assertEquals(1, parsedMessages.size());
	}

}
