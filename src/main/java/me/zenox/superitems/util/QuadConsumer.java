package me.zenox.superitems.util;

import java.util.Objects;

@FunctionalInterface
public interface QuadConsumer<T, U, V, W> {
    public void accept(T t, U u, V v, W w);

    public default QuadConsumer<T, U, V, W> andThen(QuadConsumer<? super T, ? super U, ? super V, ? super W> after) {
        Objects.requireNonNull(after);
        return (a, b, c, d) -> {
            accept(a, b, c, d);
            after.accept(a, b, c, d);
        };
    }
}

