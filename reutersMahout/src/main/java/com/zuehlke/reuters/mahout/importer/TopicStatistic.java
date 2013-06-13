package com.zuehlke.reuters.mahout.importer;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.zuehlke.reuters.mahout.ReutersMessage;

public class TopicStatistic {

	private Map<String, Integer> categoryStatistics = new HashMap<String, Integer>();

	public void addToStatistics(ReutersMessage reutersMessage) {
		
		Integer countForCategory = categoryStatistics.get(reutersMessage.getTopic());
		if(countForCategory == null) {
			categoryStatistics.put(reutersMessage.getTopic(), 1);
		} else {
			countForCategory++;
			categoryStatistics.put(reutersMessage.getTopic(), countForCategory);
		}
	}
	
	public Map<String, Integer> getCategoryStatistics() {
		return categoryStatistics;
	}
	
	public void printStatistics() {
		
		MapUtil mapUtil = new MapUtil();
		Map<String, Integer> sortedMap = mapUtil.sortByValue(categoryStatistics);
		
		for (String key : sortedMap.keySet()) {
			System.out.println("Topic: "+key + " Count:"+sortedMap.get(key));
		}
		
		System.out.println("");
		System.out.println("");
		System.out.println("");
	}
	
	public static TopicStatistic createNewStatistic(List<ReutersMessage> messages) {
		
		TopicStatistic newStatistic = new TopicStatistic();
		
		for (ReutersMessage reutersMessage : messages) {
			newStatistic.addToStatistics(reutersMessage);
		}
		
		return newStatistic;
		
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
