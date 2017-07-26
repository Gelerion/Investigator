package com.gelerion.test.service;

import com.gelerion.test.domain.model.Row;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileAppender<T extends Row> {
    private final Path outputPath;
    private final Writer writer;

    private boolean isClosed;


    public FileAppender(Path outputPath) {
        this.outputPath = outputPath;
        try {
            if (Files.exists(outputPath)) {
                Files.delete(outputPath);
            }

            this.writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE_NEW);

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public FileAppender<T> append(T data) {
        if(isClosed) throw new RuntimeException("Already closed exception");
        asUnchecked(() -> writer.append(data.toRow() + "\n"));
        return this;
    }

    public FileAppender<T> append(String data) {
        if(isClosed) throw new RuntimeException("Already closed exception");
        asUnchecked(() -> writer.append(data + "\n"), writer::close);
        return this;
    }

    public void close() {
        if(!isClosed) {
            asUnchecked(writer::close);
            isClosed = true;
        }
    }

    private void asUnchecked(FunctionEx fun) {
        try {
            fun.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void asUnchecked(FunctionEx fun, FunctionEx cleanUp) {
        try {
            fun.run();
        } catch (Exception e) {

            try {
                if(cleanUp != null) cleanUp.run();
            } catch (Exception ignore) { /*NOP*/ }

            throw new RuntimeException(e);
        }
    }


    private interface FunctionEx {
        void run() throws Exception;
    }


}
