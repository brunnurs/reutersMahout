package com.zuehlke.reuters.storm;

import java.io.File;
import java.util.Scanner;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

import com.zuehlke.reuters.storm.bolt.ClassifierBolt;
import com.zuehlke.reuters.storm.bolt.PrintBolt;
import com.zuehlke.reuters.storm.spout.RawDocumentSpout;


public class ReutersTopology {

	private static int WORKERS = 4;

	public static StormTopology buildRawDataTopology(File inputDir){
		Scanner scan = new Scanner(System.in);
		System.out.println("Select run mode, [1] interactive or [2] batch: ");
		
		TopologyBuilder builder = new TopologyBuilder();
		switch (Integer.parseInt(scan.nextLine())) {
		case 1:
			builder.setSpout("keyboard", new KeyboardSpout(), 1);
			builder.setBolt("classifier", new ClassifierBolt(), WORKERS).shuffleGrouping("keyboard");
			break;

		default:
			builder.setSpout("document", new RawDocumentSpout(inputDir), 1);
			builder.setBolt("classifier", new ClassifierBolt(), WORKERS).shuffleGrouping("document");
			break;
		}
		
		builder.setBolt("print", new PrintBolt(), 1).shuffleGrouping("classifier");
		return builder.createTopology();
	}
	
	public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException{
		Config config = new Config();
		config.setDebug(false);
		config.setNumWorkers(WORKERS);
		config.setMaxSpoutPending(1);
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("reutersTopology", config, buildRawDataTopology(new File("/home/cloudera/workspace/reutersMahout/Data")));
		Utils.sleep(60000);
		cluster.killTopology("reutersTopology");
        cluster.shutdown();
	}
}
