package com.reminis.exceldemo.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.reminis.exceldemo.annotation.ExcelColumn;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Excel工具类
 */
public class ExcelUtils {

    public final static Logger log = LoggerFactory.getLogger(ExcelUtils.class);

    public final static String EXCEL2003 = "xls";
    public final static String EXCEL2007 = "xlsx";

    /* public static <T> List<T> readExcel(String path, Class<T> cls, MultipartFile file)  throws Exception{
         //  System.out.println("进入readExcel");
         if (file.isEmpty()) {
             log.error("上传文件不能为空");
         }

         String fileName = file.getOriginalFilename();
         if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
             log.error("上传文件格式不正确");
         }
         List<T> dataList = new ArrayList<>();
         Workbook workbook = null;

             InputStream is = file.getInputStream();
             if (fileName.endsWith(EXCEL2007)) {
 //                FileInputStream is = new FileInputStream(new File(path));
                 workbook = new XSSFWorkbook(is);
             }
             if (fileName.endsWith(EXCEL2003)) {
 //                FileInputStream is = new FileInputStream(new File(path));
                 workbook = new HSSFWorkbook(is);
             }
             if (workbook != null) {
                 //类映射  注解 value-->bean columns
                 Map<String, List<Field>> classMap = new HashMap<>();
                 List<Field> fields = Stream.of(cls.getDeclaredFields()).collect(Collectors.toList());
                 fields.forEach(
                         field -> {
                             ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                             if (annotation != null) {
                                 String value = annotation.value();
                                 if (StringUtils.isBlank(value)) {
                                     return;//return起到的作用和continue是相同的 语法
                                 }
                                 if (!classMap.containsKey(value)) {
                                     classMap.put(value, new ArrayList<>());
                                 }
                                 field.setAccessible(true);
                                 classMap.get(value).add(field);
                             }
                         }
                 );
                 //后面使用它来 执行计算公式
                 // FormulaEvaluator formulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
                 FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
                 //索引-->columns
                 Map<Integer, List<Field>> reflectionMap = new HashMap<>(16);
                 //默认读取第一个sheet
                 Sheet sheet = workbook.getSheetAt(0);
                 //索引-->columns
                 boolean firstRow = true;

                 for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
                     Row row = sheet.getRow(i);
                     //首行  提取注解
                     if (firstRow) {
                         for (int j = row.getFirstCellNum(); j<= row.getLastCellNum(); j++) {
                             Cell cell = row.getCell(j);
                             String cellValue = getCellValue(cell);
                             if (classMap.containsKey(cellValue)) {
                                 // System.out.println("打印存入的字段："+cellValue);
                                 reflectionMap.put(j, classMap.get(cellValue));
                             }
                         }
                         firstRow = false;
                     }
                     else {//读取第二行及以下的数据
                         // System.out.println("进入读取非首行数据else");
                         //忽略空白行
                         if (row == null) {
                             continue;
                         }

                             // System.out.println("进入读取非首行数据try");
                            // T t = cls.newInstance();
                         // 获取类的构造函数
                         Constructor<T> constructor = null;
                         try {
                             constructor = cls.getDeclaredConstructor();
                             constructor.setAccessible(true);
                         } catch (NoSuchMethodException e) {
                             log.error("类 {} 没有无参构造函数", cls.getName(), e);
                             continue;
                         }

                         T t = null;
                         try {
                             t = constructor.newInstance();
                         } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                             log.error("创建实例失败: {}", e.getMessage(), e);
                             if (e.getCause() instanceof Exception) {
                                 log.error("根本原因: {}", e.getCause().getMessage(), e.getCause());
                             }
                             continue;
                         }
                         // 判断是否为空白行
                             //判断是否为空白行
                             boolean allBlank = true;
                             // System.out.println("打印获取每行的列数"+row.getFirstCellNum());
                             for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                                 // System.out.println("进入读取非首行数据for"+i);
                                 if (reflectionMap.containsKey(j)) {
                                     //    System.out.println("进入读取非首行数据if,获取每列数据"+j);
                                     Cell cell = row.getCell(j);
                                     //测试验证读取小数数据正常
                                    *//*  String cellstring = cell.toString();
                                       if(cellstring.equals("3044.4")){
                                          System.out.println("打印3044.4对应转换值"+getCellValue(cell));
                                        }*//*
                                    //getCellValue解析单元格数据
                                    String cellValue = getCellValue(cell);
                                    //  System.out.println("进入读取非首行数据if,获取每列数据cellValue:"+cellValue);
                                    if (cellValue.contains( "WW-")){//截取材料名称WW-
                                        String key = "WW-";
                                        int indexOf = cellValue.indexOf(key);
                                        if (indexOf != -1) {
                                            cellValue = key+cellValue.substring(indexOf+key.length()).split(" ")[0];
                                            //   System.out.println("打印截取后的材料名称:"+cellValue);
                                        }
                                    }
                                    //判断单元格是否为公式，执行计算公式
                                    if (cell == null) {
                                        cellValue = "";
                                   // }else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA){
                                    }
                                    if (cell.getCellType() == CellType.FORMULA){
                                        // System.out.println("进入判断为公式类型");
                                        String cellFormula = cell.getCellFormula();
                                        // 评估单元格的公式，并获取结果
                                        CellValue evaluate = formulaEvaluator.evaluate(cell);
                                        //System.out.println("公式：" + cellFormula);
                                        BigDecimal bd = new BigDecimal(evaluate.getNumberValue());
                                        bd = bd.setScale(4, RoundingMode.HALF_UP); // 设置小数点后四位，并进行四舍五入
                                        // System.out.println("计算公式前的值：" + bd);

                                        DataFormatter dataFormatter = new DataFormatter();
                                        // 使用 dataFormatter 格式化公式结果
                                        String formattedValue = dataFormatter.formatCellValue(cell, formulaEvaluator);
                                        // 去掉公式中的逗号
                                        formattedValue = formattedValue.replace(",", "");
                                        if (evaluate.getCellType() == CellType.NUMERIC && evaluate.getNumberValue() == 0) {
                                            // 公式计算结果为0，可以抛出自定义异常或返回特定错误信息
                                            throw new RuntimeException("Excel总价公式计算结果为0，请检查Excel数量及单价数据是否正确！");
                                            // 或者返回错误信息给前端
                                            // return "公式计算结果为0，请检查输入数据是否正确！";
                                        }
                                        cellValue = bd.toString();

                                    }
                                    if (cellValue.contains( "PD")){//截取采购单号PD24101032示例，PD24101032()
                                        String key = "(";
                                        int indexOf = cellValue.indexOf(key);
                                        if (indexOf != -1) {
                                            //截取采购单号PD24101032示例，PD24101032(N41663.N42326.N42431.N42436-N42439.N42442)
                                            cellValue = cellValue.substring(0,indexOf);
                                            //   System.out.println("打印截取后的采购单号:"+cellValue);
                                        }
                                    }
                                    if (StringUtils.isNotBlank(cellValue)) {
                                        allBlank = false;
                                    }

                                    List<Field> fieldList = reflectionMap.get(j);
                                    //替代forEach循环赋值
                                    for (int k = 0; k <fieldList.size() ; k++) {
                                        Field field = fieldList.get(k);
                                        // System.out.println("打印获取每列数据field:"+field.getName()+",数据类型："+field.getType());

                                        field.set(t,cellValue);
                                        // System.out.println("field.set结束,打印获取每列数据field:"+field.getName()+",数据类型："+field.getType()+",数据值："+cellValue);
                                    }
                                   *//* fieldList.forEach(
                                            x -> {
                                                try {
                                                    handleField(t, cellValue, x);
                                                } catch (Exception e) {
                                                    log.error(String.format("reflect field:%s value:%s exception!", x.getName(), cellValue), e);
                                                }
                                            }
                                    );*//*
                                }
                            }
                            if (!allBlank) {
                                dataList.add(t);
                            } else {
                                log.warn(String.format("row:%s is blank ignore!", i));
                            }

                    }
                }


            }

        // System.out.println("结束readExcel:"+dataList);
        return dataList;
    }*/
    public static <T> List<T> readExcel(String path, Class<T> cls, MultipartFile file){
        //  System.out.println("进入readExcel");
        if (file.isEmpty()) {
            log.error("上传文件不能为空");
        }

        String fileName = file.getOriginalFilename();
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            log.error("上传文件格式不正确");
        }
        List<T> dataList = new ArrayList<>();
        Workbook workbook = null;
        try {
            InputStream is = file.getInputStream();
            if (fileName.endsWith(EXCEL2007)) {
//                FileInputStream is = new FileInputStream(new File(path));
                workbook = new XSSFWorkbook(is);
            }
            if (fileName.endsWith(EXCEL2003)) {
//                FileInputStream is = new FileInputStream(new File(path));
                workbook = new HSSFWorkbook(is);
            }
            if (workbook != null) {
                //类映射  注解 value-->bean columns
                Map<String, List<Field>> classMap = new HashMap<>();
                List<Field> fields = Stream.of(cls.getDeclaredFields()).collect(Collectors.toList());
                fields.forEach(
                        field -> {
                            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                            if (annotation != null) {
                                String value = annotation.value();
                                if (StringUtils.isBlank(value)) {
                                    return;//return起到的作用和continue是相同的 语法
                                }
                                if (!classMap.containsKey(value)) {
                                    classMap.put(value, new ArrayList<>());
                                }
                                field.setAccessible(true);
                                classMap.get(value).add(field);
                            }
                        }
                );
                //后面使用它来 执行计算公式
                // FormulaEvaluator formulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
                FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
                //索引-->columns
                Map<Integer, List<Field>> reflectionMap = new HashMap<>(16);
                //默认读取第一个sheet
                Sheet sheet = workbook.getSheetAt(0);
                //索引-->columns
                boolean firstRow = true;

                for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    //首行  提取注解
                    if (firstRow) {
                        for (int j = row.getFirstCellNum(); j<= row.getLastCellNum(); j++) {
                            Cell cell = row.getCell(j);
                            String cellValue = getCellValue(cell);
                            // System.out.println("打印Excel上传第一行列名字段："+cellValue);
                            if(cellValue.equals("Raw Material Catagory")){
                                cellValue ="大类";//如果表头列名为"Raw Material Catagory"，重新将该列命名为"大类"
                            }
                            if (classMap.containsKey(cellValue)) {
                                // System.out.println("打印存入的字段："+cellValue);
                                reflectionMap.put(j, classMap.get(cellValue));
                            }
                        }
                        firstRow = false;
                    }
                    else {//读取第二行及以下的数据
                        // System.out.println("进入读取非首行数据else");
                        //忽略空白行
                        if (row == null) {
                            continue;
                        }
                        try {
                            // System.out.println("进入读取非首行数据try");
                            T t = cls.newInstance();
                            //判断是否为空白行
                            boolean allBlank = true;
                            // System.out.println("打印获取每行的列数"+row.getFirstCellNum());
                            for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                                // System.out.println("进入读取非首行数据for"+i);
                                if (reflectionMap.containsKey(j)) {
                                    //    System.out.println("进入读取非首行数据if,获取每列数据"+j);
                                    Cell cell = row.getCell(j);
                                    //测试验证读取小数数据正常
                                   /*  String cellstring = cell.toString();
                                       if(cellstring.equals("3044.4")){
                                          System.out.println("打印3044.4对应转换值"+getCellValue(cell));
                                        }*/
                                    //getCellValue解析单元格数据
                                    String cellValue = getCellValue(cell);
                                    //  System.out.println("进入读取非首行数据if,获取每列数据cellValue:"+cellValue);
                                    if (cellValue.contains( "WW-")){//截取材料名称WW-
                                        String key = "WW-";
                                        int indexOf = cellValue.indexOf(key);
                                        if (indexOf != -1) {
                                            cellValue = key+cellValue.substring(indexOf+key.length()).split(" ")[0];
                                            //   System.out.println("打印截取后的材料名称:"+cellValue);
                                        }
                                    }
                                    //判断单元格是否为公式，执行计算公式
                                    if (cell == null) {
                                        cellValue = "";
                                        // }else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA){
                                    }else if (cell.getCellType() == CellType.FORMULA){
                                        // System.out.println("进入判断为公式类型");
                                        String cellFormula = cell.getCellFormula();
                                        // 评估单元格的公式，并获取结果
                                        CellValue evaluate = formulaEvaluator.evaluate(cell);
                                        //System.out.println("公式：" + cellFormula);
                                        BigDecimal bd = new BigDecimal(evaluate.getNumberValue());
                                        bd = bd.setScale(4, RoundingMode.HALF_UP); // 设置小数点后四位，并进行四舍五入
                                        // System.out.println("计算公式前的值：" + bd);

                                        DataFormatter dataFormatter = new DataFormatter();
                                        // 使用 dataFormatter 格式化公式结果
                                        String formattedValue = dataFormatter.formatCellValue(cell, formulaEvaluator);
                                        // 去掉公式中的逗号
                                        formattedValue = formattedValue.replace(",", "");

                                        cellValue = bd.toString();

                                    }
                                    if (cellValue.contains( "PD")){//截取采购单号PD24101032示例，PD24101032()
                                        String key = "(";
                                        int indexOf = cellValue.indexOf(key);
                                        if (indexOf != -1) {
                                            //截取采购单号PD24101032示例，PD24101032(N41663.N42326.N42431.N42436-N42439.N42442)
                                            cellValue = cellValue.substring(0,indexOf);
                                            //   System.out.println("打印截取后的采购单号:"+cellValue);
                                        }
                                    }
                                    if (StringUtils.isNotBlank(cellValue)) {
                                        allBlank = false;
                                    }

                                    List<Field> fieldList = reflectionMap.get(j);
                                    //替代forEach循环赋值
                                    for (int k = 0; k <fieldList.size() ; k++) {
                                        Field field = fieldList.get(k);
                                        // System.out.println("打印获取每列数据field:"+field.getName()+",数据类型："+field.getType());

                                        field.set(t,cellValue);
                                        // System.out.println("field.set结束,打印获取每列数据field:"+field.getName()+",数据类型："+field.getType()+",数据值："+cellValue);
                                    }
                                   /* fieldList.forEach(
                                            x -> {
                                                try {
                                                    handleField(t, cellValue, x);
                                                } catch (Exception e) {
                                                    log.error(String.format("reflect field:%s value:%s exception!", x.getName(), cellValue), e);
                                                }
                                            }
                                    );*/
                                }
                            }
                            if (!allBlank) {
                                dataList.add(t);
                            } else {//忽略空白行警告信息，不打印
                                //log.warn(String.format("row:%s is blank ignore!", i));
                            }
                        } catch (Exception e) {
                            log.error(String.format("parse row:%s exception!", i), e);
                        }
                    }
                }


            }
        } catch (Exception e) {
            log.error(String.format("parse excel exception!"), e);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    log.error(String.format("parse excel exception!"), e);
                }
            }
        }
        // System.out.println("结束readExcel:"+dataList);
        return dataList;
    }

    public static <T> void handleField(T t, String value, Field field) throws Exception {
        Class<?> type = field.getType();
        if (type == void.class || StringUtils.isBlank(value)) {
            return;
        }
        if (type == Object.class) {
            field.set(t, value);
        } else if (type.getSuperclass() == null || type.getSuperclass() == Number.class) {
            if (type == int.class || type == Integer.class) {
                field.set(t, NumberUtils.toInt(value));
            } else if (type == long.class || type == Long.class) {
                field.set(t, NumberUtils.toLong(value));
            } else if (type == byte.class || type == Byte.class) {
                field.set(t, NumberUtils.toByte(value));
            } else if (type == short.class || type == Short.class) {
                field.set(t, NumberUtils.toShort(value));
            } else if (type == double.class || type == Double.class) {
                field.set(t, NumberUtils.toDouble(value));
            } else if (type == float.class || type == Float.class) {
                field.set(t, NumberUtils.toFloat(value));
            } else if (type == char.class || type == Character.class) {
                field.set(t, CharUtils.toChar(value));
            } else if (type == boolean.class) {
                field.set(t, BooleanUtils.toBoolean(value));
            } else if (type == BigDecimal.class) {
                field.set(t, new BigDecimal(value));
            }
        } else if (type == Boolean.class) {
            field.set(t, BooleanUtils.toBoolean(value));
        } else if (type == Date.class) {
            field.set(t, value);
        } else if (type == String.class) {
            field.set(t, value);
        } else if (type == LocalDateTime.class) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dt = LocalDateTime.parse(value, df);
            field.set(t, dt);
        } else {
            Constructor<?> constructor = type.getConstructor(String.class);
            field.set(t, constructor.newInstance(value));
        }
    }

    public static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        if (cell.getCellType() == CellType.NUMERIC) {//判断数据类型为数字类型
            if (HSSFDateUtil.isCellDateFormatted(cell)) {//判断为日期类型
                Date dateCellValue = cell.getDateCellValue();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");//格式日期类型，参考Excel中类型的格式
                return simpleDateFormat.format(dateCellValue);
            } else {//判断为非日期类型的数字，转化为小数类型的字符串
                Double d = cell.getNumericCellValue();
                DecimalFormat df = new DecimalFormat("#.####");
                String value = df.format(d);
                return value;
            }
        } else if (cell.getCellType() == CellType.STRING) {
            return StringUtils.trimToEmpty(cell.getStringCellValue());
        } else if (cell.getCellType() == CellType.BLANK) {
            return "";
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == CellType.ERROR) {
            return "ERROR";
        } else {
            return cell.toString().trim();
        }
    }

    public static <T> void writeExcel(HttpServletRequest request, HttpServletResponse response,String fileName, List<T> dataList, Class<T> cls) {
        if (CollectionUtils.isEmpty(dataList)) {
            log.warn("Data list is empty, no data to write to Excel.");
            return;
        }

        Field[] fields = cls.getDeclaredFields();
        List<Field> fieldList = Arrays.stream(fields)
                .filter(field -> {
                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    if (annotation != null && annotation.col() > 0) {
                        field.setAccessible(true);
                        return true;
                    }
                    return false;
                })
                .sorted(Comparator.comparing(field -> {
                    int col = 0;
                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    if (annotation != null) {
                        col = annotation.col();
                    }
                    return col;
                }))
                .collect(Collectors.toList());

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Sheet1");

        AtomicInteger ai = new AtomicInteger();
        {
            Row row = sheet.createRow(ai.getAndIncrement());
            AtomicInteger aj = new AtomicInteger();
            fieldList.forEach(field -> {
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                String columnName = "";
                if (annotation != null) {
                    columnName = annotation.value();
                }
                Cell cell = row.createCell(aj.getAndIncrement());

                CellStyle  cellStyle = wb.createCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cellStyle.setAlignment(HorizontalAlignment.CENTER);

               /* Font font = wb.createFont();
                font.setBold(true); // 设置字体加粗
                cellStyle.setFont(font);*/

                cell.setCellStyle(cellStyle);
                cell.setCellValue(columnName);
            });
        }

        if (CollectionUtils.isNotEmpty(dataList)) {
            for (int i = 0; i < dataList.size(); i++) {
                int rowIndex = i + 1;
                Row contentRow = sheet.createRow(rowIndex); // 修改这里
                //新增设置列宽,设置列宽为5000
                sheet.setColumnWidth(i, 3000);

                T t = dataList.get(i);
                AtomicInteger aj = new AtomicInteger();
                fieldList.forEach(field -> {
                    Class<?> type = field.getType();
                    Object value = "";
                    try {
                        value = field.get(t);
                    } catch (Exception e) {
                        log.error("Failed to get value from field: {}", field.getName(), e);
                    }
                    Cell cell = contentRow.createCell(aj.getAndIncrement());
                    //新增居中样式
                    CellStyle  cellStyle2 = wb.createCellStyle();
                    cellStyle2.setAlignment(HorizontalAlignment.CENTER);
                    cell.setCellStyle(cellStyle2);
                    //新增居中样式 end
                    if (value != null) {
                        // System.out.println("打印type类型："+type);
                        if (type == Date.class) {//要先设置样式，然后再填入值。。。。。。。。。。。。才能生效
                            CellStyle cellStyle = wb.createCellStyle();
                            //  cellStyle.setDataFormat(wb.createDataFormat().getFormat("yyyy/M/d"));//设置日期格式的单元格
                            cellStyle.setDataFormat(wb.createDataFormat().getFormat("yyyy/m/d"));//设置日期格式的单元格
                            cell.setCellStyle(cellStyle);

                           // SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                           // value = sdf.format((Date) value);
                           // cell.setCellValue((String) value);
                            cell.setCellValue((Date) value);

                            //System.out.println("打印下载的日期格式1："+value);
                        } else if (type == LocalDateTime.class) {
                            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            cell.setCellValue(df.format((LocalDateTime) value));
                           // System.out.println("打印下载的日期格式2："+value);
                        } else if (type == BigDecimal.class ) {//判断类型为数字类型的话，设置单元格为可以计算的格式
                            // System.out.println("打印BigDecimal类型的值：" + value);
                            //builder.setCellStyle(cellStyle);
                            DataFormat dataFormat = wb.createDataFormat();
                            // 创建一个数据格式对象，设置数据格式为"#,0.00"，用于格式化数字
                            CellStyle cellStyle = wb.createCellStyle();
                            // cellStyle.setDataFormat(dataFormat.getFormat("#,#0"));

                            // cellStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));//保留两位小数点
                            // 设置单元格样式为数字格式
                            //  cell.setCellStyle(cellStyle);
                            // cell.setCellValue(((BigDecimal) value).doubleValue());

                            // 如果是BigDecimal,已数字格式输出, 并且设置单元格为可以计算的格式
                            cell.setCellValue(((BigDecimal) value).doubleValue());
                            cellStyle.setDataFormat(wb.createDataFormat().getFormat("0"));
                        }else if (type == Double.class ) {//判断类型为数字类型的话，设置单元格为可以计算的格式
                            // System.out.println("打印Double类型的值：" + value);
                            //builder.setCellStyle(cellStyle);
                            DataFormat dataFormat = wb.createDataFormat();
                            // 创建一个数据格式对象，设置数据格式为"#,0.00"，用于格式化数字
                            CellStyle cellStyle = wb.createCellStyle();
                            // cellStyle.setDataFormat(dataFormat.getFormat("#,##0.0000"));//保留四位小数点
                            // 如果是Double,已数字格式输出, 并且设置单元格为可以计算的格式
                            cell.setCellValue(((Double) value));
                            cellStyle.setDataFormat(wb.createDataFormat().getFormat("0"));
                        }  else {
                            cell.setCellValue(value.toString());
                        }
                    }
                });
            }
        }

        wb.getSheet("Sheet1").createFreezePane(0, 1, 0, 1);
        //下载的文件名称命名为fileName,方法新增传入文件名称，在具体调用的实现类中定义传入
        buildExcelDocument(fileName +  ".xlsx", wb, response);
    }



    public static void buildExcelDocument(String fileName, Workbook wb, HttpServletResponse response) {
        try {
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
            response.flushBuffer();
            ServletOutputStream outputStream = response.getOutputStream();
            wb.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void buildExcelFile(String path, Workbook wb) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        try {
            wb.write(new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
