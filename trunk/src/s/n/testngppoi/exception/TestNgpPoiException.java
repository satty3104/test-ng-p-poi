package s.n.testngppoi.exception;

public class TestNgpPoiException extends RuntimeException {

	private static final long serialVersionUID = 3232620745810713112L;

	public TestNgpPoiException() {
		super();
	}

	public TestNgpPoiException(String message) {
		super(message);
	}

	public TestNgpPoiException(String message, Throwable cause) {
		super(message, cause);
	}

	public TestNgpPoiException(Throwable cause) {
		super(cause);
	}
}
