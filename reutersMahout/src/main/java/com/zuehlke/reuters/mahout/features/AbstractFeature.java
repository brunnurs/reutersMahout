package com.zuehlke.reuters.mahout.features;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

public abstract class AbstractFeature implements Feature {
	protected Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_31);    
}
