package com.jimmy.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author : aiden
 * @ClassName :  com.jimmy.utils.ClassUtils
 * @Description :
 * @date : 2019/1/31/031
 */
public class ClassUtils extends org.apache.commons.lang.ClassUtils {



    public static List<Field> getFieldList(Class<?> tClass) {
        if (tClass == null) {
            return new ArrayList<>();
        }
        Field[] fields = tClass.getDeclaredFields();
        List<Field> resultList = new ArrayList<>();
        if (fields != null) {
            resultList.addAll(Arrays.asList(fields));
        }
        resultList.addAll(getFieldList(tClass.getSuperclass()));
        return resultList;
    }

    public static List<Class<?>> getClassList(Class<?> tClass) {
        List<Class<?>> resultList = new ArrayList<>();
        if (tClass == null) {
            return resultList;
        }
        resultList.add(tClass);
        Class<?>[] tClassInterfaces = tClass.getInterfaces();
        if (tClassInterfaces != null && tClassInterfaces.length > 0) {
            resultList.addAll(Arrays.asList(tClassInterfaces));
            for (Class<?> tClazz : tClassInterfaces) {
                resultList.addAll(getClassList(tClazz));
            }
        }
        Class<?> tClassSuperclass = tClass.getSuperclass();
        resultList.addAll(getClassList(tClassSuperclass));
        return resultList;
    }

    public static <T> boolean contatin(Class<?> tClass, Class<T> targetClass) {
        return getClassList(tClass).contains(targetClass);
    }
}