package com.jimmy.excel.operation;

import com.jimmy.excel.anno.CellProperty;
import com.jimmy.excel.anno.CellPropertyArray;
import com.jimmy.excel.exception.FileException;
import com.jimmy.excel.exception.ReadIOException;
import com.jimmy.excel.exception.StreamException;
import com.jimmy.excel.utils.ExcelCellOperationUtils;
import com.jimmy.utils.ClassUtils;
import com.jimmy.utils.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : aiden
 * @ClassName :  ExcelReadOperation
 * @Description : 读取Excel信息内容的操作对象
 * @date : 2019/3/14/014
 */
public class ExcelReadOperation {
    /**
     * 工作文件
     */
    private Workbook workbook;

    /**
     * 工作表
     */
    private Sheet sheet;
    /**
     * excel对象的数据流
     */
    private InputStream inputStream;

    /**
     * 当前行数
     */
    private Integer currentRow = 0;
    /**
     * 最后一行的行数
     */
    private Integer lastRow;

    /**
     * 私有构造函数
     */
    private ExcelReadOperation(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * 通过静态方法获取excel操作对象
     *
     * @param filePath 文件的路径
     * @return 读取的对象
     */
    public static ExcelReadOperation getInstance(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            throw new FileException("the file path is blank");
        }
        File file = new File(filePath);
        System.out.println(file.getAbsolutePath());
        if (!file.exists()) {
            throw new FileException("the file is not exists");
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            return getInstance(fileInputStream);
        } catch (FileNotFoundException e) {
            throw new FileException("the file is not exists");
        }

    }

    /**
     * 通过静态方法获取excel操作对象
     *
     * @param inputStream 流
     * @return 读取的对象
     */
    public static ExcelReadOperation getInstance(InputStream inputStream) {
        if (inputStream == null) {
            throw new StreamException("the stream is null");
        }
        return new ExcelReadOperation(inputStream);
    }

    public void createWorkbook() {
        if (workbook != null) {
            return;
        }
        try {
            workbook = WorkbookFactory.create(inputStream);

        } catch (IOException e) {
            throw new ReadIOException(e);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    public void openSheet(String sheetName) {
        createWorkbook();
        if (sheet == null) {
            if (sheetName == null) {
                this.sheet = workbook.getSheetAt(0);
            } else {
                this.sheet = workbook.getSheet(sheetName);
            }
            init();
        }
    }

    private void init() {
        lastRow = null;
        currentRow = 0;
    }

    public void openSheet() {
        openSheet(null);
    }

    private List<Cell> getRowContent(int rowNum) {
        openSheet();
        Row row = sheet.getRow(rowNum);
        Iterator<Cell> cellIterator = row.cellIterator();
        List<Cell> result = new ArrayList<>();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            result.add(cell);
        }
        return result;
    }

    public List<Cell> getCurrentRowContent() {
        List<Cell> resultList = getRowContent(currentRow);
        currentRow++;
        return resultList;
    }

    public Map<Integer, Cell> mapCurrentRowContent() {
        List<Cell> resultList = getCurrentRowContent();
        return resultList.stream().collect(Collectors.toMap(Cell::getColumnIndex, cell -> cell));

    }

    public boolean nextRow() {
        return getLastedRowNum() >= currentRow;
    }

    public boolean isExistsRow(int rowNum) {
        return getLastedRowNum() >= rowNum;
    }


    public int getLastedRowNum() {
        if (lastRow != null) {
            return lastRow;
        }
        openSheet();
        lastRow = sheet.getLastRowNum();
        return lastRow;
    }

    /**
     * 读取文件中或者流当中所有的数据并转换成相应的对象
     *
     * @param startRow 从对几行进行开始
     * @param tClass   对应的泛型的对象信息
     * @param <E>      泛型
     * @return List<返回的对象>
     */
    public <E> List<E> listAllContent(Class<E> tClass, Integer startRow) {
        return listAllContent(tClass, null, startRow, null);
    }

    /**
     * 读取文件中或者流当中所有的数据并转换成相应的对象
     *
     * @param startRow 从对几行进行开始
     * @param tClass   对应的泛型的对象信息
     * @param <E>      泛型
     * @return List<返回的对象>
     */
    public <E> List<E> listAllContent(Class<E> tClass, Integer startRow, Integer endRow) {
        return listAllContent(tClass, null, startRow, endRow);
    }

    /**
     * 读取文件中或者流当中所有的数据并转换成相应的对象
     *
     * @param startRow 从对几行进行开始
     * @param tClass   对应的泛型的对象信息
     * @param <E>      泛型
     * @return List<返回的对象>
     */
    public <E> List<E> listAllContent(Class<E> tClass, String groupName, Integer startRow, Integer endRow) {
        String group = groupName == null ? "" : groupName;
        List<Field> fieldList = ClassUtils.getFieldList(tClass);
        Map<Integer, String> columnMap = new HashMap<>();
        fieldList.forEach(field -> {
            CellPropertyArray cellPropertyArray = field.getAnnotation(CellPropertyArray.class);
            Set<Integer> columnSet = new HashSet<>();
            if (cellPropertyArray != null) {
                for (CellProperty cellProperty : cellPropertyArray.value()) {
                    if (cellProperty.group().equals(group)) {
                        columnSet.add(cellProperty.column());
                        columnMap.put(cellProperty.column(), field.getName());
                    }
                }
            }
            CellProperty cellProperty = field.getAnnotation(CellProperty.class);
            if (cellProperty != null) {
                columnSet.add(cellProperty.column());
                columnMap.put(cellProperty.column(), field.getName());
            }
            if (columnSet.size() > 1) {
                throw new ReadIOException("the filed anno is error the cloumn size:" + columnSet.size() + " in the same group,filed:" + field.getName());
            }
        });
        return listAllContent(columnMap, tClass, startRow, endRow);
    }

    /**
     * 读取文件中或者流当中所有的数据并转换成相应的对象
     *
     * @param columnMap 列与字段名想对应的
     * @param startRow  从对几行进行开始
     * @param tClass    对应的泛型的对象信息
     * @param <E>       泛型
     * @return List<返回的对象>
     */
    public <E> List<E> listAllContent(Map<Integer, String> columnMap, Class<E> tClass, Integer startRow, Integer endRow) {
        if (columnMap == null || columnMap.keySet().size() == 0) {
            return null;
        }
        openSheet();
        if (startRow != null && startRow >= 0) {
            currentRow = startRow;
        }
        if (endRow == null) {
            endRow = getLastedRowNum();
        }
        List<E> targetList = new ArrayList<>();
        /**
         * 进行转换
         */
        Set<Integer> columnSet = columnMap.keySet();
        List<Field> fieldList = ClassUtils.getFieldList(tClass);
        Map<String, Field> fieldMap = new HashMap<>();
        fieldList.forEach(field -> fieldMap.put(field.getName(), field));
        E target;
        Field field;
        Map<Integer, Cell> cellMap;
        while (nextRow() && endRow >= currentRow) {
            cellMap = mapCurrentRowContent();
            try {
                target = tClass.newInstance();
                for (Integer column : columnSet) {
                    Cell cell = cellMap.get(column);
                    if (cell == null) {
                        continue;
                    }
                    String filedName = columnMap.get(column);
                    field = fieldMap.get(filedName);
                    ExcelCellOperationUtils.setFiled(cell, field, target);
                }
                targetList.add(target);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return targetList;
    }


}
