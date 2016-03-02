# TesgNgpPoi #
## TestNgpPoiとは？ ##
TestNgpPoiは、TestNgの強力な機能である`DataProvider`を使用したテストのテストデータをExcelファイル上で管理できるフレームワークです。

## サンプルコードを使った比較 ##
### 通常のTestNgテスト ###
```
public class MyStringUtilsTest {

    private MyStringUtils target;

    @BeforeClass
    public void setUp() {
        target = new MyStringUtils();
    }

    @Test(dataProvider = "createData")
    public void testConcat(String expect, String str1, String delim, String str2) {
        assertEquals(target.concat(str1, delim, str2), expect);
    }

    @DataProvider(name = "createData")
    public Iterator<Object[]> createData() {
        // テストデータがソースコード内にべた書き！
        // 可読性も低い！
        List<Object[]> data = new ArrayList<Object[]>();
        data.add(new Object[] { "", null, null, null });
        data.add(new Object[] { "", "", null, null });
        data.add(new Object[] { "str1", "str1", null, null });
        data.add(new Object[] { "", null, "", null });
        data.add(new Object[] { "", "", "", null });
        data.add(new Object[] { "str1", "str1", "", null });
        data.add(new Object[] { "", null, ":", null });
        data.add(new Object[] { "", "", ":", null });
        data.add(new Object[] { "str1", "str1", ":", null });
        data.add(new Object[] { "", null, null, "" });
        data.add(new Object[] { "", "", null, "" });
        data.add(new Object[] { "str1", "str1", null, "" });
        data.add(new Object[] { "", null, "", "" });
        data.add(new Object[] { "", "", "", "" });
        data.add(new Object[] { "str1", "str1", "", "" });
        data.add(new Object[] { "", null, ":", "" });
        data.add(new Object[] { "", "", ":", "" });
        data.add(new Object[] { "str1", "str1", ":", "" });
        data.add(new Object[] { "str2", null, null, "str2" });
        data.add(new Object[] { "str2", "", null, "str2" });
        data.add(new Object[] { "str1str2", "str1", null, "str2" });
        data.add(new Object[] { "str2", null, "", "str2" });
        data.add(new Object[] { "str2", "", "", "str2" });
        data.add(new Object[] { "str1str2", "str1", "", "str2" });
        data.add(new Object[] { "str2", null, ":", "str2" });
        data.add(new Object[] { "str2", "", ":", "str2" });
        data.add(new Object[] { "str1:str2", "str1", ":", "str2" });
        return data.iterator();
    }
}
```

### TestNgpPoiを使用したテスト ###
```
public class MyStringUtilsTest {

    private MyStringUtils target;

    @BeforeClass
    public void setUp() {
        target = new MyStringUtils();
    }

    @Test(dataProvider = "createData")
    public void testConcat(Map<String, Object> map) {
        String str1 = (String) map.get("str1");
        String delim = (String) map.get("delim");
        String str2 = (String) map.get("str2");
        String expect = (String) map.get("expect");
        assertEquals(target.concat(str1, delim, str2), expect);
    }

    @DataProvider(name = "createData")
    public Iterator<Object[]> createData() {
        // Excelからテストデータを読み込むので、テストクラスはこんなにシンプル！
        return new SSFDataProviderFactoryCreator().getFactory().create();
    }
```

Excelでテストデータを管理するので、データの追加・削除がやりやすい！
| **No** | **expect** | **str1** | **delim** | **str2** |
|:-------|:-----------|:---------|:----------|:---------|
| 1      | ""         | null     | null      | null     |
| 2      | ""         | ""       | null      | null     |
| 3      | str1       | str1     | null      | null     |
| 4      | ""         | null     | ""        | null     |
| 5      | ""         | ""       | ""        | null     |
| 6      | str1       | str1     | ""        | null     |
| 7      | ""         | null     | :         | null     |
| 8      | ""         | ""       | :         | null     |
| 9      | str1       | str1     | :         | null     |
| 10     | ""         | null     | null      | ""       |
| 11     | ""         | ""       | null      | ""       |
| 12     | str1       | str1     | null      | ""       |
| 13     | ""         | null     | ""        | ""       |
| 14     | ""         | ""       | ""        | ""       |
| 15     | str1       | str1     | ""        | ""       |
| 16     | ""         | null     | :         | ""       |
| 17     | ""         | ""       | :         | ""       |
| 18     | str1       | str1     | :         | ""       |
| 19     | str2       | null     | null      | str2     |
| 20     | str2       | ""       | null      | str2     |
| 21     | str1str2   | str1     | null      | str2     |
| 22     | str2       | null     | ""        | str2     |
| 23     | str2       | ""       | ""        | str2     |
| 24     | str1str2   | str1     | ""        | str2     |
| 25     | str2       | null     | :         | str2     |
| 26     | str2       | ""       | :         | str2     |
| 27     | str1:str2  | str1     | :         | str2     |