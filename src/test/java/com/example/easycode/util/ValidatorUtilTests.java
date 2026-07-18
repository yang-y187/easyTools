package com.example.easycode.util;

import com.example.easycode.mode.BizException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class ValidatorUtilTests {

    @Test
    void shouldPassWhenDataIsValid() {
        ValidatorUtil.notBlank("easytool", "invalid");
        ValidatorUtil.notEmpty(Arrays.asList("a"), "invalid");
        ValidatorUtil.positive(1L, "invalid");
    }

    @Test
    void shouldThrowBizExceptionWhenInvalid() {
        Assertions.assertThrows(BizException.class, () -> ValidatorUtil.notBlank("   ", "blank"));
        Assertions.assertThrows(BizException.class, () -> ValidatorUtil.between(11L, 1L, 10L, "range error"));
    }
}
