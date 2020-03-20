package com.test.Excels;



import com.test.Model.*;
//import com.test.Model.ProdectServer;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ImportExcel {

    public static void importData(String fileName, MultipartFile mfile, Map map) {
        File uploadDir = new File("src/main/java/com/test/files/");
        //创建一个目录 （它的路径名由当前 File 对象指定，包括任一必须的父路径。）
        if (!uploadDir.exists()) uploadDir.mkdirs();
        //新建一个文件
        File tempFile = new File("src/main/java/com/test/files/" + fileName);
        if (!tempFile.exists()) {
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //初始化输入流
        InputStream is = null;
        try {
            //将上传的文件写入新建的文件中
            // 在spring boot 内嵌的tomcat中不work
            //mfile.transferTo(tempFile);

            FileUtils.copyInputStreamToFile(mfile.getInputStream(), tempFile);

            //根据新建的文件实例化输入流
            is = new FileInputStream(tempFile);

            //根据版本选择创建Workbook的方式
            Workbook wb = null;
            //根据文件名判断文件是2003版本还是2007版本
            if (ImportExcelUtils.isExcel2007(fileName)) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = new HSSFWorkbook(is);
            }
            //根据excel里面的内容读取知识库信息
            readExcelValue(wb, tempFile, map);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void readExcelValue(Workbook wb, File tempFile, Map map) {
        //错误信息接收器
        String errorMsg = "";
        //得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        //得到Excel的行数
        int totalRows = sheet.getPhysicalNumberOfRows();
        //总列数
        int totalCells = 0;
        //得到Excel的列数(前提是有行数)，从第二行算起
        if (totalRows >= 2 && sheet.getRow(1) != null) {
            totalCells = sheet.getRow(1).getPhysicalNumberOfCells();
        }
        String br = "\n";
        Row rowHeader = sheet.getRow(0);
        Row rowString = sheet.getRow(2);
        Row rowYorN = sheet.getRow(3);
        
        //获取excel首行每列第一个字符，并转小写
        List<String> CellList=new ArrayList<String>(); 
        for (int c = 0; c < totalCells; c++) {
        	Cell cellHeader = rowHeader.getCell(c);
        	CellList.add(cellHeader.toString().substring(0,1).toLowerCase());
        	
        }
        //ExcelGuig作为识别28类数据类别唯一标识
        String ExcelGuig = String.join("", CellList);
        System.out.println(ExcelGuig);
        
//        String aa = ChooseExcelUtils.getHostname(ExcelGuig);  //根据唯一标识获取Server
        List<ProdectServer> prodectServerList = new ArrayList<ProdectServer>();
        ProdectServer prodectServer;
        
        //循环Excel行数,从第二行开始。标题不入库
        for (int r = 4; r < totalRows; r++) {
            String rowMessage = "";
            Row row = sheet.getRow(r);
            if (row == null) {
                errorMsg += br + "第" + (r + 1) + "行数据有问题，请仔细检查！";
                continue;
            }
            prodectServer = new ProdectServer();
            //循环Excel的列
            //采用反射的方式判断每列数据并读取数据
            Class<? extends ProdectServer> aClass = prodectServer.getClass();
            Field[] declaredFields = aClass.getDeclaredFields();
            for (int c = 0; c < totalCells; c++) {
                Cell cell = row.getCell(c);
                Cell cellHeader = rowHeader.getCell(c);
                Cell rowStrings = rowString.getCell(c);
                Cell rowYorNs = rowYorN.getCell(c);
                if (null != cell) {
                    Object cellValue = ExcelUtils.getCellValue(cell);
                    if("YES".equals(rowYorNs.toString())){
                    	if(cellValue==null || "".equals(cellValue.toString())) {   //必填项不能出现空值判断
	                        rowMessage += "第" + (c + 1) + "列数据不能为空，请仔细检查；";
	                        break;
                    	}
                    } 
                    for (Field f : declaredFields) {
                        f.setAccessible(true);
                        if (f.getName().equals(String.valueOf(ExcelUtils.getCellValue(cellHeader)))) {
                            if ("YYYY-MM-DD".equals(rowStrings.toString()) && cellValue!=null) {  //判断时间格式，及必填项出现空值的判断
                            	if(!StringUtils.isboolDate(cellValue.toString())) {   //判断时间格式是否合格
                                    rowMessage += "第" + (c + 1) + "列数据不是有效的时间格式，请仔细检查；";     
                                    break;
                            	}
                            }
                            if (!"YYYY-MM-DD".equals(rowStrings.toString()) && cellValue!=null) {  //判断非时间格式，及非必填先出现空值的判断
                            	if (cellValue.toString().length() > Integer.parseInt(rowStrings.toString().substring(1))){   //判断是字符串长度是否超过限定长度
	                                 rowMessage += "第" + (c + 1) + "列数据超过限定长度，请仔细检查；";    
	                                 break;
                            	}
                             }
                            try {
                                switch (f.getGenericType().toString()) {
                                    case "class java.lang.String":
                                        f.set(prodectServer, String.valueOf(cellValue));
                                        break;
                                    case "int":
                                        f.set(prodectServer, Integer.valueOf(cellValue.toString()));
                                        break;
                                    default:
                                        break;
                                }
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                } else {
                    rowMessage += "第" + (c + 1) + "列数据有问题，请仔细检查；";
                }
            }
            //拼接每行的错误提示
            if (!StringUtils.isEmpty(rowMessage)) {
                errorMsg += br + "第" + (r + 1) + "行，" + rowMessage;
                break;
            } else {
            	prodectServerList.add(prodectServer);
            }
        }

//        删除上传的临时文件
//        if (tempFile.exists()) {
//            tempFile.delete();
//        }

        //全部验证通过才导入到数据库
        if (StringUtils.isEmpty(errorMsg)) {
            for (ProdectServer server : prodectServerList) {
                //TODO 插入到数据库
                System.out.println(server);
            }
            errorMsg = "导入成功，共" + prodectServerList.size() + "条数据！";
        }
        map.put("cloudServerList", prodectServerList);
        map.put("errorMsg", errorMsg);
    }

}
