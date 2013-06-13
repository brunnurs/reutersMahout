package com.zuehlke.reuters.mahout.importer;

import java.util.List;

import com.zuehlke.reuters.mahout.ReutersMessage;

public class CategoryFilter {

	private final static String NON_TOPIC = "NONE";

	public void reduceMessagesToMostImportantTopics(TopicStatistic topicStatistic,List<ReutersMessage> allMessages,int limitCount) {
		
		for (ReutersMessage message : allMessages) {
			
			int countForThatTopic = topicStatistic.getCategoryStatistics().get(message.getTopic());
			if(countForThatTopic < limitCount) {
				message.setTopic(NON_TOPIC);
			}
			
		}
		
	}
}
