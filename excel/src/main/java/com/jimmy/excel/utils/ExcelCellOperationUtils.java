package com.jimmy.excel.utils;

import com.jimmy.excel.exception.ReadIOException;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author : aiden
 * @ClassName :  ExcelCellOperation
 * @Description :
 * @date : 2019/3/14/014
 */
public class ExcelCellOperationUtils {
    private static List<Class<?>> baseClassList = Arrays.asList(boolean.class, double.class, char.class, byte.class, int.class, long.class, float.class, short.class);

    private static List<Class<?>> baseClassObjectList = Arrays.asList(Boolean.class, Double.class, Character.class, Byte.class, Integer.class, Long.class, Float.class, Short.class);


    public static Object convertCell(Cell cell) {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BLANK:
                return null;
            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue();
            case Cell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                }
                return cell.getNumericCellValue();
            case Cell.CELL_TYPE_STRING:
                return cell.getRichStringCellValue().getString();
            case Cell.CELL_TYPE_ERROR:
                throw new ReadIOException(" the excel cell is error[" + cell.getRowIndex() + "," + cell.getColumnIndex() + "]");
            case Cell.CELL_TYPE_FORMULA:
                throw new ReadIOException("the content is formula is error[" + cell.getRowIndex() + "," + cell.getColumnIndex() + "]");
        }
        return null;
    }


    public static void setFiled(Cell cell, Field field, Object target) throws IllegalAccessException {
        Object cellObj = convertCell(cell);
        Class<?> targetClass = field.getType();
        if (baseClassList.contains(targetClass) && cellObj == null) {
            throw new ReadIOException(" the excel cell content is blank,the cell:[" + cell.getRowIndex() + "," + cell.getColumnIndex() + "],the target class:" + targetClass.getName() + ";cell class:" + Date.class.getName());
        }
        if (Character.class.equals(targetClass) || char.class.equals(targetClass)) {
            throw new ReadIOException(" the field not char or Character");
        }
        if (cellObj == null) {
            return;
        }
        if ((baseClassList.contains(targetClass) || baseClassObjectList.contains(targetClass)) && (cellObj instanceof Date)) {
            throw new ReadIOException(" the excel cell is format error,the cell:[" + cell.getRowIndex() + "," + cell.getColumnIndex() + "],the target class:" + targetClass.getName() + ";cell class:" + Date.class.getName());
        }
        if (targetClass.equals(Character.class) || targetClass.equals(char.class)) {
            throw new ReadIOException(" the excel cell is format error,the cell:[" + cell.getRowIndex() + "," + cell.getColumnIndex() + "],the target class:" + targetClass.getName() + ";cell class:" + Date.class.getName());
        }

        if (targetClass.equals(Date.class)) {
            if (cellObj instanceof Date) {
                field.setAccessible(true);
                field.set(target, cellObj);
                return;
            } else {
                throw new ReadIOException(" the excel cell is format error,the cell:[" + cell.getRowIndex() + "," + cell.getColumnIndex() + "]the targetClass:" + targetClass.getName());
            }
        }
        if (targetClass.equals(String.class)) {
            field.setAccessible(true);
            field.set(target, String.valueOf(cellObj));
            return;
        }
        if (cellObj instanceof String) {
            String cellStr = (String) cellObj;
            if (targetClass.equals(Double.class) || targetClass.equals(double.class)) {
                field.setAccessible(true);
                field.set(target, Double.parseDouble(cellStr));
                return;
            } else if (targetClass.equals(Integer.class) || targetClass.equals(int.class)) {
                field.setAccessible(true);
                field.set(target, Integer.parseInt(cellStr));
                return;
            } else if (targetClass.equals(Float.class) || targetClass.equals(float.class)) {
                field.setAccessible(true);
                field.set(target, Float.parseFloat(cellStr));
                return;
            } else if (targetClass.equals(Short.class) || targetClass.equals(short.class)) {
                field.setAccessible(true);
                field.set(target, Short.parseShort(cellStr));
                return;
            } else if (targetClass.equals(Long.class) || targetClass.equals(long.class)) {
                field.setAccessible(true);
                field.set(target, Long.parseLong(cellStr));
                return;
            } else if (targetClass.equals(Byte.class) || targetClass.equals(byte.class)) {
                field.setAccessible(true);
                field.set(target, Byte.parseByte(cellStr));
                return;
            } else if (targetClass.equals(Boolean.class) || targetClass.equals(boolean.class)) {
                field.setAccessible(true);
                field.set(target, Boolean.parseBoolean(cellStr));
                return;
            }
        } else if (cellObj instanceof Double) {
            Double doubleCell = (Double) cellObj;
            if (targetClass.equals(Double.class) || targetClass.equals(double.class)) {
                field.setAccessible(true);
                field.set(target, doubleCell);
                return;
            } else if (targetClass.equals(Integer.class) || targetClass.equals(int.class)) {
                field.setAccessible(true);
                field.set(target, doubleCell.intValue());
                return;
            } else if (targetClass.equals(Float.class) || targetClass.equals(float.class)) {
                field.setAccessible(true);
                field.set(target, doubleCell.floatValue());
                return;
            } else if (targetClass.equals(Short.class) || targetClass.equals(short.class)) {
                field.setAccessible(true);
                field.set(target, doubleCell.shortValue());
                return;
            } else if (targetClass.equals(Long.class) || targetClass.equals(long.class)) {
                field.setAccessible(true);
                field.set(target, doubleCell.longValue());
                return;
            } else if (targetClass.equals(Byte.class) || targetClass.equals(byte.class)) {
                field.setAccessible(true);
                field.set(target, doubleCell.byteValue());
                return;
            }
        } else if (cellObj instanceof Boolean) {
            if (targetClass.equals(Boolean.class) || targetClass.equals(boolean.class)) {
                field.setAccessible(true);
                field.set(target, cellObj);
                return;
            }
        }


    }
}
