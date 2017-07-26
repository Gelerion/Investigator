package com.gelerion.test.common;

import java.util.Objects;

public class Pair<F,S> {
    final F first;
    final S second;

    Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair<?, ?> p = (Pair<?, ?>) o;
        return Objects.equals(p.first, first) && Objects.equals(p.second, second);
    }

    @Override
    public int hashCode() {
        return (first == null ? 0 : first.hashCode()) ^ (second == null ? 0 : second.hashCode());
    }

    public static <A, B> Pair <A, B> create(A a, B b) {
        return new Pair<>(a, b);
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }
}
