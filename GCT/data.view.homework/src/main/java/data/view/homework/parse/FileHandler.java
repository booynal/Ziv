package data.view.homework.parse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileHandler {
	
	public static final String dir = "dataview";

	private static final String CHARSET = "UTF-8";
	
	private FileOutputStream fileOutputStream;

	public FileHandler(String dataFileName) throws FileNotFoundException {
		File file = new File(dir, dataFileName);
		fileOutputStream = new FileOutputStream(file);
	}
	
	public FileHandler(String dataFileName, boolean append) throws FileNotFoundException {
		File file = new File(dir, dataFileName);
		fileOutputStream = new FileOutputStream(file, append);
	}
	
	public void write(String data) {
		BufferedWriter bufferedWriter = null;
		try {
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream, CHARSET));
			bufferedWriter.append(data);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != bufferedWriter) {
				try {bufferedWriter.close();} catch(Exception e) {}
			}
		}
	}
	

}
