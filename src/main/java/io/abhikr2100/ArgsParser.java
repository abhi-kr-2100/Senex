package io.abhikr2100;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ArgsParser {
    private static ArrayList<String> generateRange(String range) {
        String[] endPoints = range.split("\\.\\.");

        int first = Integer.parseInt(endPoints[0]);
        int last = Integer.parseInt(endPoints[1]);

        return IntStream.rangeClosed(first, last)
                .mapToObj(String::valueOf)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static ArrayList<String> extractValues(String valuesStr) {
        if (valuesStr.charAt(0) != '[') {
            return new ArrayList<>(Collections.singletonList(valuesStr));
        }

        if (valuesStr.charAt(valuesStr.length() - 1) != ']') {
            throw new IllegalArgumentException("parse: missing ']' in passed values");
        }

        String valuesStrWithoutDelimiters = valuesStr.substring(1, valuesStr.length() - 1);

        if (valuesStr.contains("..")) {
            return ArgsParser.generateRange(valuesStrWithoutDelimiters);
        }

        String[] values = valuesStrWithoutDelimiters.split("\\s");
        return new ArrayList<>(Arrays.asList(values));
    }

    private static AbstractMap.SimpleImmutableEntry<String, ArrayList<String>> parseArg(String arg) {
        String[] parts = arg.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("parse: each argument should be of the form `key:val` or `key:[values]`");
        }

        String key = parts[0].trim();
        String values = parts[1].trim();

        ArrayList<String> valuesList = ArgsParser.extractValues(values);
        return new AbstractMap.SimpleImmutableEntry<>(key, valuesList);
    }

    public static HashMap<String, ArrayList<String>> parse(String[] args) {
        HashMap<String, ArrayList<String>> argsMap = new HashMap<>();
        for (String arg : args) {
            AbstractMap.SimpleImmutableEntry<String, ArrayList<String>> kv = ArgsParser.parseArg(arg);
            argsMap.put(kv.getKey(), kv.getValue());
        }

        return argsMap;
    }
}
