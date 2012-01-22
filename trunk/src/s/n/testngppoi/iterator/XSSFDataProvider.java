package s.n.testngppoi.iterator;

public class XSSFDataProvider extends AbstractExcelDataProvider {

	public XSSFDataProvider() {
		StackTraceElement caller = getCaller(new Throwable());
		initCallerClass(caller);
		init(callerClass.getSimpleName() + ".xlsx", caller.getMethodName());
	}

	public XSSFDataProvider(String fileNm, String sheetName) {
		initCallerClass(getCaller(new Throwable()));
		init(fileNm, sheetName);
	}
}
