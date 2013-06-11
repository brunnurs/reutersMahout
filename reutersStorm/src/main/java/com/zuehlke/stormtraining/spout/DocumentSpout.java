package com.zuehlke.stormtraining.spout;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import com.google.common.io.Files;


public class DocumentSpout extends BaseRichSpout {
	private static final long serialVersionUID = -5786029654629110601L;
	private SpoutOutputCollector collector;
	private File[] files;
	private int nextFile;
	
	public DocumentSpout(File[] folder) {
		this.files = folder;
		nextFile = 0;
	}

	@Override
	public void open(@SuppressWarnings("rawtypes") Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
		
	}

	@Override
	public void nextTuple() {
		File file = files[nextFile++];
		
		try {
			String document = Files.toString(file, Charset.forName("UTF-8"));
			collector.emit(new Values(document));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("document"));
	}
}
