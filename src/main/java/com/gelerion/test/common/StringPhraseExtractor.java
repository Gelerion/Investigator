package com.gelerion.test.common;

import com.gelerion.test.domain.model.Metadata;
import com.gelerion.test.domain.model.Phrase;
import com.gelerion.test.domain.model.Phrase.PhraseBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringPhraseExtractor implements Extractor<String, Phrase> {
    private final String wordsSeparator;
    private final Pattern metadataAndSentencePattern;
    private final DateTimeFormatter timeFormatter;
    private final StopWordsFilter stopWordsFilter;

    public StringPhraseExtractor(String wordsSeparator, StopWordsFilter stopWordsFilter) {
        this.wordsSeparator = wordsSeparator;
        this.stopWordsFilter = stopWordsFilter;
        this.metadataAndSentencePattern = Pattern.compile("(^(\\d+|-|\\s|:)+)(.*)");
        this.timeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    }


    @Override
    public Optional<Phrase> extract(String data) {
        Metadata<LocalDateTime> metadata = null;
        String phrase = null;

        Matcher metadataMatcher = metadataAndSentencePattern.matcher(data);
        if (metadataMatcher.find()) {
            metadata = Metadata.of(LocalDateTime
                    .from(timeFormatter.parse(metadataMatcher.group(1).trim())));
            phrase = metadataMatcher.group(3);
        }

        if(phrase != null) {
            String[] words = phrase.split(wordsSeparator);

            return Optional.of(PhraseBuilder.aPhrase()
                    .withRawPhrase(phrase)
                    .withMetadata(metadata)
                    .withWords(words)
                    .withDescribingTerms(stopWordsFilter.filter(words))
                    .build());
        }

        return Optional.empty();
    }
}
