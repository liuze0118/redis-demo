package com.lz.redis.demo.utils;

import com.lz.redis.demo.model.entity.mysql.HealthInterfaceInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : liuze
 * @date: 2022/8/25 10:47
 **/
public class PoiExcelUtils {

    public static List<HealthInterfaceInfo> getDataFromExcel(InputStream inputStream) throws Exception {
        ZipSecureFile.setMinInflateRatio(-1.0d);
        Workbook workBook = new XSSFWorkbook(inputStream);
        List<HealthInterfaceInfo> interfaceInfoList = new ArrayList<>();
        for (int j = 0; j < workBook.getNumberOfSheets(); j++) {
            Sheet sheet = workBook.getSheetAt(0);
            Row rowHead = sheet.getRow(j);
            if (rowHead.getPhysicalNumberOfCells() < 1) {
                throw new Exception("表头错误");
            }
            String module = "";
            String serviceName = "";
            String version = "";
            for (int i = 1; i < sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if(i == 1){
                    module = row.getCell(0) +"";
                    serviceName = row.getCell(1) +"";
                    version = row.getCell(2) +"";
                }
                if(row.getCell(3) != null && !StringUtils.isEmpty(row.getCell(3)+"") && !StringUtils.isEmpty(row.getCell(4)+"")){
                    if(!StringUtils.isEmpty(row.getCell(0) + "") && i !=1){
                        module = row.getCell(0) + "";
                    }
                    if(!StringUtils.isEmpty(row.getCell(1) + "") && i !=1){
                        serviceName = row.getCell(1) + "";
                    }
                    if(!StringUtils.isEmpty(row.getCell(2) + "") && i !=1){
                        version = row.getCell(2)+ "";
                    }
                    HealthInterfaceInfo healthInterfaceInfo = HealthInterfaceInfo.builder().module(module)
                            .serviceName(serviceName)
                            .version(version)
                            .docUrl(row.getCell(3) + "")
                            .url(row.getCell(4) + "")
                            .describe(row.getCell(5) + "")
                            .department(row.getCell(6) + "")
                            .chain(row.getCell(7) + "")
                            .project(row.getCell(8) + "")
                            .remark(row.getCell(9) + "").build();
                    interfaceInfoList.add(healthInterfaceInfo);
                }
            }
        }

        return interfaceInfoList;
    }
}
