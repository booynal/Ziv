package misc.personal.income.tax;


public class Main {

	public static void main(String[] args) {
		double calc = new TaxRateHandler().calc(10329);
		System.out.println(calc);
	}

}
