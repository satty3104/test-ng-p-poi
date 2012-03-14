package s.n.testngppoi.service;

import org.apache.poi.ss.usermodel.Cell;

import s.n.testngppoi.exception.InvalidCellTypeException;

/**
 * SSF形式ファイルのCellに関する処理を行うクラスのインタフェース。
 * 
 * @author s_nagai
 * @since 2012/03/08
 */
public interface CellService {

	/**
	 * Cellに格納されている値を取得します。
	 * 
	 * @param cell
	 *            Cell
	 * @return 値
	 * @throws InvalidCellTypeException
	 *             セルの形式が想定外のものであった場合
	 */
	public Object getCellValue(Cell cell) throws InvalidCellTypeException;
}
