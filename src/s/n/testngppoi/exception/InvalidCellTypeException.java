package s.n.testngppoi.exception;

public class InvalidCellTypeException extends Exception {

	private static final long serialVersionUID = 8223903932368224139L;

	public InvalidCellTypeException() {
		super();
	}

	public InvalidCellTypeException(String message) {
		super(message);
	}

	public InvalidCellTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidCellTypeException(Throwable cause) {
		super(cause);
	}
}
