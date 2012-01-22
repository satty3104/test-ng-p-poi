package test.tmp.jai;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Factory;

public class WebTestFactory {

    private static final String[] names = new String[]{"Account Size",
                                                       "Restore Activity",
                                                       "Account Backup Summary",
                                                       "Account Back Up Details"};
    private static final String suffix = " - Report Criteria";

    @Factory
    public Object[] createInstances() {
        final List<Object> result = new ArrayList<Object>();

        for (final String name : names) {
            result.add(new SubB(name, name.concat(suffix)));
        }
        return result.toArray(new Object[result.size()]);
    }
}
