package com.jimmy.utils;

/**
 * @author : aiden
 * @ClassName :  com.jimmy.utils.Assert
 * @Description :内部使用的断言
 * @date : 2019/3/6/006
 */
public class Assert {

    private Assert() {
    }

    /**
     * 断言目标不是空字符串
     *
     * @param target  目标字符串
     * @param message 异常信息
     */
    public static void notBlank(String target, String message) {
        if (StringUtils.isBlank(target)) {
            throw new RuntimeException(message);
        }
    }

    /**
     * 断言目标不是空字符串
     *
     * @param target 目标字符
     */
    public static void notBlank(String target) {
        notBlank(target, null);
    }

    /**
     * 断言目标不是空的
     *
     * @param target  目标对象
     * @param message 异常信息
     */
    public static void notNull(Object target, String message) {
        if (target == null) {
            throw new RuntimeException(message);
        }
    }

    /**
     * 断言目标不是空的
     *
     * @param target 目标对象
     */
    public static void notNull(Object target) {
        notNull(target, null);
    }
}
