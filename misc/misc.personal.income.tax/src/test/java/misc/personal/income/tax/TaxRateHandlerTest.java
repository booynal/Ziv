package misc.personal.income.tax;

import junit.framework.Assert;


public class TaxRateHandlerTest {
	
	@org.junit.Test
	public void testCalc() {
		TaxRateHandler test = new TaxRateHandler();
		
		Assert.assertEquals(45D, test.calc(5000));
		Assert.assertEquals(55D, test.calc(5100));
		Assert.assertEquals(370.8D, test.calc(8129));
		Assert.assertEquals(810.8D, test.calc(10329));
	}
	
}
