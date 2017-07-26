package com.gelerion.test.domain.repository;

import java.util.stream.Stream;

public interface DataRepository {

    Stream<String> load();
}
