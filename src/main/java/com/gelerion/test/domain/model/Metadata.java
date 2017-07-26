package com.gelerion.test.domain.model;

public class Metadata<T> {
    final T metadata;

    private Metadata(T meta) {
        this.metadata = meta;
    }

    public static <V> Metadata<V> of(V meta) {
        return new Metadata<>(meta);
    }

    public T getMetadata() {
        return metadata;
    }

    @Override
    public String toString() {
        return "{ metadata: " + metadata + '}';
    }
}
