package demo;

import com.alibaba.fastjson.JSON;
import com.jimmy.excel.operation.ExcelReadOperation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : aiden
 * @ClassName :  ReadOperationDemo
 * @Description :
 * @date : 2019/3/14/014
 */
public class ReadOperationDemo {


    public static void main(String[] arg) {
        String path=ReadOperationDemo.class.getClassLoader().getResource("demo.xls").getPath();
        ExcelReadOperation excelReadOperation = ExcelReadOperation.getInstance(path);
        Map<Integer, String> columnMap = new HashMap<>();
        columnMap.put(0, "result");
        List<DataDemo> result = excelReadOperation.listAllContent( DataDemo.class, 1,null);
        System.out.println(JSON.toJSONString(result));
    }
}
