package io.abhikr2100;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentTatoebaSentenceExtractor {
    private final OutputStream os;
    private final int numThreads;

    public ConcurrentTatoebaSentenceExtractor(OutputStream os) {
        this.os = os;
        this.numThreads = 10;
    }
    public ConcurrentTatoebaSentenceExtractor(OutputStream os, int numThreads) {
        this.os = os;
        this.numThreads = numThreads;
    }

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

        return s.substring(start, end);
    }

    private synchronized void writeToOutputStream(ArrayList<String> jsons) throws IOException {
        for (String json : jsons) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
            os.write('\n');
        }
    }

    private void extractJSONsFromURL(URL url) throws IOException {
        Document doc = Jsoup.connect(url.toString()).get();
        Elements sentenceDivs = doc.select("#content > div > section > md-content > div");

        ArrayList<String> sentenceTranslationPairJSONs = new ArrayList<>();
        for (Element sentenceDiv : sentenceDivs) {
            String ngInitVal = sentenceDiv.attr("ng-init");
            String JSON = ConcurrentTatoebaSentenceExtractor.extractJSONPart(ngInitVal);
            sentenceTranslationPairJSONs.add(JSON);
        }

        writeToOutputStream(sentenceTranslationPairJSONs);
    }

    public void extractAll(ArrayList<URL> urls) {
        ExecutorService executor = Executors.newFixedThreadPool(this.numThreads);

        urls.forEach(url -> executor.execute(() -> {
            try {
                extractJSONsFromURL(url);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));

        executor.shutdown();
        while (!executor.isTerminated()) {
        }
    }
}
