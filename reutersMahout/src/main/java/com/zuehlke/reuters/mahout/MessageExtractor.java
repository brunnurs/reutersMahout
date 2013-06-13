package com.zuehlke.reuters.mahout;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import com.zuehlke.reuters.mahout.importer.ParseException;
import com.zuehlke.reuters.mahout.importer.ReutersMessageImporter;

public class MessageExtractor {
	
	public List<ReutersMessage> extract(String dataDir) throws FileNotFoundException, ParseException {
		ReutersMessageImporter importer = new ReutersMessageImporter();
		List<ReutersMessage> messages = importer.importData( new File(dataDir) );
		importer.printStatistics();
		return messages;
	}

}
