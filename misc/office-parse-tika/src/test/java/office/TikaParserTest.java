package office;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 类描述:  Tika文档解-测试类<br/>
 *
 * @auth 张黄江
 * @date 2018/11/10 17:00
 */
public class TikaParserTest {

	@Test
	public void test1_基础解析() throws Exception {
		String filename = "/Users/ziv/tmp/test.doc";
//		String filename = "/Users/ziv/tmp/test.docx";
//		String filename = "/Users/ziv/Downloads/猎你推荐报告-上海猎都网络科技有限公司-UI设计师-刘志楠.doc";
//		String filename = "/Users/ziv/Downloads/国内三大通用搜索引擎的垂直搜索功能比较_余元秀.pdf";
//		String filename = "/Users/ziv/Downloads/模块分配.xls";
//		String filename = "/Users/ziv/Downloads/模块分配.xlsx";

		one(filename);
	}

	private void one(String filename) throws IOException, TikaException {
		long start = System.currentTimeMillis();
		// 据官方文档说此种方式简单，但是效率比下面一种要低
		Tika tika = new Tika();
		Metadata metadata = new Metadata();
		FileInputStream inputStream = new FileInputStream(filename);
		String content = tika.parseToString(inputStream, metadata);
		for (String name : metadata.names()) {
			System.out.println(name + "\t= " + metadata.get(name));
		}
		System.out.println("content: " + content);
		long end = System.currentTimeMillis();
		System.out.println("============");
		System.out.println("cost1: " + (end - start));
	}

	@Test
	public void test2_高效的解析() {
//		String filename = "/Users/ziv/tmp/test.doc";
		String filename = "/Users/ziv/百度云同步盘/Life/myDiary/diary.xlsx";
//		String filename = "/Users/ziv/tmp/test.docx";
//		String filename = "/Users/ziv/Downloads/猎你推荐报告-上海猎都网络科技有限公司-UI设计师-刘志楠.doc";
//		String filename = "/Users/ziv/Downloads/国内三大通用搜索引擎的垂直搜索功能比较_余元秀.pdf";
//		String filename = "/Users/ziv/Downloads/模块分配.xls";
//		String filename = "/Users/ziv/Downloads/模块分配.xlsx";

		two(filename);
	}

	private void two(String filename) {
		InputStream is = null;
		try {
			long start = System.currentTimeMillis();
			Metadata metadata = new Metadata();
			is = new FileInputStream(filename);
			// 据官方文档说此种方式稍复杂，但是效率比上面一种要高
			BodyContentHandler handler = new BodyContentHandler(1000000);
			//自动检测文档类型，自动创建相应的解析器
//			new AutoDetectParser().parse(is, handler, metadata, new ParseContext());
			new AutoDetectParser().parse(is, handler, metadata);
			for (String name : metadata.names()) {
				System.out.println(name + "\t= " + metadata.get(name));
			}
			String content = handler.toString();
			System.out.println("content: " + content);
			System.out.println("============");
			long end = System.currentTimeMillis();
			System.out.println("cost2: " + (end - start));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
