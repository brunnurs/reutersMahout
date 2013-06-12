import static org.junit.Assert.*;

import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.junit.Test;

import com.zuehlke.reuters.mahout.features.WordCountFeature;


public class WordCountFeatureTest {

	@Test
	public void test() {
		WordCountFeature wordCountFeature = new WordCountFeature();
		Vector v1 = new RandomAccessSparseVector(10);                   
		wordCountFeature.extract("two words", v1);
	}

}
