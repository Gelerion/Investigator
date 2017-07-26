package com.gelerion.test.common;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StopWordsFilter {

    //better approach is to load it from custom file
    private Set<String> stopWords = Stream.of(
            "is", "at", "a", "into", "the"
    ).collect(Collectors.toSet());


    Set<String> filter(String[] words) {
        Set<String> terms = new HashSet<>();

        for (String word : words) {
            if (!stopWords.contains(word.trim())) {
                terms.add(word);
            }
        }

        return terms;
    }

}
