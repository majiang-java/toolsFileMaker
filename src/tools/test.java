package tools;

public class test {

	public static void main(String[] args) {
		String str = "2012123_042_03_LoanApply.txt";
		System.out.println(str.substring(0, str.lastIndexOf("_")));
		System.out.println(str.substring(str.lastIndexOf("_")));
		
		System.out.println(str.replaceAll("[0-9]{7}_[0-9]{3}_[0-9]{2}", ""));
	}
}
