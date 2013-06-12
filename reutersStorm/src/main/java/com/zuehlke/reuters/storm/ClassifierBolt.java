package com.zuehlke.reuters.storm;

import java.io.IOException;
import java.util.Map;

import org.apache.mahout.math.Vector;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.zuehlke.reuters.mahout.classifier.Classifier;
import com.zuehlke.reuters.mahout.classifier.LogisticRegression;
import com.zuehlke.reuters.mahout.features.FeatureCollector;

public class ClassifierBolt extends BaseRichBolt {
	private static final long serialVersionUID = 5727406562716802109L;
	private OutputCollector collector;
	private Classifier classifier;
	
	public void prepare(@SuppressWarnings("rawtypes") Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		try {
			classifier = LogisticRegression.loadFromFile("");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void execute(Tuple input) {
		String text = input.getString(0);
		FeatureCollector featureCollector = new FeatureCollector();
		Vector featureVector = featureCollector.extractFeatures(text);
		String textClass = classifier.classify(featureVector);
		
		collector.emit(new Values(input.getString(0), textClass));
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("name", "class"));
	}

}
