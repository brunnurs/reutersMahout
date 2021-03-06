package com.zuehlke.reuters.mahout.importer;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;

import com.zuehlke.reuters.mahout.ReutersMessage;

public class ReutersDataImporterTest {

	@Test
	public void testImportReutersMultipleSamples()
			throws FileNotFoundException, ParseException {

		/* prepare */
		File dir = new File("target/test-classes");
		ReutersMessageImporter importer = new ReutersMessageImporter();

		/* perform */
		List<ReutersMessage> parsedMessages = importer.importData(dir);
		importer.getStatistic().printStatistics();

		/* validate */
		assertEquals(2, parsedMessages.size());

	}
}
