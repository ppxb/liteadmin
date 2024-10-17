package com.ppxb.la.base.common.code;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class ErrorCodeRangeContainer {

    static final int MIN_START_CODE = 10000;

    static final Map<Class<? extends ErrorCode>, ImmutablePair<Integer, Integer>> CODE_RANGE_MAP = new ConcurrentHashMap<>();

    static int errorCounter = 0;

    static void register(Class<? extends ErrorCode> clazz, int start, int end) {
        var simpleName = getSimpleName(clazz, start, end);

        CODE_RANGE_MAP.forEach((k, v) -> {
            if (isExistOtherRange(start, end, v)) {
                throw new IllegalArgumentException(String.format("<<ErrorCodeRangeValidator>> error: %s[%d,%d] has intersection with class:%s[%d,%d]",
                        simpleName, start, end, k.getSimpleName(), v.getLeft(), v.getRight()));
            }
        });

        var codeList = Stream.of(clazz.getEnumConstants()).map(codeEnum -> {
            var code = codeEnum.getCode();
            if (code < start || code > end) {
                throw new IllegalArgumentException(String.format("<<ErrorCodeRangeValidator>> error: %s[%d,%d] code %d out of range.", simpleName, start, end, code));
            }
            return code;
        }).toList();

        var distinctCodeList = codeList.stream().distinct().toList();
        var subtract = CollectionUtils.subtract(codeList, distinctCodeList);
        if (CollectionUtils.isNotEmpty(subtract)) {
            throw new IllegalArgumentException(String.format("<<ErrorCodeRangeValidator>> error: %s code %s is repeat.", simpleName, subtract));
        }

        CODE_RANGE_MAP.put(clazz, ImmutablePair.of(start, end));
        errorCounter = errorCounter + distinctCodeList.size();
    }

    private static String getSimpleName(Class<? extends ErrorCode> clazz, int start, int end) {
        var simpleName = clazz.getSimpleName();
        if (!clazz.isEnum()) {
            throw new ExceptionInInitializerError(String.format("<<ErrorCodeRangeValidator>> error: %s not enum class.", simpleName));
        }
        if (start > end) {
            throw new ExceptionInInitializerError(String.format("<<ErrorCodeRangeValidator>> error: %s start must less than the end.", simpleName));
        }
        if (start <= MIN_START_CODE) {
            throw new ExceptionInInitializerError(String.format("<<ErrorCodeRangeValidator>> error: %s start must more than %s.", simpleName, MIN_START_CODE));
        }

        var containsKey = CODE_RANGE_MAP.containsKey(clazz);
        if (containsKey) {
            throw new ExceptionInInitializerError(String.format("<<ErrorCodeRangeValidator>> error: enum %s already exist.", simpleName));
        }
        return simpleName;
    }

    private static boolean isExistOtherRange(int start, int end, ImmutablePair<Integer, Integer> range) {
        if (start >= range.getLeft() && start <= range.getRight()) {
            return true;
        }

        return end >= range.getLeft() && end <= range.getRight();
    }

    static int initialize() {
        return errorCounter;
    }
}
