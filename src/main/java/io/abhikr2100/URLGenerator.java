package io.abhikr2100;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
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

    private static ArrayList<HashMap<String, String>> generateQueryMapsAux(
            HashMap<String, ArrayList<String>> argsMap, HashMap<String, String> prefix
    ) {
        HashMap<String, String> prefixCopy = new HashMap<>(prefix);

        for (Map.Entry<String, ArrayList<String>> arg : argsMap.entrySet()) {
            String key = arg.getKey();
            ArrayList<String> values = arg.getValue();

            if (prefixCopy.containsKey(key)) {
                continue;
            }

            if (values.size() == 1) {
                prefixCopy.put(key, values.get(0));
                continue;
            }

            ArrayList<HashMap<String, String>> queryMaps = new ArrayList<>();
            for (String value : values) {
                HashMap<String, String> newPrefix = new HashMap<>(prefixCopy);
                newPrefix.put(key, value);

                queryMaps.addAll(generateQueryMapsAux(argsMap, newPrefix));
            }

            return queryMaps;
        }

        return new ArrayList<>(Collections.singletonList(prefixCopy));
    }

    private static ArrayList<HashMap<String, String>> generateQueryMaps(HashMap<String, ArrayList<String>> argsMap) {
        HashMap<String, String> initialPrefix = new HashMap<>();
        return generateQueryMapsAux(argsMap, initialPrefix);
    }

    public static ArrayList<URL> generate(HashMap<String, ArrayList<String>> argsMap) {
        ArrayList<HashMap<String, String>> queryMaps = generateQueryMaps(argsMap);

        ArrayList<URL> urls = new ArrayList<>(queryMaps.size());
        for (HashMap<String, String> queryMap : queryMaps) {
            urls.add(generateURLFromQueryMap(queryMap));
        }

        return urls;
    }
}
