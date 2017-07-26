package com.gelerion.test;

import com.gelerion.test.common.Extractor;
import com.gelerion.test.domain.model.Phrase;
import com.gelerion.test.domain.repository.DataRepository;
import com.gelerion.test.service.FileAppender;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Investigator {

    private final DataRepository dataRepository;
    private final Extractor<String, Phrase> extractor;
    private final FileAppender<Phrase> fileAppender;

    public Investigator(DataRepository dataRepository,
                        Extractor<String, Phrase> extractor,
                        FileAppender<Phrase> fileAppender) {
        this.dataRepository = dataRepository;
        this.extractor = extractor;
        this.fileAppender = fileAppender;
    }


    public void investigate() {
        List<Phrase> phrases = dataRepository.load()
                .map(extractor::extract)
                .filter(Optional::isPresent) //or custom collector to get rid of this
                .map(Optional::get)
                .collect(Collectors.toList());

        try {
            for (Phrase thisPhrase : phrases) {
                for (Phrase thatPhrase : phrases) {
                    if(thatPhrase.isSynced()) { //was already processed just skip
                        continue;
                    }

                    if (!thisPhrase.isSameTermsCount(thatPhrase)) {
                        continue;
                    }

                    Set<String> differentTerms = thisPhrase.symmetricDifference(thatPhrase);

                    if(differentTerms.size() == 2) {
                        thisPhrase.markAsSynced();
                        thatPhrase.markAsSynced();

                        fileAppender.append(thisPhrase)
                                .append(thatPhrase)
                                .append("The changing word was: " + differentTerms.toString());

                    }
                }
            }
        } finally {
            fileAppender.close();
        }
    }

}
