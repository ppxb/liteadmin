package com.ppxb.la.base.common.util;

import cn.hutool.core.util.StrUtil;

import java.util.*;

public class StringUtil extends StrUtil {

    public static Set<String> splitConvertToSet(String str, String separator) {
        if (isEmpty(str)) {
            return new HashSet<>();
        }

        var splitArr = str.split(separator);
        var set = new HashSet<String>(splitArr.length);
        Collections.addAll(set, splitArr);
        return set;
    }

    public static List<String> splitConvertToList(String str, String separator) {
        if (isEmpty(str)) {
            return new ArrayList<>();
        }

        var splitArr = str.split(separator);
        var list = new ArrayList<String>(splitArr.length);
        list.addAll(Arrays.asList(splitArr));
        return list;
    }

    public static List<Integer> splitConvertToIntList(String str, String separator, int defaultValue) {
        if (isEmpty(str)) {
            return new ArrayList<>();
        }

        var splitArr = str.split(separator);
        var list = new ArrayList<Integer>(splitArr.length);
        for (var s : splitArr) {
            try {
                var parseInt = Integer.parseInt(s);
                list.add(parseInt);
            } catch (NumberFormatException e) {
                list.add(defaultValue);
            }
        }
        return list;
    }

    public static Set<Integer> splitConvertToIntSet(String str, String separator, int defaultValue) {
        if (isEmpty(str)) {
            return new HashSet<>();
        }

        var splitArr = str.split(separator);
        var set = new HashSet<Integer>(splitArr.length);
        for (var s : splitArr) {
            try {
                var parseInt = Integer.parseInt(s);
                set.add(parseInt);
            } catch (NumberFormatException e) {
                set.add(defaultValue);
            }
        }
        return set;
    }
}
