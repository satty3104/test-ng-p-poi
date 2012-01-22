package main;

public class MyStringUtils {
	public String concat(String str1, String delim, String str2) {
		if (str1 == null || str1.length() == 0) {
			if (str2 == null || str2.length() == 0) {
				return "";
			}
			return str2;
		}
		if (str2 == null || str2.length() == 0) {
			return str1;
		}
		if (delim == null || delim.length() == 0) {
			return str1 + str2;
		}
		return str1 + delim + str2;
	}
}
