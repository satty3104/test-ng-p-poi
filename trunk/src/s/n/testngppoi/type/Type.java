package s.n.testngppoi.type;

public enum Type {

	HSSF(".xls"),
	XSSF(".xlsx");

	private String extension;

	private Type(String extension) {
		this.extension = extension;
	}

	public String getExtension() {
		return extension;
	}
}
