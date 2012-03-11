package s.n.testngppoi.util;

import java.io.File;

import s.n.testngppoi.exception.InvalidFileException;

public class FileUtil {

	/**
	 * 
	 * 
	 * @param dirPath
	 * @param fileName
	 * @return
	 */
	public static String getFilePath(final String dirPath, final String fileName) {
		if (dirPath == null || dirPath.length() == 0) {
			return fileName;
		}
		return dirPath + File.separator + (fileName == null ? "" : fileName);
	}

	public static File getFile(final String filePath) {
		if (filePath == null) {
			throw new NullPointerException("The argument [filePath] is null.");
		}
		final File file = new File(filePath);
		if (isInvalidFile(file)) {
			throw new InvalidFileException("The file ["
					+ file.getAbsolutePath()
					+ "] is not a file or can not read.");
		}
		return file;
	}

	public static boolean isValidFile(final File file) {
		if (file == null) {
			throw new NullPointerException("The argument [file] is null.");
		}
		return file.isFile() && file.canRead();
	}

	public static boolean isInvalidFile(final File file) {
		return isValidFile(file) == false;
	}
}
