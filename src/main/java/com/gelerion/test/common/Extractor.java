package com.gelerion.test.common;

import java.util.Optional;

public interface Extractor<T, R> {

    Optional<R> extract(T data);

}
