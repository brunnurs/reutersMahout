package com.zuehlke.reuters.storm.bolt;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class ExtractionBolt extends BaseRichBolt {
	private static final long serialVersionUID = -1452219939177487194L;
	private OutputCollector collector;
	
	public void prepare(@SuppressWarnings("rawtypes") Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	public void execute(Tuple input) {
		String document = input.getString(1);
		collector.emit(input, new Values(input.getString(0), extractBody(document)));
		collector.ack(input);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("name", "body"));
	}

	String extractBody(String document) {
		String[] splits = document.split("\\n+");
		return splits[splits.length -1];
	}
}
