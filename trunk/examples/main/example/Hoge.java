package main.example;

public class Hoge {

	String str1;

	Fuga fuga;

	public String toString() {
		return str1 + " " + fuga.str2;
	}
}

class Fuga {
	String str2;
}
