package com.zuehlke.reuters.mahout.features;

import org.apache.mahout.math.Vector;

public interface Feature {
	public void extract( String text, Vector vector );
}
