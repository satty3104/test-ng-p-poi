package main.example;

public class Hoge {

	String str1;

	Fuga fuga;

	public String toString() {
		if (fuga != null) {
			return str1 + " " + fuga.str2;
		}
		return str1 + " " + null;
	}
}

class Fuga {
	String str2;
}
