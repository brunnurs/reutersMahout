package com.zuehlke.reuters.storm;

import java.io.File;

import com.zuehlke.reuters.storm.bolt.ClassifierBolt;
import com.zuehlke.reuters.storm.bolt.ExtractionBolt;
import com.zuehlke.reuters.storm.spout.DocumentSpout;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;


public class ReutersTopology {
	public static StormTopology buildTopology(File inputDir){
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("document", new DocumentSpout(inputDir.listFiles()), 1);
		builder.setBolt("bodyExtraction", new ExtractionBolt(), 2).shuffleGrouping("document");
		builder.setBolt("classifier", new ClassifierBolt(), 1).shuffleGrouping("bodyExtraction");
		
		return builder.createTopology();
	}
	
	public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException{
		Config config = new Config();
		config.setDebug(true);
		config.setNumWorkers(2); //TODO: ???
		config.setMaxSpoutPending(1); //TODO: ???
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("reutersTopology", config, buildTopology(new File("/tmp/reuters21578-out")));
		Utils.sleep(10000);
		cluster.killTopology("reutersTopology");
        cluster.shutdown();
	}
}
