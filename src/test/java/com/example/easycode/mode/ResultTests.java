package com.example.easycode.mode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class ResultTests {

    @Test
    void shouldBuildSuccessResult() {
        Result<String> result = Result.success("ok");

        Assertions.assertTrue(result.isSuccess());
        Assertions.assertEquals(DefaultResultCode.SUCCESS.getCode(), result.getCode());
        Assertions.assertEquals("ok", result.getData());
    }

    @Test
    void shouldBuildPageResultWithRecords() {
        PageResult<String> result = PageResult.success(1, 10, 2L, Arrays.asList("a", "b"));

        Assertions.assertTrue(result.isSuccess());
        Assertions.assertEquals(Integer.valueOf(1), result.getPageNo());
        Assertions.assertEquals(Long.valueOf(2L), result.getTotal());
        Assertions.assertEquals(Arrays.asList("a", "b"), result.getRecords());
    }
}
