package com.zuehlke.reuters.mahout;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import com.zuehlke.reuters.mahout.importer.CategoryFilter;
import com.zuehlke.reuters.mahout.importer.ParseException;
import com.zuehlke.reuters.mahout.importer.ReutersMessageImporter;
import com.zuehlke.reuters.mahout.importer.TopicStatistic;

public class MessageExtractor {
	
	private static final int TOPIC_LIMIT = 500;

	public List<ReutersMessage> extract(String dataDir) throws FileNotFoundException, ParseException {
		ReutersMessageImporter importer = new ReutersMessageImporter();
		List<ReutersMessage> messages = importer.importData( new File(dataDir) );
		importer.getStatistic().printStatistics();

		CategoryFilter categoryFilter = new CategoryFilter();
		categoryFilter.reduceMessagesToMostImportantTopics(importer.getStatistic(), messages, TOPIC_LIMIT);

		TopicStatistic.createNewStatistic(messages).printStatistics();
		
		return messages;
	}

}
