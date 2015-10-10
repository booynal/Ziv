package misc.personal.income.tax.model;

import java.math.BigDecimal;

public class TaxRate {
	
	private TaxRateRange taxRange;
	private BigDecimal rate;
	
	public TaxRate() {
	}
	public TaxRate(TaxRateRange taxRange, BigDecimal rate) {
		super();
		this.taxRange = taxRange;
		this.rate = rate;
	}
	public TaxRateRange getTaxRange() {
		return taxRange;
	}
	public void setTaxRange(TaxRateRange taxRange) {
		this.taxRange = taxRange;
	}
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	@Override
	public String toString() {
		return "TaxRate [taxRange=" + taxRange + ", rate=" + rate + "]";
	}
	
}
