package com.zuehlke.reuters.mahout.importer;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.zuehlke.reuters.mahout.ReutersMessage;

public class ReutersDataImporterTest {

	ReutersDataImporter importer = new ReutersDataImporter();

	@Test
	public void testImportReutersSample() throws IOException, ParseException {

		/* prepare */
		InputStream resourceAsStream = getClass().getResourceAsStream("/reutersDataExample.xml");
		
		/* perform */
		List<ReutersMessage> parsedMessages = importer.parseData(resourceAsStream);
		
		/* validate */
		Assert.assertEquals(1,parsedMessages.size());
	}
}
