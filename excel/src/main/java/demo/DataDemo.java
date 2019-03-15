package demo;

import com.jimmy.excel.anno.CellProperty;
import lombok.Data;

/**
 * @author : aiden
 * @ClassName :  DataDemo
 * @Description :
 * @date : 2019/3/14/014
 */
@Data
public class DataDemo {
    @CellProperty(column = 0)
    private String result;
}
