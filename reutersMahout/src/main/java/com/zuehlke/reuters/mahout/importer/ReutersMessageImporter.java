package com.zuehlke.reuters.mahout.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.zuehlke.reuters.mahout.ReutersMessage;

public class ReutersMessageImporter {
	
	private TopicStatistic statistic;
	
	public ReutersMessageImporter() {
		statistic = new TopicStatistic();
	}

	public List<ReutersMessage> importData(File directory) throws FileNotFoundException, ParseException {
		if(directory.isDirectory()) {
			List<ReutersMessage> result = new ArrayList<ReutersMessage>();
			File[] files = getFilesByExtension(directory);
			for(File f : files) {
				result.addAll(importData(f));
			}
			
			return result;
		} else {
			ReutersMessageParser parser = new ReutersMessageParser(getStatistic());
			return parser.parseData(new FileInputStream(directory));
		}
	}
	

	private File[] getFilesByExtension(File directory) {
		File[] files = directory.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.toLowerCase().endsWith(".sgm");
		    }
		});
		return files;
	}
	
	public TopicStatistic getStatistic() {
		return statistic;
	}
}
