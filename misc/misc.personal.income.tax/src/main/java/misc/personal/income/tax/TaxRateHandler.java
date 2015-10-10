package misc.personal.income.tax;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import misc.personal.income.tax.model.Consts;
import misc.personal.income.tax.model.TaxRate;
import misc.personal.income.tax.model.TaxRateRange;

public class TaxRateHandler {

	private static final BigDecimal NAGV_ONE = new BigDecimal(-1);

	/**
	 * @param base 扣除三险一金之后剩余的
	 * @return 返回所得税
	 */
	public double calc(double base) {
		BigDecimal base2 = new BigDecimal(base).subtract(new BigDecimal(Consts.START_TAX_FREE));
		if (base2.compareTo(BigDecimal.ZERO) <= 0) {
			return 0D;
		}
		List<TaxRate> taxs = TaxRateManager.getTaxs();
		StringBuilder messageBase = new StringBuilder();
		List<BigDecimal> deltas = new ArrayList<BigDecimal>();
		for (TaxRate tax : taxs) {
			TaxRateRange range = tax.getTaxRange();
			BigDecimal end = range.getEnd();
			end = NAGV_ONE.compareTo(end) == 0 ? base2 : end;
			BigDecimal target = (base2.compareTo(end) > 0 ? end : base2).subtract(range.getStart());
			if (messageBase.length() > 0) {
				messageBase.append(" + ");
			}
			messageBase.append(target);
			deltas.add(target.multiply(tax.getRate()));
			if (base2.compareTo(end) <= 0) {
				break;
			}
		}
		
		BigDecimal sum = BigDecimal.ZERO;
		Iterator<BigDecimal> iterator = deltas.iterator();
		StringBuilder messageTax = new StringBuilder();
		while (iterator.hasNext()) {
			BigDecimal delta = iterator.next();
			sum = sum.add(delta);
			messageTax.append(delta);
			if (iterator.hasNext()) {
				messageTax.append(" + ");
			}
		}
		System.out.println("base: " + base2 + " = " + messageBase.toString());
		System.out.println("tax: " + sum + " = " + messageTax.toString());
		return sum.doubleValue();
	}

	public double testCalc(int base) {
		int base2 = base - Consts.START_TAX_FREE;
		double summaryTax = 0;
		if (base2 > 1500) {
			summaryTax += 1500 * 0.03;
		} else {
			summaryTax += base2 * 0.03;
		}
		if (base2 > 4500) {
			summaryTax += (4500 -1500 ) * 0.1;
		} else {
			summaryTax += (base2 - 1500) * 0.1;
		}
		if (base2 > 9000) {
			summaryTax += (base2 - 9000) * 0.2;
		} else {
			summaryTax += (base2 - 4500) * 0.2;
		}
		System.out.println("total tax=" + summaryTax);
		return summaryTax;
	}

}
