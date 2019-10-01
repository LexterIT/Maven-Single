package com.lexter.sample;
import java.util.Scanner; 
import org.apache.commons.lang3.StringUtils;

public class ScannerUtil {

	private static Scanner scanner = new Scanner(System.in);

	public static int getInputInt(String message) {
		int input = 0;
		int limit = 0;
		boolean valid = false;
		System.out.println(message);
		while(valid == false) {
			try {
				input = Integer.parseInt(scanner.nextLine());
				valid = true;
			} catch(NumberFormatException e) {
				System.out.println("Please only enter numbers!");
				valid = false;
			}
		}
		return input;
	}

	public static String getInputString(String message) {
		String input = "";
		System.out.println(message);
		input = scanner.nextLine();
		return input;
	}

}