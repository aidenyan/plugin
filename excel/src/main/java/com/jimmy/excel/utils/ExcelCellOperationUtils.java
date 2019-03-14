package com.jimmy.excel.utils;

import com.jimmy.excel.exception.ReadIOException;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;

/**
 * @author : aiden
 * @ClassName :  ExcelCellOperation
 * @Description :
 * @date : 2019/3/14/014
 */
public class ExcelCellOperationUtils {

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

}
