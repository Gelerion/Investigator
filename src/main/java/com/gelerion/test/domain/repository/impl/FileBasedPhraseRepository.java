package com.gelerion.test.domain.repository.impl;

import com.gelerion.test.domain.repository.DataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;

public class FileBasedPhraseRepository implements DataRepository {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private Path inputPath;

    public FileBasedPhraseRepository(Path inputPath) {
        Objects.requireNonNull(inputPath);
        this.inputPath = inputPath;
    }

    @Override
    public Stream<String> load() {
        try {
            if (Files.isDirectory(inputPath, NOFOLLOW_LINKS)) {
                return StreamSupport.stream(Files.newDirectoryStream(inputPath).spliterator(), false)
                        .filter(FileBasedPhraseRepository::isNotDirectory)
                        .flatMap(this::lines);
            }

            return lines(inputPath);
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Stream<String> lines(Path path) {
        try {
            return Files.lines(path);
        }
        catch (IOException e) {
            LOG.warn("File is unreadable {}", path);
            return Stream.empty();
        }
    }

    private static boolean isNotDirectory(Path path) {
        if (Files.isDirectory(path)) {
            LOG.warn("Recursive option not allowed, omitting sub directory {}", path);
            return false;
        }
        return true;
    }
}
