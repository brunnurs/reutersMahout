package com.zuehlke.reuters.storm.spout;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import com.zuehlke.reuters.mahout.MessageExtractor;
import com.zuehlke.reuters.mahout.ReutersMessage;
import com.zuehlke.reuters.mahout.importer.ParseException;
import com.zuehlke.reuters.mahout.importer.ReutersMessageImporter;


public class RawDocumentSpout extends BaseRichSpout {
	private static final long serialVersionUID = -5786029654629110601L;
	private SpoutOutputCollector collector;
	private int nextMessage;
	private List<ReutersMessage> messages;
	
	public RawDocumentSpout(File folder) {
		try {
			messages = new MessageExtractor().extract(folder.getAbsolutePath());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collections.shuffle(messages);
		nextMessage = 0;
	}

	public void open(@SuppressWarnings("rawtypes") Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
	}

	public void nextTuple() {
		if(nextMessage < messages.size()){
			ReutersMessage message = messages.get(nextMessage++);
			collector.emit(new Values(message.getId(), message.getBody(), message.getTopic()));
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("name", "text", "class"));
	}
}



