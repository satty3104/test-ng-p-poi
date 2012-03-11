package s.n.testngppoi.service;

import org.apache.poi.ss.usermodel.Cell;

import s.n.testngppoi.exception.InvalidCellTypeException;

public interface CellService {

	public Object getCellValue(Cell cell) throws InvalidCellTypeException;
}
