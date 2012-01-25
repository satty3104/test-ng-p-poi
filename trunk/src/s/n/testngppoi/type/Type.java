package s.n.testngppoi.type;

/**
 * Spread Sheet Format (SSF) の各フォーマットを定義する{@code Enum}。
 * 
 * @author s_nagai
 * @since 2012/01/23
 */
public enum Type {

	/** Horrible Spread Sheet Format */
	HSSF(".xls"),

	/** XML Spread Sheet Format */
	XSSF(".xlsx");

	/** 拡張子 */
	private String extension;

	/**
	 * 引数に指定した拡張子を使って、SSF 形式ファイルの定義を生成します。
	 * 
	 * @param extension 拡張子
	 */
	private Type(final String extension) {
		this.extension = extension;
	}

	/**
	 * 拡張子を返します。
	 * 
	 * @return 拡張子（"."を含む）
	 */
	public String getExtension() {
		return extension;
	}
}
