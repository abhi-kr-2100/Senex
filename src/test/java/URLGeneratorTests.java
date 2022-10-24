import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import io.abhikr2100.ArgsParser;
import io.abhikr2100.URLGenerator;

public class URLGeneratorTests {
    @Test
    public void shouldGenerateOneURLForSingularArgsMap() {
        String[] args = {"from: fra", "to: eng", "page: 42"};
        HashMap<String, ArrayList<String>> argsMap = ArgsParser.parse(args);
        ArrayList<URL> urls = URLGenerator.generate(argsMap);

        Assertions.assertEquals(1, urls.size());
        Assertions.assertTrue(urls.get(0).getQuery().contains("fra"));
        Assertions.assertTrue(urls.get(0).getQuery().contains("eng"));
        Assertions.assertTrue(urls.get(0).getQuery().contains("42"));
    }

    @Test
    public void shouldGenerateAllURLsForMultiValuedArgsMap() {
        String[] args = {"from: [fra, deu]", "to: eng", "page: [1, 2]"};
        HashMap<String, ArrayList<String>> argsMap = ArgsParser.parse(args);
        ArrayList<URL> urls = URLGenerator.generate(argsMap);

        Assertions.assertEquals(4, urls.size());
    }
}
