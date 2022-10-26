import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import io.abhikr2100.ArgsParser;

public class ArgsParserTests {
    @Test
    public void shouldReturnEmptyMapWhenArgsIsEmpty() {
        HashMap<String, ArrayList<String>> argsMap = ArgsParser.parse(new String[]{});
        Assertions.assertTrue(argsMap.isEmpty());
    }

    @Test
    public void shouldThrowExceptionOnBadFormat() {
        String[] args = {"from", "to", "page"};
        Assertions.assertThrows(IllegalArgumentException.class, () -> ArgsParser.parse(args));
    }

    @Test
    public void shouldParseSingularValues() {
        String[] args = {"from: fra", "to: eng", "page: 42"};
        HashMap<String, ArrayList<String>> argsMap = ArgsParser.parse(args);

        Assertions.assertEquals(3, argsMap.size());

        Assertions.assertTrue(argsMap.containsKey("from"));
        ArrayList<String> from = argsMap.get("from");
        Assertions.assertEquals(1, from.size());
        Assertions.assertEquals("fra", from.get(0));

        Assertions.assertTrue(argsMap.containsKey("to"));
        ArrayList<String> to = argsMap.get("to");
        Assertions.assertEquals(1, to.size());
        Assertions.assertEquals("eng", to.get(0));

        Assertions.assertTrue(argsMap.containsKey("page"));
        ArrayList<String> page = argsMap.get("page");
        Assertions.assertEquals(1, page.size());
        Assertions.assertEquals("42", page.get(0));
    }

    @Test
    public void shouldParseMultipleValues() {
        String[] args = {"args:[argA argB]"};
        HashMap<String, ArrayList<String>> argsMap = ArgsParser.parse(args);

        Assertions.assertEquals(1, argsMap.size());
        Assertions.assertTrue(argsMap.containsKey("args"));

        ArrayList<String> vals = argsMap.get("args");
        Assertions.assertEquals(2, vals.size());
        Assertions.assertEquals("argA", vals.get(0));
        Assertions.assertEquals("argB", vals.get(1));
    }

    @Test
    public void shouldParseNumericRange() {
        String[] args = {"pages:[1..10]"};
        HashMap<String, ArrayList<String>> argsMap = ArgsParser.parse(args);

        Assertions.assertEquals(1, argsMap.size());
        Assertions.assertTrue(argsMap.containsKey("pages"));

        ArrayList<String> pages = argsMap.get("pages");
        Assertions.assertEquals(10, pages.size());

        for (int i = 1; i <= 10; ++i) {
            Assertions.assertEquals(String.valueOf(i), pages.get(i - 1));
        }
    }
}
