package s.n.testngppoi.exception;

public class InvalidFileException extends RuntimeException {

	private static final long serialVersionUID = -4084068410793213969L;

	public InvalidFileException() {
		super();
	}

	public InvalidFileException(String message) {
		super(message);
	}

	public InvalidFileException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidFileException(Throwable cause) {
		super(cause);
	}
}
