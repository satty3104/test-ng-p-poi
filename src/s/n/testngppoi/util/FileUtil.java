package s.n.testngppoi.util;

import java.io.File;

public class FileUtil {

	public static boolean isValidFile(final File f) {
		if (f == null) {
			throw new NullPointerException("The argument [f] is null.");
		}
		return f.isFile() && f.canRead();
	}

	public static boolean isInvalidFile(final File f) {
		return isValidFile(f) == false;
	}
}
