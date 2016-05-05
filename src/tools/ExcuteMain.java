package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

public class ExcuteMain {
	public static String pathInput = "E:/uftp/input/LoanApply/";
	public static String pathOutPut = "E:/uftp/output/LoanResult/";
	public static String pathSuccess = "E:/uftp/input/SuccessRepayment/";
	public static Logger logger = Logger.getLogger(ExcuteMain.class);
	public static void main(String[] args) {
		logger.info("ִ�п�ʼ");
		pathInput = PropertiesUtil.getProperty("pathInput");
		pathOutPut =  PropertiesUtil.getProperty("pathOutPut");
		pathSuccess =  PropertiesUtil.getProperty("pathSuccess");
		try{
			FileBean fileinBean = initInputFile();
			procOutFile(fileinBean);
			//����succ�ļ�
			procSuccFile(fileinBean);
			
		}catch(Exception e){
			logger.error("ִ�д���",e);
			e.printStackTrace();
		}
		logger.info("ִ�н���");
	}

	private static FileBean initInputFile() {
		File inputPath = new File(pathInput);
		File[] filesInput = inputPath.listFiles();
		if (filesInput == null || filesInput.length == 0) {
			throw new RuntimeException("input�ļ�����û���ļ�");
		}
		sortFile(filesInput);
		File fileInput = filesInput[0];
		List<String[]> indata = readFile(fileInput);
		
		FileBean fileinBean = new FileBean();
		fileinBean.setFileName(fileInput.getName());
		fileinBean.setFiledata(indata);
		logger.info("��ʼ��input" + fileinBean);
		return fileinBean;
	}

	//����out�ļ�
	private static void procOutFile(FileBean fileinBean) throws Exception {
		File outputPath = new File(pathOutPut);
		File[] filesOutput = outputPath.listFiles();
		if (filesOutput == null || filesOutput.length == 0) {
			throw new RuntimeException("input�ļ�����û���ļ�");
		}
		sortFile(filesOutput);
		
		
		File fileOut =  filesOutput[0];
		List<String[]> outdata = readFile(fileOut);
		FileBean fileOutBean = new FileBean();
		fileOutBean.setFileName(fileOut.getName());
		fileOutBean.setFiledata(outdata);
		fileOutBean.setFile(fileOut);
		logger.info("��ʼ��outFile" + fileOutBean);
		processFiledata(fileinBean,fileOutBean);
		//����
		processFileName(fileinBean,fileOutBean);
		//д���ļ�
		logger.info("д��ǰfileOutBean" + fileOutBean);
		writeFile(fileOutBean,1);
	}
	
	//����success�ļ�
	private static void procSuccFile(FileBean fileinBean) throws Exception {
		File successPath = new File(pathSuccess);
		File[] filesSuccPut = successPath.listFiles();
		if (filesSuccPut == null || filesSuccPut.length == 0) {
			throw new RuntimeException("input�ļ�����û���ļ�");
		}
		sortFile(filesSuccPut);
		
		
		File fileSucc =  filesSuccPut[0];
		List<String[]> succdata = readFile(fileSucc);
		FileBean fileSuccBean = new FileBean();
		fileSuccBean.setFileName(fileSucc.getName());
		fileSuccBean.setFiledata(succdata);
		fileSuccBean.setFile(fileSucc);
		
		logger.info("��ʼ��fileSuccBean" + fileSuccBean);
		processFiledata(fileinBean,fileSuccBean);
		//����
		processFileName(fileinBean,fileSuccBean);
		//д���ļ�
		logger.info("д��ǰfileSuccBean" + fileSuccBean);
		writeFile(fileSuccBean,2);
	}

	/**
	 * ��file����
	 * 
	 * @param files
	 */
	
	private static void sortFile(File[] files) {
		Arrays.sort(files, new Comparator<File>() {
			@Override
			public int compare(File file1, File file2) {
				return (int) (file2.lastModified() - file1.lastModified());
			}
		});
	}

	public static List<String[]> readFile(File file) {
		List<String[]> data = new ArrayList<String[]>();
		FileInputStream is = null;
		BufferedReader in = null;
		try {
			is = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(is);
			in = new BufferedReader(isr);
			String line = null;
			while ((line = in.readLine()) != null) {
				if(line ==null || "".equals(line)) continue;
				String[] unit = line.split("\\|\\+\\|");
				data.add(unit);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return data;
	}
	
	
	public static void processFiledata(FileBean filebeanin,FileBean fileBeanout){
		List<String[]> listin = filebeanin.getFiledata();
		List<String[]> listOut = fileBeanout.getFiledata();
		//��ʼ������
		
		if(listOut.size() < listin.size()){
			int count = listin.size() - listOut.size();
			for (int i = 0; i < count; i++) {
				listOut.add(listOut.get(i));
			}
		}else if(listOut.size() > listin.size()){
			for (int i = listOut.size()-1; i >= listin.size(); i--) {
				listOut.remove(listOut.get(i));
			}
		}
		for (int i = 0; i < listin.size(); i++) {
			listOut.get(i)[1] = listin.get(i)[1];
		}
		fileBeanout.setFiledata(listOut);
	}
	
	public static void processFileName(FileBean filebeanin,FileBean fileBeanout){
		
		String nameout = fileBeanout.getFileName();
		String namein = filebeanin.getFileName();
	    String extout = nameout.replaceAll("[0-9]+_[0-9]+_[0-9]+","");
		String newName = namein.substring(0,namein.lastIndexOf("_"))+extout;
		fileBeanout.setFileName(newName);
	}
	
	public static void writeFile(FileBean bean,int flag) throws Exception {
		  FileOutputStream o = null;
		try {
			File file = bean.getFile();
			String content = makeContent(bean.getFiledata(),flag);
			logger.info("д�룺" + bean.getFileName() + " content:" + content);
			deleteFile(file);
			File newFile = null;
			if (!file.exists()) {
				String rootPath = file.getParent();
			    logger.info("�޸�ǰ�ļ������ǣ�"+file.getName());
				newFile = new File(rootPath + File.separator + bean.getFileName());
				file.renameTo(newFile);
				logger.info("�޸ĺ��ļ������ǣ�"+newFile.getName());
				newFile.createNewFile();
			}

			o = new FileOutputStream(newFile);
			o.write(content.getBytes("utf-8"));
		} catch (Exception e) {
			throw new Exception("����д���ļ�����");
		} finally {
			o.close();
		}
	  
	}
	public static String makeContent(List<String[]> data,int flag){
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < data.size(); i++) {
			String[] str = data.get(i);
			for (int j = 0; j < str.length; j++) {
				content.append(str[j]);
				if(j ==str.length-1 &&flag == 2 ){
					
				}else{
					content.append("|+|");
				}
			}
			content.append(System.getProperty("line.separator"));
		}
		logger.info("content:"+content);
		return content.toString();
		
	}
	/**
	 * ɾ���ļ�
	 * @param file
	 * @return
	 */
	public static boolean deleteFile(File file) {  
	     boolean flag = false;  
	    // ·��Ϊ�ļ��Ҳ�Ϊ�������ɾ��  
	    if (file.isFile() && file.exists()) {  
	        file.delete();  
	        flag = true;  
	    }  
	    return flag;  
	}  
}
