package com.zuehlke.reuters.mahout.preprocess;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.google.common.collect.Maps;
import com.zuehlke.reuters.mahout.ReutersMessage;

public class WordCategoryMapper {

	public Map<String, List<String>> map(List<ReutersMessage> messages) {
		HashMap<String, List<String>> map = Maps.newHashMap();
		
		for (ReutersMessage msg : messages) {
			StringTokenizer tokenizer = new StringTokenizer(msg.getBody());
			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();
			}
		}
		
		return map;
	}
}
