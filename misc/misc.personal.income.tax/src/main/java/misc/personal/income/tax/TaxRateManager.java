package misc.personal.income.tax;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import misc.personal.income.tax.model.TaxRate;
import misc.personal.income.tax.model.TaxRateRange;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class TaxRateManager {
	
	private static List<TaxRate> taxs;
	
	static {
		loadTax();
	}

	private static void loadTax() {
		try {
			URL url = ClassLoader.getSystemResource("tax");
			CSVParser csvParser = CSVParser.parse(url, Charset.defaultCharset(), CSVFormat.DEFAULT);
			taxs = parse(csvParser);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static List<TaxRate> parse(Iterable<CSVRecord> iterable) {
		List<TaxRate> taxs = new ArrayList<TaxRate>();
		for (CSVRecord record : iterable) {
			BigDecimal start = new BigDecimal(record.get(0));
			BigDecimal end = new BigDecimal(record.get(1));
			BigDecimal rate = new BigDecimal(record.get(2));
			TaxRateRange range = new TaxRateRange(start, end);
			TaxRate tax = new TaxRate(range, rate);
			taxs.add(tax);
		}
		return taxs;
	}
	
	
	public static List<TaxRate> getTaxs() {
		return taxs;
	}
	

	public static void main(String[] args) {
		System.out.println(taxs);
		for (TaxRate tax : taxs) {
			System.out.println(tax);
		}
	}
	
}
