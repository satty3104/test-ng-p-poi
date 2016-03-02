# TestNgpPoiの簡単な使い方 #
ここでは、サンプルコードを使用しながらTestNgpPoiの使い方を紹介します。
## テスト対象クラス ##
```
/**
 * 文字列を操作するクラス。
 */
public class MyStringUtils {

    /**
     * 引数に指定した2つの文字列を、区切り文字を間にはさんで連結します。
     * 
     * @param str1 左側の文字列
     * @param delim 区切り文字
     * @param str2 右側の文字列
     */
    public String concat(String str1, String delim, String str2) {
        if (str1 == null || str1.length() == 0) {
            if (str2 == null || str2.length() == 0) {
                return "";
            }
            return str2;
        }
        if (str2 == null || str2.length() == 0) {
            return str1;
        }
        if (delim == null || delim.length() == 0) {
            return str1 + str2;
        }
        return str1 + delim + str2;
    }
}
```

## テストクラスのひな型を作成する ##
```
/**
 * {@link MyStringUtils}のテストクラス。
 */
public class MyStringUtilsTest {

    /** テスト対象クラス */
    private MyStringUtils target;

    /**
     * 初期設定を行ないます。
     */
    @BeforeClass
    public void setUp() {
        target = new MyStringUtils();
    }

    /**
     * {@link MyStringUtils#concat(String, String, String)}のテストメソッド。
     * 
     * @param map
     *            テストデータが格納されたMap
     */
    @Test(dataProvider = "testConcatData")
    public void testConcat(Map<String, Object> map) {
        // TODO 後で実装する
    }

    /**
     * {@link #testConcat(Map)}のテストデータを作成するメソッド。
     * 
     * @return 1回分のテストデータ
     */
    @DataProvider(name = "testConcatData")
    public Iterator<Object[]> testConcatData() {
        return new SSFDataProviderFactoryCreator().getFactory().create();
    }
}
```

`@DataProvider`アノテーションを付与したメソッド内で、`SSFDataProviderFactoryCreator`のインスタンスを生成し、`getFactory()`メソッドを使用して`SSFDataProviderFactory`のインスタンスを取得します。

取得した`SSFDataProviderFactory`のインスタンスの`create()`メソッドを使用してテストデータが格納された`Iterator`を取得します。

## テストデータを作成する ##
Excelファイルに以下のようなデータを準備します。このとき、シート名は`@DataProvider`アノテーションを付与したメソッドの名前と同じ「`testConcatData`
」とします。

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

これを、`MyStringUtilsTest.java`をコンパイルした`MyStringUtilsTest.class`ファイルと同じ場所に、`MyStringUtilsTest.xlsx`という名前で保存します。

このExcelの1行が1回のテストパターンとなります。
Excelを使用することにより、組み合わせテストの全テストパターンをより管理しやすくなります。

Excelの書き方の詳細は[Excelの書き方 こちら]

## テストメソッドの実装 ##
```
/**
 * {@link MyStringUtils}のテストクラス。
 */
public class MyStringUtilsTest {

    /** テスト対象クラス */
    private MyStringUtils target;

    /**
     * 初期設定を行ないます。
     */
    @BeforeClass
    public void setUp() {
        target = new MyStringUtils();
    }

    /**
     * {@link MyStringUtils#concat(String, String, String)}のテストメソッド。
     * 
     * @param map
     *            テストデータが格納されたMap
     */
    @Test(dataProvider = "testConcatData")
    public void testConcat(Map<String, Object> map) {
        String str1 = (String) map.get("str1");
        String delim = (String) map.get("delim");
        String str2 = (String) map.get("str2");
        String expect = (String) map.get("expect");
        assertEquals(target.concat(str1, delim, str2), expect);
    }
    }

    /**
     * {@link #testConcat(Map)}のテストデータを作成するメソッド。
     * 
     * @return 1回分のテストデータ
     */
    @DataProvider(name = "testConcatData")
    public Iterator<Object[]> testConcatData() {
        return new SSFDataProviderFactoryCreator().getFactory().create();
    }
}
```
`@DataProvider`アノテーションを付与したメソッドの戻り値である`Iterator`の1つの要素は長さ1の`Object`型1次元配列となります。
この配列の0番目の要素が、テストメソッドの第1引数に渡される`Map<String, Object>`となります。

`Map<String, Object>`のKeyはExcelのヘッダ行の文字列、ValueはExcelの現在のテストパターンの値となります。

例）27回目のテストの場合
| **No** | **expect** | **str1** | **delim** | **str2** | ← Key |
|:-------|:-----------|:---------|:----------|:---------|:------|
| 27     | str1:str2  | str1     | :         | str2     | ← Value |

`map.toString()` → `{No=27.0, expect=str1:str2, str1=str1, delim=:, str2=str2}`

## 実行してみる ##
TestNg プラグインを導入済みのEclipseで実行

```
[TestNG] Running:
  testng-customsuite.xml

PASSED: testConcat({str2=null, expect=, str1=null, No=1.0, delim=null})
PASSED: testConcat({str2=null, expect=, str1=, No=2.0, delim=null})
PASSED: testConcat({str2=null, expect=str1, str1=str1, No=3.0, delim=null})
PASSED: testConcat({str2=null, expect=, str1=null, No=4.0, delim=})
PASSED: testConcat({str2=null, expect=, str1=, No=5.0, delim=})
PASSED: testConcat({str2=null, expect=str1, str1=str1, No=6.0, delim=})
PASSED: testConcat({str2=null, expect=, str1=null, No=7.0, delim=:})
PASSED: testConcat({str2=null, expect=, str1=, No=8.0, delim=:})
PASSED: testConcat({str2=null, expect=str1, str1=str1, No=9.0, delim=:})
PASSED: testConcat({str2=, expect=, str1=null, No=10.0, delim=null})
PASSED: testConcat({str2=, expect=, str1=, No=11.0, delim=null})
PASSED: testConcat({str2=, expect=str1, str1=str1, No=12.0, delim=null})
PASSED: testConcat({str2=, expect=, str1=null, No=13.0, delim=})
PASSED: testConcat({str2=, expect=, str1=, No=14.0, delim=})
PASSED: testConcat({str2=, expect=str1, str1=str1, No=15.0, delim=})
PASSED: testConcat({str2=, expect=, str1=null, No=16.0, delim=:})
PASSED: testConcat({str2=, expect=, str1=, No=17.0, delim=:})
PASSED: testConcat({str2=, expect=str1, str1=str1, No=18.0, delim=:})
PASSED: testConcat({str2=str2, expect=str2, str1=null, No=19.0, delim=null})
PASSED: testConcat({str2=str2, expect=str2, str1=, No=20.0, delim=null})
PASSED: testConcat({str2=str2, expect=str1str2, str1=str1, No=21.0, delim=null})
PASSED: testConcat({str2=str2, expect=str2, str1=null, No=22.0, delim=})
PASSED: testConcat({str2=str2, expect=str2, str1=, No=23.0, delim=})
PASSED: testConcat({str2=str2, expect=str1str2, str1=str1, No=24.0, delim=})
PASSED: testConcat({str2=str2, expect=str2, str1=null, No=25.0, delim=:})
PASSED: testConcat({str2=str2, expect=str2, str1=, No=26.0, delim=:})
PASSED: testConcat({str2=str2, expect=str1:str2, str1=str1, No=27.0, delim=:})

===============================================
    Default test
    Tests run: 27, Failures: 0, Skips: 0
===============================================


===============================================
Default suite
Total tests run: 27, Failures: 0, Skips: 0
===============================================

[TestNG] Time taken by org.testng.reporters.SuiteHTMLReporter@2b5ac3c9: 153 ms
[TestNG] Time taken by [TestListenerAdapter] Passed:0 Failed:0 Skipped:0]: 4 ms
[TestNG] Time taken by org.testng.reporters.XMLReporter@1572e449: 55 ms
[TestNG] Time taken by org.testng.reporters.JUnitReportReporter@f01a1e: 8 ms
[TestNG] Time taken by org.testng.reporters.EmailableReporter@7290cb03: 13 ms
```

簡単に、全パターンテストが実施できました。