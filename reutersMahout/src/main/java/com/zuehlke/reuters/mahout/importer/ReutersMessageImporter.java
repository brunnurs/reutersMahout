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
import java.util.Map.Entry;
import java.util.TreeMap;

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
			ReutersMessageParser parser = new ReutersMessageParser(statistic);
			return parser.parseData(new FileInputStream(directory));
		}
	}
	
	public void printStatistics() {
		
		MapUtil mapUtil = new MapUtil();
		Map<String, Integer> sortedMap = mapUtil.sortByValue(statistic.getCategoryStatistics());
		
		for (String key : sortedMap.keySet()) {
			System.out.println("Topic: "+key + " Count:"+sortedMap.get(key));
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
	
	class MapUtil
	{
	    public <K, V extends Comparable<? super V>> Map<K, V> 
	        sortByValue( Map<K, V> map )
	    {
	        List<Map.Entry<K, V>> list =
	            new LinkedList<Map.Entry<K, V>>( map.entrySet() );
	        Collections.sort( list, new Comparator<Map.Entry<K, V>>()
	        {
	            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
	            {
	                return (o1.getValue()).compareTo( o2.getValue() );
	            }
	        } );

	        Map<K, V> result = new LinkedHashMap<K, V>();
	        for (Map.Entry<K, V> entry : list)
	        {
	            result.put( entry.getKey(), entry.getValue() );
	        }
	        return result;
	    }
	}

}
