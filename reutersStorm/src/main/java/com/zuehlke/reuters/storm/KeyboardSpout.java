package com.zuehlke.reuters.storm;

import java.util.Map;
import java.util.Scanner;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class KeyboardSpout extends BaseRichSpout implements IRichSpout {

	private static final long serialVersionUID = 7823391573684497629L;
	private SpoutOutputCollector collector;

	public void open(@SuppressWarnings("rawtypes") Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
	}

	public void nextTuple() {
		Scanner scan = new Scanner(System.in);
		while (scan.hasNext()) {
			collector.emit(new Values(Long.toString(System.currentTimeMillis()), scan.nextLine(), "manual"));
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("name", "text", "class"));
	}

}
