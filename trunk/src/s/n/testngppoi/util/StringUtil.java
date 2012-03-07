package s.n.testngppoi.util;

import java.util.regex.Pattern;

public class StringUtil {

	public static boolean matches(Pattern p, String target) {
		return p.matcher(target).matches();
	}

	public static boolean notMatches(Pattern p, String target) {
		return matches(p, target) == false;
	}
}
