package com.ppxb.la.base.common.enumeration;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.CaseFormat;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Objects;

public interface BaseEnum {

    static String getInfo(Class<? extends BaseEnum> clazz) {
        var enums = clazz.getEnumConstants();
        var json = new LinkedHashMap<>(enums.length);
        for (BaseEnum e : enums) {
            var jsonObject = new JSONObject();
            jsonObject.put("value", new DeletedQuotationAware(e.getValue()));
            jsonObject.put("desc", new DeletedQuotationAware(e.getDesc()));
            json.put(e.toString(), jsonObject);
        }

        var enumJson = JSON.toJSONString(json, true);
        enumJson = enumJson.replaceAll("\"", "");
        enumJson = enumJson.replaceAll("\t", "&nbsp;&nbsp;");
        enumJson = enumJson.replaceAll("\n", "<br/>");
        var prefix = "  <br>  export const " + CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, clazz.getSimpleName() + " = <br/> ");
        return prefix + enumJson + " <br/>";
    }

    Object getValue();

    String getDesc();

    default boolean equalsValue(Object value) {
        return Objects.equals(getValue(), value);
    }

    default boolean equals(BaseEnum baseEnum) {
        return Objects.equals(getValue(), baseEnum.getValue()) && Objects.equals(getDesc(), baseEnum.getDesc());
    }

    @Data
    class DeletedQuotationAware implements JSONAware {

        private final String value;

        public DeletedQuotationAware(Object value) {
            if (value instanceof String) {
                this.value = "'" + value + "'";
            } else {
                this.value = value.toString();
            }
        }

        @Override
        public String toJSONString() {
            return value;
        }
    }
}
