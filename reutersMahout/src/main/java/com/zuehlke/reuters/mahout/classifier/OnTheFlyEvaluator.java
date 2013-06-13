package com.zuehlke.reuters.mahout.classifier;

public class OnTheFlyEvaluator {
	
	double averageCorrect = 0;
	int count = 0;
	double mu = 0;
	
	

	public void recalculateMu() {
		mu = Math.min(count + 1, 1000);
		count++;
	}

	
	public void calculateAndPrintCorrectness(String predicted, String effectiveCategory) {
		int correct = predicted.equals(effectiveCategory) ? 1 : 0;				
		averageCorrect = averageCorrect + (correct - averageCorrect) / mu;
		System.out.println("Correctness:"+averageCorrect);
	}
	
}
