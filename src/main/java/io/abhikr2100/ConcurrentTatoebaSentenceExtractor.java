package io.abhikr2100;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class ConcurrentTatoebaSentenceExtractor {
    private static String extractJSONPart(String s) {
        int start = 0;
        while (s.charAt(start) != '{') {
            ++start;
        }

        int end = start;
        int nesting = 0;
        do {
            if (s.charAt(end) == '{') {
                ++nesting;
            } else if (s.charAt(end) == '}') {
                --nesting;
            }

            ++end;
        } while (nesting != 0);

        return s.substring(start, end + 1);
    }

    private static ArrayList<String> extractJSONsFromURL(URL url) throws IOException {
        Document doc = Jsoup.connect(url.toString()).get();
        Elements sentenceDivs = doc.select("#content > div > section > md-content > div");

        ArrayList<String> sentenceTranslationPairJSONs = new ArrayList<>();
        for (Element sentenceDiv : sentenceDivs) {
            String ngInitVal = sentenceDiv.attr("ng-init");
            String JSON = ConcurrentTatoebaSentenceExtractor.extractJSONPart(ngInitVal);
            sentenceTranslationPairJSONs.add(JSON);
        }

        return sentenceTranslationPairJSONs;
    }

    public static ArrayList<String> extractAll(ArrayList<URL> urls) throws IOException {
        ArrayList<String> sentenceTranslationPairJSONs = new ArrayList<>();
        for (URL url : urls) {
            ArrayList<String> JSONs = ConcurrentTatoebaSentenceExtractor.extractJSONsFromURL(url);
            sentenceTranslationPairJSONs.addAll(JSONs);
        }

        return sentenceTranslationPairJSONs;
    }
}
