package com.ziv.office;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.FileNotFoundException;
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
			@SuppressWarnings("resource")
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
		String result = null;
		PDDocument document = null;
		try {
			PDFParser parser = new PDFParser(inputStream);
			parser.parse();
			document = parser.getPDDocument();
			PDFTextStripper stripper = new PDFTextStripper();
			result = stripper.getText(document);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
		return result;
	}

	/**
	 * @param InputStream
	 * @return 读出的Excel的内容
	 */
	@SuppressWarnings({"resource", "deprecation"})
	public static String getTextFromExcel(InputStream InputStream) {
		StringBuffer buff = new StringBuffer();
		try {
			// 创建对Excel工作簿文件的引用
			HSSFWorkbook wb = new HSSFWorkbook(InputStream);
			// 创建对工作表的引用。
			for (int numSheets = 0; numSheets < wb.getNumberOfSheets(); numSheets++) {
				if (null != wb.getSheetAt(numSheets)) {
					HSSFSheet aSheet = wb.getSheetAt(numSheets);// 获得一个sheet
					for (int rowNumOfSheet = 0; rowNumOfSheet <= aSheet.getLastRowNum(); rowNumOfSheet++) {
						if (null != aSheet.getRow(rowNumOfSheet)) {
							HSSFRow aRow = aSheet.getRow(rowNumOfSheet); // 获得一个行
							for (int cellNumOfRow = 0; cellNumOfRow <= aRow.getLastCellNum(); cellNumOfRow++) {
								if (null != aRow.getCell(cellNumOfRow)) {
									HSSFCell aCell = aRow.getCell(cellNumOfRow);// 获得列值
									switch (aCell.getCellType()) {
										case Cell.CELL_TYPE_FORMULA:
											break;
										case Cell.CELL_TYPE_NUMERIC:
											buff.append(aCell.getNumericCellValue()).append('\t');
											break;
										case Cell.CELL_TYPE_STRING:
											buff.append(aCell.getStringCellValue()).append('\t');
											break;
									}
								}
							}
							buff.append(System.lineSeparator());
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buff.toString();
	}

	/**
	 * @param InputStream
	 * @return 读取出的Excle内容
	 */
	@SuppressWarnings("deprecation")
	public static String getTextFromExcel2007(InputStream InputStream) {
		StringBuffer buff = new StringBuffer();
		try {
			// 创建对Excel工作簿文件的引用
			@SuppressWarnings("resource")
			XSSFWorkbook wb = new XSSFWorkbook(InputStream);
			// 创建对工作表的引用。
			for (int numSheets = 0; numSheets < wb.getNumberOfSheets(); numSheets++) {
				if (null != wb.getSheetAt(numSheets)) {
					XSSFSheet aSheet = wb.getSheetAt(numSheets);// 获得一个sheet
					for (int rowNumOfSheet = 0; rowNumOfSheet <= aSheet.getLastRowNum(); rowNumOfSheet++) {
						if (null != aSheet.getRow(rowNumOfSheet)) {
							XSSFRow aRow = aSheet.getRow(rowNumOfSheet); // 获得一个行
							for (int cellNumOfRow = 0; cellNumOfRow <= aRow.getLastCellNum(); cellNumOfRow++) {
								if (null != aRow.getCell(cellNumOfRow)) {
									XSSFCell aCell = aRow.getCell(cellNumOfRow);// 获得列值
									switch (aCell.getCellType()) {
										case Cell.CELL_TYPE_FORMULA:
											break;
										case Cell.CELL_TYPE_NUMERIC:
											buff.append(aCell.getNumericCellValue()).append('\t');
											break;
										case Cell.CELL_TYPE_STRING:
											buff.append(aCell.getStringCellValue()).append('\t');
											break;
									}
								}
							}
							buff.append('\n');
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buff.toString();
	}
}
