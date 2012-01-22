package s.n.testngppoi.iterator;

public class HSSFDataProvider extends AbstractExcelDataProvider {

	public HSSFDataProvider() {
		StackTraceElement caller = getCaller(new Throwable());
		initCallerClass(caller);
		init(callerClass.getSimpleName() + ".xls", caller.getMethodName());
	}

	public HSSFDataProvider(String fileNm, String sheetName) {
		initCallerClass(getCaller(new Throwable()));
		init(fileNm, sheetName);
		this.fileName = fileNm;
	}
}
