package com.zuehlke.reuters.storm.bolt;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class PrintBolt extends BaseRichBolt {
	private static final long serialVersionUID = 5134793814671192380L;
	private OutputCollector collector;
	private double categorizedText = 0.0;
	private double correctlyCategorizedText = 0.0;

	public void prepare(@SuppressWarnings("rawtypes") Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	public void execute(Tuple input) {
		collector.ack(input);
		
		if(input.getString(1).equals(input.getString(2))){
			correctlyCategorizedText++;
		}
		categorizedText++;
		System.out.println("Accuracy: " + correctlyCategorizedText/categorizedText);
		
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}


}
