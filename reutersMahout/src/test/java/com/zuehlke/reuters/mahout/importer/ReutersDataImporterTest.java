package com.zuehlke.reuters.mahout.importer;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.zuehlke.reuters.mahout.ReutersMessage;

public class ReutersDataImporterTest {

	ReutersDataImporter importer = new ReutersDataImporter();

	@Test
	public void testImportReutersSample() throws IOException {

		/* prepare */
		String inputAsString = getDataExampleAsString();
		
		/* perform */
		List<ReutersMessage> parsedMessages = importer.parseData(inputAsString);
		
		/* validate */
		assertThat(parsedMessages.size(), is(1));
	}

	private String getDataExampleAsString() throws IOException {
		InputStream resourceAsStream = getClass().getResourceAsStream("/reutersDataExample.xml");
		
		StringWriter writer = new StringWriter();
		IOUtils.copy(resourceAsStream, writer,"UTF-8");
		String inputAsString = writer.toString();
		return inputAsString;
	}

}
