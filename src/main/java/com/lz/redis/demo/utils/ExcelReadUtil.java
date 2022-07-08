package com.lz.redis.demo.utils;

import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileInputStream;

/**
 * @author : liuze
 * @date: 2022/6/30 17:08
 **/
public class ExcelReadUtil {

    public static void testLoad() throws Exception{
        FileInputStream in = new FileInputStream("C:\\Download\\汇总数据.xlsx");
        Workbook wk = StreamingReader.builder()
                .rowCacheSize(10000)  //缓存到内存中的行数，默认是10
                .bufferSize(4096)  //读取资源时，缓存到内存的字节大小，默认是1024
                .open(in);  //打开资源，必须，可以是InputStream或者是File，注意：只能打开XLSX格式的文件
        Sheet sheet = wk.getSheetAt(0);
        //遍历所有的行
        int count = 0;
        for (Row row : sheet) {
            count++;
//            System.out.println("开始遍历第" + row.getRowNum() + "行数据：");
//            //遍历所有的列
//            for (Cell cell : row) {
//                System.out.print(cell.getStringCellValue() + " ");
//            }
//            System.out.println(" ");
        }
        System.out.println("总行数为:"+count);
    }

    public static void main(String[] args) throws Exception {
        testLoad();
    }
}
