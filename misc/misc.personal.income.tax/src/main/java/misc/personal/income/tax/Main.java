package misc.personal.income.tax;


public class Main {

	public static void main(String[] args) {
		double calc = new TaxRateHandler().calc(10329);
		System.out.println(calc);
//		print();
		
	}
	
	
	public static void print() {
		TaxRateHandler taxRateHandler = new TaxRateHandler();
		StringBuilder sb = new StringBuilder();
		for (int base = 1000; base < 100000; base += 1000) {
			double calc = taxRateHandler.calc(base);
			sb.append(base + "," + calc + "\r\n");
		}
		System.out.println("=======================================");
		System.out.println(sb.toString());
		
	}

}
