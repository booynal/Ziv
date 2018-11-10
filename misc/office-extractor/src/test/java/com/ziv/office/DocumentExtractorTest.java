package com.ziv.office;


import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 类描述:  文档内容抽取测试类<br/>
 *
 * @auth 张黄江
 * @date 2018/11/9 16:43
 */
public class DocumentExtractorTest {

	@Test
	public void getWordFromDoc() throws FileNotFoundException {
		String name = "/Users/ziv/tmp/test.doc";
		InputStream input = new FileInputStream(name);
		String wordFromDoc = DocumentExtractor.getWordFromDoc(input);
		System.out.println("wordFromDoc: " + wordFromDoc);
	}

	@Test
	public void getWordFromDocx() throws FileNotFoundException {
		InputStream input = new FileInputStream("/Users/ziv/tmp/test.docx");
		String wordFromDocx = DocumentExtractor.getWordFromDocx(input);
		System.out.println("wordFromDocx: " + wordFromDocx);
	}

	@Test
	public void getTextFromPdf() throws FileNotFoundException {
		InputStream input = new FileInputStream("/Users/ziv/Downloads/国内三大通用搜索引擎的垂直搜索功能比较_余元秀.pdf");
		String textFromPdf = DocumentExtractor.getTextFromPdf(input);
		System.out.println("textFromPdf: " + textFromPdf);
	}

	@Test
	public void getTextFromExcel() throws FileNotFoundException {
		InputStream input = new FileInputStream("/Users/ziv/Downloads/模块分配.xls");
		String textFromExcel = DocumentExtractor.getTextFromExcel(input);
		System.out.println("textFromExcel: " + textFromExcel);
	}

	@Test
	public void getTextFromExcel2007() throws FileNotFoundException {
		InputStream input = new FileInputStream("/Users/ziv/Downloads/模块分配.xlsx");
		String textFromExcel2007 = DocumentExtractor.getTextFromExcel2007(input);
		System.out.println("textFromExcel2007: " + textFromExcel2007);
	}
}
