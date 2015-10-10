package misc.personal.income.tax.model;

import java.math.BigDecimal;

public class TaxRateRange {
	
	private BigDecimal start;
	private BigDecimal end;
	
	public BigDecimal getStart() {
		return start;
	}
	public void setStart(BigDecimal start) {
		this.start = start;
	}
	public BigDecimal getEnd() {
		return end;
	}
	public void setEnd(BigDecimal end) {
		this.end = end;
	}
	public TaxRateRange(BigDecimal start, BigDecimal end) {
		super();
		this.start = start;
		this.end = end;
	}
	@Override
	public String toString() {
		return "TaxRateRange [start=" + start + ", end=" + end + "]";
	}

}
