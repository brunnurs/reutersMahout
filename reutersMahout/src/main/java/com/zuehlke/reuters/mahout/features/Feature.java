package com.zuehlke.reuters.mahout.features;

import org.apache.mahout.math.Vector;

import com.zuehlke.reuters.mahout.ReutersMessage;

public interface Feature {
	public void extract( ReutersMessage message, Vector vector );
}
