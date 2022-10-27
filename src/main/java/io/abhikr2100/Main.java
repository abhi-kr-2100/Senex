package io.abhikr2100;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        HashMap<String, ArrayList<String>> argsMap = ArgsParser.parse(args);
        ArrayList<URL> urls = URLGenerator.generate(argsMap);
        ConcurrentTatoebaSentenceExtractor senex = new ConcurrentTatoebaSentenceExtractor(System.out);
        senex.extractAll(urls);
    }
}