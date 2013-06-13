package com.zuehlke.reuters.mahout.preprocess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import com.google.common.collect.Maps;
import com.zuehlke.reuters.mahout.ReutersMessage;

public class WordCategoryMapper {
	
	private static final int NUMBER_TOP_WORDS = 25;

	public Map<String, List<String>> map(List<ReutersMessage> messages) {
		Map<String, Map<String, Integer>> tmpMap = Maps.newHashMap();

		for (ReutersMessage msg : messages) {

			Map<String, Integer> wordMap = tmpMap.get(msg.getTopic());
			if (wordMap == null) {
				wordMap = new HashMap<String, Integer>();

				tmpMap.put(msg.getTopic(), wordMap);
			}

			StringTokenizer tokenizer = new StringTokenizer(msg.getBody());
			while (tokenizer.hasMoreTokens()) {
				String word = tokenizer.nextToken();
				Integer wordCount = wordMap.get(word);
				if (wordCount == null)
					wordMap.put(word, 1);
				else
					wordMap.put(word, wordCount + 1);
			}
		}

		HashMap<String, List<String>> topMap = Maps.newHashMap();
		for (String category : tmpMap.keySet()) {
			Map<String, Integer> unorderedMap = tmpMap.get(category);
			Map<String, Integer> sortedMap = sort(unorderedMap);
			topMap.put(category, extractList(sortedMap));
		}

		return topMap;
	}

	private List<String> extractList(Map<String, Integer> sortedMap) {
		List<String> list = new ArrayList<String>();
		
		List<Entry<String, Integer>> sortedList = new ArrayList<Entry<String, Integer>>(sortedMap.entrySet());
		int count = NUMBER_TOP_WORDS;
		for (int i = sortedList.size() - 1; i >= 0 || count >= 0; i--) {
			Entry<String, Integer> entry = sortedList.get(i);
			list.add(entry.getKey());
			count--;
		}
		return list;
	}

	private static Map<String, Integer> sort(Map<String, Integer> unsortMap) {

		List list = new LinkedList(unsortMap.entrySet());

		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue())
						.compareTo(((Map.Entry) (o2)).getValue());
			}
		});

		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put((String) entry.getKey(), (Integer) entry.getValue());
		}
		return sortedMap;
	}

}
