package com.jimmy.excel.anno;

import java.lang.annotation.*;

/**
 * @author : aiden
 * @ClassName :  CellPropertyArray
 * @Description :
 * @date : 2019/3/15/015
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CellPropertyArray {
    CellProperty[] value();
}
