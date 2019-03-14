package com.jimmy.excel.operation;

import com.jimmy.excel.exception.FileException;
import com.jimmy.excel.exception.ReadIOException;
import com.jimmy.excel.exception.StreamException;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    private XSSFWorkbook workbook;

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
            workbook = new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            throw new ReadIOException(e);
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

    public boolean nextRow() {
        return getLastedRowNum() > currentRow;
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
     * @param columnMap 列与字段名想对应的
     * @param startRow  从对几行进行开始
     * @param tClass    对应的泛型的对象信息
     * @param <E>       泛型
     * @return List<返回的对象>
     */
    public <E> List<E> listAllContent(Map<Integer, String> columnMap, Class<E> tClass, Integer startRow) {
        if (columnMap == null || columnMap.keySet().size() == 0) {
            return null;
        }
        openSheet();
        if (startRow != null && startRow >= 0) {
            currentRow = startRow;
        }
        List<E> targetList = new ArrayList<>();
        /**
         * 进行转换
         */

        return targetList;
    }

}
