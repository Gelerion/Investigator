package com.gelerion.test;

import com.gelerion.test.common.StopWordsFilter;
import com.gelerion.test.common.StringPhraseExtractor;
import com.gelerion.test.domain.model.Phrase;
import com.gelerion.test.domain.repository.impl.FileBasedPhraseRepository;
import com.gelerion.test.service.FileAppender;

import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        String inputFilePath  = args[0];
        String outputFilePath = args[1];

        FileBasedPhraseRepository repository = new FileBasedPhraseRepository(Paths.get(inputFilePath));
        StringPhraseExtractor extractor      = new StringPhraseExtractor("\\s", new StopWordsFilter());
        FileAppender<Phrase> fileAppender    = new FileAppender<>(Paths.get(outputFilePath));

        Investigator investigator = new Investigator(repository, extractor, fileAppender);
        investigator.investigate();
    }
}
