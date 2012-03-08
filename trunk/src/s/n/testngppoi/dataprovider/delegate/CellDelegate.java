package s.n.testngppoi.dataprovider.delegate;

import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;

public interface CellDelegate {

	public void processCell(Cell headerCell, Cell valueCell);

	public Map<String, Object> getMap();
}
