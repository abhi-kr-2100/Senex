package io.abhikr2100;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class URLGenerator {
    private static URL generateURLFromQueryMap(HashMap<String, String> queryMap) {
        StringBuilder queryString = new StringBuilder();
        for (Map.Entry<String, String> query : queryMap.entrySet()) {
            String key = query.getKey();
            String val = query.getValue();

            queryString.append((queryString.length() == 0) ? '?' : '&');
            queryString.append(key).append("=").append(val);
        }

        try {
            return new URL("http://tatoeba.org/en/sentences/search" + queryString);
        } catch (MalformedURLException e) {
            // this should never have happened
            throw new RuntimeException("generateURLFromQueryMap: new URL() failed");
        }
    }

    public static ArrayList<URL> generate(HashMap<String, ArrayList<String>> argsMap) {
        HashMap<String, String> queryMap = new HashMap<>();
        for (Map.Entry<String, ArrayList<String>> arg : argsMap.entrySet()) {
            String key = arg.getKey();
            String val = arg.getValue().get(0);

            queryMap.put(key, val);
        }

        ArrayList<URL> urls = new ArrayList<>();
        urls.add(URLGenerator.generateURLFromQueryMap(queryMap));

        return urls;
    }
}
