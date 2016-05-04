package tools;

import java.io.File;
import java.util.List;

public class FileBean {
	
	private String fileName;
	private List<String[]> filedata;
	private File file ;
	
	public String getFileName() {
		return fileName;
	}
	public List<String[]> getFiledata() {
		return filedata;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public void setFiledata(List<String[]> filedata) {
		this.filedata = filedata;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}

	@Override
	public String toString() {
		
		String returnstr =  "fileName:"+fileName +"\n"
				+"filedata: "+"\n";
		StringBuilder sb = new StringBuilder();
		for (String[] strings : filedata) {
			for (String string : strings) {
				sb.append(string).append("|+|");
			}
			sb.append("\n");
		}
		returnstr += sb.toString();
		return returnstr;
	}
	

}
