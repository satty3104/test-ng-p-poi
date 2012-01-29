package s.n.testngppoi.util;

import java.io.File;

public class FileUtil {

	public static boolean isValid(File f) {
		if (f == null) {
			throw new NullPointerException("The argument [f] is null.");
		}
		return f.isFile() && f.canRead();
	}

	public static boolean isInvalid(File f) {
		return isValid(f) == false;
	}
}
