package com.example.easycode.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author wangyangyang
 * @Description: TraceId 工具类
 * @date 2026-03-28 14:12
 */
public class TraceIdUtil {

    private static final ThreadLocal<String> TRACE_ID_HOLDER = new ThreadLocal<>();

    private static final DateTimeFormatter TRACE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    private static final AtomicLong SEQUENCE = new AtomicLong();

    private TraceIdUtil() {
    }

    public static String createTraceId() {
        return LocalDateTime.now().format(TRACE_TIME_FORMATTER) + String.format("%06d", SEQUENCE.getAndIncrement() % 1000000);
    }

    public static void setTraceId(String traceId) {
        AssertUtil.notBlank(traceId, "traceId can not be blank");
        TRACE_ID_HOLDER.set(traceId);
    }

    public static String getTraceId() {
        return TRACE_ID_HOLDER.get();
    }

    public static String getOrCreateTraceId() {
        String traceId = getTraceId();
        if (traceId == null) {
            traceId = createTraceId();
            setTraceId(traceId);
        }
        return traceId;
    }

    public static void clear() {
        TRACE_ID_HOLDER.remove();
    }
}
