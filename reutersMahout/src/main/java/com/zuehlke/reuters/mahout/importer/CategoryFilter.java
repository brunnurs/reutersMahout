package com.zuehlke.reuters.mahout.importer;

import java.util.LinkedList;
import java.util.List;

import com.zuehlke.reuters.mahout.ReutersMessage;

public class CategoryFilter {

	private final static String NON_TOPIC = "NONE";

	public void reduceMessagesToMostImportantTopics(TopicStatistic topicStatistic,List<ReutersMessage> allMessages,int limitCount) {
		List<ReutersMessage> toKeep = new LinkedList<ReutersMessage>();
		for (ReutersMessage message : allMessages) {
			
			int countForThatTopic = topicStatistic.getCategoryStatistics().get(message.getTopic());
			if(countForThatTopic < limitCount) {
				message.setTopic(NON_TOPIC);
			}else{
				toKeep.add(message);
			}
		}
//		allMessages.clear();
//		allMessages.addAll(toKeep);
	}
}
