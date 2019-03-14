package com.jimmy.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * JSON
 */
public final class JsonUtils {

    /**
     * 不可实例化
     */
    private JsonUtils() {
    }

    /**
     * 将对象转换为JSON字符串
     *
     * @param value 对象
     * @return JSOn字符串
     */
    public static String toJson(Object value) {
        return JSON.toJSONString(value);
    }

    /**
     * 将对象转换为JSON字符串
     *
     * @param value
     * @param arg
     * @return
     */
    public static String toJson(Object value, SerializerFeature arg) {
        return JSON.toJSONString(value, arg);
    }

    /**
     * 将JSON字符串转换为对象
     *
     * @param json      JSON字符串
     * @param valueType 对象类型
     * @return 对象
     */
    public static <T> T toObject(String json, Class<T> valueType) {
        Assert.notBlank(json);
        Assert.notNull(valueType);
        return JSON.parseObject(json, valueType);
    }

    /**
     * 将JSON字符串转换为对象集合
     *
     * @param json      JSON字符串
     * @param valueType 对象类型
     * @return 对象
     */
    public static <T> List<T> toObjectArray(String json, Class<T> valueType) {
        Assert.notBlank(json);
        Assert.notNull(valueType);
        return JSON.parseArray(json, valueType);
    }

    /**
     * 将对象转换为JSON流
     *
     * @param writer writer
     * @param value  对象
     */
    public static void writeValue(Writer writer, Object value) {
        try {
            writer.write(JSON.toJSONString(value));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
