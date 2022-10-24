package io.abhikr2100;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class ArgsParser {
    private static Pair<String, ArrayList<String>> parseArg(String arg) {
        String[] parts = arg.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException(
                    "parse: each argument should be of the form `key:val` or `key:[values]`"
            );
        }

        String key = parts[0].trim();
        ArrayList<String> value = new ArrayList<>();
        value.add(parts[1].trim());

        return new Pair<>(key, value);
    }

    public static HashMap<String, ArrayList<String>> parse(String[] args) {
        HashMap<String, ArrayList<String>> argsMap = new HashMap<>();
        for (String arg : args) {
            Pair<String, ArrayList<String>> kv = ArgsParser.parseArg(arg);
            argsMap.put(kv.getKey(), kv.getValue());
        }

        return argsMap;
    }
}
