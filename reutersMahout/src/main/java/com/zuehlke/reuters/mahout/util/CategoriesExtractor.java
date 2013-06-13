package com.zuehlke.reuters.mahout.util;

import java.util.ArrayList;
import java.util.List;

import com.zuehlke.reuters.mahout.DataPoint;

public class CategoriesExtractor {

	public List<String> extract(List<DataPoint> trainingData) {
		List<String> categories = new ArrayList<String>();
		for (DataPoint dataPoint : trainingData) {
			if (!categories.contains(dataPoint.getCategory())) {
				categories.add(dataPoint.getCategory());
			}
		}
		return categories;
	}
}
