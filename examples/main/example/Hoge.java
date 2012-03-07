package main.example;

import java.util.List;

public class Hoge {

	String str1;

	Fuga fuga;

	List<String> hogelist;

	public String toString() {
		if (fuga != null) {
			return str1 + " " + fuga.str2 + " " + hogelist;
		}
		return str1 + " " + null + " " + hogelist;
	}
}
	