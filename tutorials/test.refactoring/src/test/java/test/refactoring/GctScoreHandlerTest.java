package test.refactoring;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.booynal.test.refactoring.gct.GctScoreHandler;

public class GctScoreHandlerTest {

	
	@BeforeClass
	public static void setup() {
		
	}
	
	@Test
	public void testCalc() throws IOException, URISyntaxException {
		List<String> standardAnswers = Files.readAllLines(Paths.get(ClassLoader.getSystemResource("0").toURI()), Charset.defaultCharset());
		List<String> myAnswers = Files.readAllLines(Paths.get(ClassLoader.getSystemResource("1").toURI()), Charset.defaultCharset());
		int[] wight = new int[] {2, 4, 2, 2};
		GctScoreHandler handler = new GctScoreHandler();
		int[] scores = handler.calc(standardAnswers, myAnswers, wight);
		Assert.assertArrayEquals(new int[]{50, 60, 32, 52, 194}, scores);
	}
	
}
