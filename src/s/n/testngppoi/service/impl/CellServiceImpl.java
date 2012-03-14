package s.n.testngppoi.service.impl;

import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

import s.n.testngppoi.exception.InvalidCellTypeException;
import s.n.testngppoi.service.CellService;

/**
 * SSF形式ファイルのCellに関する処理を行うクラス。
 * 
 * @author s_nagai
 * @since 2012/03/08
 */
public class CellServiceImpl implements CellService {

	/**
	 * {@inheritDoc}<br>
	 * Cellの形式によって以下の型のオブジェクトを返します。<br>
	 * <table border="1">
	 * <tr>
	 * <td>null</td>
	 * <td>{@code null}</td>
	 * </tr>
	 * <tr>
	 * <td>入力値無し</td>
	 * <td>{@code null}</td>
	 * </tr>
	 * <tr>
	 * <td>文字列</td>
	 * <td>{@code String}</td>
	 * </tr>
	 * <tr>
	 * <td>数値</td>
	 * <td>{@code Double}</td>
	 * </tr>
	 * <tr>
	 * <td>真偽値</td>
	 * <td>{@code Boolean}</td>
	 * </tr>
	 * <tr>
	 * <td>上記以外</td>
	 * <td>{@link InvalidCellTypeException}</td>
	 * </tr>
	 * </table>
	 * 
	 * @see s.n.testngppoi.service.CellService#getCellValue(org.apache.poi.ss.usermodel.Cell)
	 */
	@Override
	public Object getCellValue(final Cell cell) throws InvalidCellTypeException {
		if (cell == null) {
			return processNull();
		}
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
			return processBlank(cell);
		case Cell.CELL_TYPE_NUMERIC:
			return processNumeric(cell);
		case Cell.CELL_TYPE_STRING:
			return processString(cell);
		case Cell.CELL_TYPE_BOOLEAN:
			return processBoolean(cell);
		default:
			// TODO 抽象クラスに移動
			throw new InvalidCellTypeException("Invalid cell type. ["
					+ cell.getCellType() + "]");
		}
	}

	/**
	 * Cellが{@code null}であったときの処理を行ないます。
	 * 
	 * @return {@code null}
	 */
	protected Object processNull() {
		return null;
	}

	/**
	 * Cellに値が入力されていなかったときの処理を行ないます。
	 * 
	 * @param cell
	 *            Cell
	 * @return {@code null}
	 */
	protected Object processBlank(final Cell cell) {
		return null;
	}

	/**
	 * Cellの形式が数値であったときの処理を行ないます。
	 * 
	 * @param cell
	 *            Cell
	 * @return Cellの値
	 */
	protected Object processNumeric(final Cell cell) {
		if (DateUtil.isCellDateFormatted(cell)) {
			return processDate(cell);
		}
		return processDouble(cell);
	}

	/**
	 * Cellの形式が日付であったときの処理を行ないます。
	 * 
	 * @param cell
	 *            Cell
	 * @return Cellの値
	 */
	protected Date processDate(final Cell cell) {
		return cell.getDateCellValue();
	}

	/**
	 * Cellの形式が数値であったときの処理を行ないます。
	 * 
	 * @param cell
	 *            Cell
	 * @return Cellの値
	 */
	protected Double processDouble(final Cell cell) {
		return Double.valueOf(cell.getNumericCellValue());
	}

	/**
	 * Cellの形式が文字列であったときの処理を行ないます。<br>
	 * 文字列として"null"が入力されていた場合は{@code null}が、""（空文字）が入力されていた場合は""（空文字）が返却されます。<br>
	 * また、以下のルールに従い、特殊文字が置換されます。
	 * <table border="1">
	 * <tr>
	 * <th>入力値</th>
	 * <th>変換後値</th>
	 * </tr>
	 * <tr>
	 * <td>[TAB]</td>
	 * <td>\t</td>
	 * </tr>
	 * </table>
	 * 
	 * @param cell
	 *            Cell
	 * @return Cellの値
	 */
	protected String processString(final Cell cell) {
		final String cellValue = cell.getRichStringCellValue().getString();
		if ("null".equals(cellValue)) {
			return null;
		}
		if ("\"\"".equals(cellValue)) {
			return "";
		}
		// TODO 他に特殊文字は？
		return cell.getRichStringCellValue().getString()
				.replaceAll("\\[TAB\\]", "\t");
	}

	/**
	 * Cellの形式が真偽値であったときの処理を行ないます。
	 * 
	 * @param cell
	 *            Cell
	 * @return Cellの値
	 */
	protected Boolean processBoolean(final Cell cell) {
		return Boolean.valueOf(cell.getBooleanCellValue());
	}
}
