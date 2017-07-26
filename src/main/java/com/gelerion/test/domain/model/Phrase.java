package com.gelerion.test.domain.model;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Phrase implements Row {
    private final Metadata<LocalDateTime> metadata;
    private final String[] words;
    private final Set<String> describingTerms;
    private final String rawPhrase;

    private boolean isSynced;


    Phrase(String rawPhrase, Metadata<LocalDateTime> metadata, String[] words, Set<String> describingTerms) {
        this.rawPhrase = rawPhrase;
        this.metadata = metadata;
        this.words = words;
        this.describingTerms = describingTerms;
    }

    public Set<String> getDescribingTerms() {
        return describingTerms;
    }

    public boolean isSameTermsCount(Phrase that) {
        return this.getDescribingTerms().size() == that.getDescribingTerms().size();
    }

    public Set<String> symmetricDifference(Phrase that) {
        if(this == that) return Collections.emptySet(); //if same object
        HashSet<String> result = new HashSet<>(this.describingTerms);

        for (String thatDescribingTerm : that.describingTerms) {
            if(!result.add(thatDescribingTerm)) {
                result.remove(thatDescribingTerm);
            }
        }

        return result;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public Phrase markAsSynced() {
        this.isSynced = true;
        return this;
    }

    @Override
    public String toString() {
        return "Phrase{" +
                rawPhrase + '}';
    }

    public String toRow() {
        return rawPhrase;
    }

    public static final class PhraseBuilder {
        private Metadata<LocalDateTime> metadata;
        private String[] words;
        private Set<String> describingTerms;
        private String phrase;

        private PhraseBuilder() {
        }

        public static PhraseBuilder aPhrase() {
            return new PhraseBuilder();
        }

        public PhraseBuilder withMetadata(Metadata<LocalDateTime> metadata) {
            this.metadata = metadata;
            return this;
        }

        public PhraseBuilder withWords(String[] words) {
            this.words = words;
            return this;
        }

        public PhraseBuilder withRawPhrase(String phrase) {
            this.phrase = phrase;
            return this;
        }

        public PhraseBuilder withDescribingTerms(Set<String> terms) {
            this.describingTerms = terms;
            return this;
        }

        public Phrase build() {
            return new Phrase(phrase, metadata, words, describingTerms);
        }
    }
}
