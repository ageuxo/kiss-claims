package org.ageuxo.kiss.claims.core;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public sealed interface Result<T> {

    Optional<T> getResult();
    Optional<Throwable> getCause();

    static <T> Success<T> success(T result) {
        return new Success<>(result);
    }

    static <T> Informative<T> informative(String... messages) {
        return informative(null, messages);
    }

    static <T> Informative<T> informative(@Nullable Throwable cause, String... messages) {
        return informative(cause, List.of(messages));
    }

    static <T> Informative<T> informative(@Nullable Throwable cause, List<String> messages) {
        return new Informative<>(Optional.ofNullable(cause), messages);
    }

    static <T> Error<T> error( Throwable throwable) {
        return new Error<>(throwable);
    }

    record Error<T>(Throwable cause) implements Result<T> {

        @Override
        public Optional<T> getResult() {
            return Optional.empty();
        }

        @Override
        public Optional<Throwable> getCause() {
            return Optional.of(cause());
        }
    }

    record Informative<T>(Optional<Throwable> cause, List<String> messages) implements Result<T> {

        @Override
        public Optional<T> getResult() {
            return Optional.empty();
        }

        @Override
        public Optional<Throwable> getCause() {
            return cause();
        }
    }

    record Success<T>(T result) implements Result<T> {

        @Override
        public Optional<T> getResult() {
            return Optional.of(result());
        }

        @Override
        public Optional<Throwable> getCause() {
            return Optional.empty();
        }
    }
}
