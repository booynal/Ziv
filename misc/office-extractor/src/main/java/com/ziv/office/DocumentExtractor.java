package com.ziv.office;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DocumentExtractor {

	/**
	 * 获取word的doc格式的文档内容
	 *
	 * @return 读出的word的内容
	 */
	public static String getWordFromDoc(InputStream inputStream) {
		try {
			return new WordExtractor(inputStream).getText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取word的doc格式的文档内容
	 *
	 * @return 读出的word2007的内容
	 */
	public static String getWordFromDocx(InputStream inputStream) {
		try {
			StringBuffer stringBuffer = new StringBuffer();
			XWPFDocument doc = new XWPFDocument(inputStream);
			List<XWPFParagraph> paras = doc.getParagraphs();
			for (XWPFParagraph para : paras) {
				stringBuffer.append(para.getText()).append(System.lineSeparator());
			}
			return stringBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取PDF格式的文档内容
	 *
	 * @param inputStream
	 * @return 读出的Pdf的内容
	 */
	public static String getTextFromPdf(InputStream inputStream) {
		PDDocument document = null;
		try {
			PDFParser parser = new PDFParser(inputStream);
			parser.parse();
			document = parser.getPDDocument();
			return new PDFTextStripper().getText(document);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (document != null) {
				try {
					document.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * @param InputStream
	 * @return 读出的Excel的内容
	 */
	public static String getTextFromExcel(InputStream InputStream) {
		Workbook workbook = null;
		try {
			StringBuffer buff = new StringBuffer();
			// 创建对Excel工作簿文件的引用
			workbook = new HSSFWorkbook(InputStream);
			// 遍历工作页
			pickOneBook(buff, workbook);
			return buff.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != workbook) {
				try {
					workbook.close();
				} catch (IOException ignore) {
				}
			}
		}
		return null;
	}

	/**
	 * @param InputStream
	 * @return 读取出的Excle内容
	 */
	public static String getTextFromExcel2007(InputStream InputStream) {
		Workbook workbook = null;
		try {
			StringBuffer buff = new StringBuffer();
			// 创建对Excel工作簿文件的引用
			workbook = new XSSFWorkbook(InputStream);
			// 创建对工作表的引用
			pickOneBook(buff, workbook);
			return buff.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null == workbook) {
				try {
					workbook.close();
				} catch (IOException ignore) {
				}
			}
		}
		return null;
	}

	private static void pickOneBook(StringBuffer buff, Workbook workbook) {
		for (Sheet aSheet : workbook) {
			// 遍历当前页中的每一行
			for (Row aRow : aSheet) {
				// 遍历当前行中的每一列(单元格)
				for (Cell cell : aRow) {
					pickOneCell(buff, cell);
				}
				buff.append(System.lineSeparator());
			}
		}
	}

	private static void pickOneCell(StringBuffer buff, Cell aCell) {
		if (null == aCell) {
			return;
		}

		CellType cellType = aCell.getCellTypeEnum();
		switch (cellType) {
			case FORMULA:
				break;
			case NUMERIC:
				double numericCellValue = aCell.getNumericCellValue();
				buff.append(numericCellValue).append('\t');
				break;
			case STRING:
				String stringCellValue = aCell.getStringCellValue();
				buff.append(stringCellValue).append('\t');
				break;
		}
	}
}
