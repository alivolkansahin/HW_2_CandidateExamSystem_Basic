package com.volkans.implementations.repository.utilities;

import java.util.InputMismatchException;
import java.util.Scanner;
/**
 * ExamMenuImpl için kullanılan scannerda kullanıcıdan 
 * farklı veri tipi değerler almaya yarayacak methodları içeren Utils Sınıfı.
 * Değerleri alırken try-catch ile hataları yakalayacak ve hatasız işlem yapmak için kullanıcıyı zorlayacak.
 * 
 * Aynı zamanda static final renk kodlarını da içerir!
 * 
 */
public final class Utils {
	public static final String RESET = "\033[0m";  // RESET COLOR
	public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
	public static final String RED_BOLD_BRIGHT = "\033[1;91m";   // BOLD & BRIGHT RED
    public static final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // BOLD & BRIGHT GREEN
    public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// BOLD & BRIGHT YELLOW
    
    private static Scanner scanner = new Scanner(System.in);
	
	private static int intValue;
	private static long longValue;
	private static double doubleValue;
	private static String stringValue;
	
	public static String getStringValue(String message) {
		System.out.print(message);
		stringValue = scanner.nextLine();
		return stringValue;
	}
	
	public static String getStringValueForExam(String message) {
		while(true) {
			System.out.print(message);
			stringValue = scanner.nextLine();
			if(stringValue.equalsIgnoreCase("A") || stringValue.equalsIgnoreCase("B") || 
					stringValue.equalsIgnoreCase("C") || stringValue.equalsIgnoreCase("D")) {
				return stringValue;
			}
			else {
				System.out.println(RED + "Invalid input! Please enter your input in 'a/A' 'b/B' 'c/C' 'd/D' format" + RESET);
				continue;
			}
		}
		
	}

	public static long getLongValue(String message) { // try-catch denemesi
		while(true) {
			try {
				System.out.print(message);
				longValue = scanner.nextLong(); scanner.nextLine();
				break;
			} catch (InputMismatchException e) {
				System.out.println(RED + "Çok uzun veya string bir değer girdiniz!" + RESET);
				scanner.nextLine();
				continue;		
			}
		}
		return longValue;		
	}
	
	public static int getIntegerValue(String message) { // try-catch denemesi
		while(true) {
			try {
				System.out.print(message);
				intValue = scanner.nextInt(); scanner.nextLine();
				break;
			} catch (InputMismatchException e) {
				System.out.println(RED + "Integer değer girilmedi !" + RESET);
				scanner.nextLine();
				continue;
			}
		}
		return intValue;
	}
	
	public static double getDoubleValue(String message) { // try-catch denemesi
		while(true) {
			try {
				System.out.print(message);
				doubleValue = scanner.nextDouble(); scanner.nextLine();
				break;
			} catch (InputMismatchException e) {
				System.out.println(RED + "Double değer girilmedi !" + RESET);
				scanner.nextLine();
				continue;
			}
		}
		return doubleValue;
	}
	
}
