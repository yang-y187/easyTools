package com.example.easycode.convert;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DefaultConverterRegistryTests {

    @Test
    void shouldConvertByRegisteredFunction() {
        DefaultConverterRegistry registry = new DefaultConverterRegistry();
        registry.register(String.class, Integer.class, Integer::valueOf);

        Assertions.assertEquals(Integer.valueOf(100), registry.convert("100", Integer.class));
    }

    @Test
    void shouldUseAssignableSourceConverter() {
        DefaultConverterRegistry registry = new DefaultConverterRegistry();
        registry.register(Number.class, String.class, String::valueOf);

        Assertions.assertEquals("12", registry.convert(12, String.class));
    }
}

