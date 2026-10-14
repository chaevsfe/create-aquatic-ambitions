package net.davio.aquaticambitions.registrate.fn;

import java.util.function.Function;

@FunctionalInterface
public interface NonNullFunction<T, R> extends Function<T, R> {
}
