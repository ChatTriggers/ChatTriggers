package com.chattriggers.ctjs.utils;

import java.util.function.Function;

public class NullableModifier {
    public static <T, R> R modify(T value, Function<T, R> function) {
        if (value == null) {
            return null;
        }
        return function.apply(value);
    }
}
