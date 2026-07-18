package com.example.easycode.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TraceIdUtilTests {

    @Test
    void shouldCreateAndManageTraceId() {
        TraceIdUtil.clear();
        String traceId = TraceIdUtil.getOrCreateTraceId();

        Assertions.assertNotNull(traceId);
        Assertions.assertTrue(traceId.matches("\\d{23}"));
        Assertions.assertEquals(traceId, TraceIdUtil.getTraceId());

        TraceIdUtil.clear();
        Assertions.assertNull(TraceIdUtil.getTraceId());
    }

    @Test
    void shouldCreateDifferentTraceIds() {
        Assertions.assertNotEquals(TraceIdUtil.createTraceId(), TraceIdUtil.createTraceId());
    }
}
