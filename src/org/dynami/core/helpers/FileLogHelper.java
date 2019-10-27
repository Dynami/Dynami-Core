package org.dynami.core.helpers;

import java.io.File;
import java.io.RandomAccessFile;

public class FileLogHelper {
	private final String delimeter;
	private final RandomAccessFile file;
	
	public FileLogHelper(File f, String delimeter, boolean append) throws Exception {
		this.delimeter = delimeter;
		
		if(!append && f.exists()) {
			f.delete();
		}
		
		this.file = new RandomAccessFile(f, "w");
		
		if(append) {
			file.seek(file.length()-1);
		}
	}
	
	public void header(String...fields) throws Exception {
		for(String field : fields) {
			file.writeChars(field);
			file.writeChars(delimeter);
		}
		file.writeChar('\n');
	}
	
	public void write(Object...values) throws Exception {
		for(Object value : values) {
			file.writeChars(String.valueOf(value));
			file.writeChars(delimeter);
		}
		file.writeChar('\n');
	}
	
	public void close() throws Exception {
		file.close();
	}
}
