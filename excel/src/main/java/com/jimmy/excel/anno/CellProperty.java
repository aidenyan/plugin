package com.jimmy.excel.anno;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2019/3/15/015.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(CellPropertyArray.class)
public @interface CellProperty {
    int column();

    String group() default "";

    String format() default "";

}
