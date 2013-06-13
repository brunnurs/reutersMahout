package com.zuehlke.reuters.mahout.importer;

import java.util.HashMap;
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
	
	public static TopicStatistic createNewStatistic(List<ReutersMessage> messages) {
		
		TopicStatistic newStatistic = new TopicStatistic();
		
		for (ReutersMessage reutersMessage : messages) {
			newStatistic.addToStatistics(reutersMessage);
		}
		
		return newStatistic;
		
	}

	
}
