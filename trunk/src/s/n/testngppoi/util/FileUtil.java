package s.n.testngppoi.util;

import java.io.File;

public class FileUtil {

	public static boolean isValidFile(File f) {
		if (f == null) {
			throw new NullPointerException("");
		}
		return f.isFile() && f.canRead();
	}
}
