package com.zuehlke.stormtraining;

import java.io.File;

import com.zuehlke.stormtraining.spout.DocumentSpout;

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
		builder.setSpout("document", new DocumentSpout(inputDir.listFiles()));
		
		return builder.createTopology();
	}
	
	public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException{
		Config config = new Config();
		config.setDebug(true);
		config.setNumWorkers(2); //TODO: ???
		config.setMaxSpoutPending(1); //TODO: ???
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("reutersTopology", config, buildTopology(new File("/home/cloudera/Desktop/reuters21578-out")));
		Utils.sleep(10000);
		cluster.killTopology("reutersTopology");
        cluster.shutdown();
	}
}
