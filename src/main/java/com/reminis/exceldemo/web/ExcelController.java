package com.reminis.exceldemo.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.reminis.exceldemo.entity.dingdan;
import com.reminis.exceldemo.entity.fromqf;
import com.reminis.exceldemo.entity.fromqf_download;
import com.reminis.exceldemo.service.DingdanService;
import com.reminis.exceldemo.service.FromqfService;
import com.reminis.exceldemo.util.ExcelUtils;
import com.reminis.exceldemo.util.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.reminis.exceldemo.entity.fromqf;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/test")
public class ExcelController {

    private static final Logger log = LogManager.getLogger(ExcelController.class);

    @Autowired
    private FromqfService FromqfService;
    @Autowired
    private  DingdanService DingdanService;
    /**
     * Excel导出,导出符合erp数据格式
     * @param response
     */
    @GetMapping("/exportExcel")
    public void exportExcel(HttpServletRequest request,HttpServletResponse response){

        List<fromqf_download> fromqf_download = new ArrayList<>();
        fromqf_download = FromqfService.douloadExcel();
        SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd");//把数据库中的日期格式yyyy-MM-dd转换成yyyy/MM/dd格式
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd");
        String fileName1 = "";
        for (fromqf_download fromqf_download1 : fromqf_download) {
            try {
             //   System.out.println("打印当前获取日期:" + fromqf_download1.get日期());
                Date date = fromqf_download1.get日期(); // 假设 get日期() 返回的是 Date 类型
             //   System.out.println("打印当前获取date日期:" + date);
                String dateString = inputFormat.format(date); // 将 Date 转换为 String
              //  System.out.println("打印当前获取dateString日期:" + dateString);
                String format = outputFormat.format(inputFormat.parse(dateString));
               // System.out.println("打印当前获取format日期:" + format);
                fromqf_download1.set日期(outputFormat.parse(format)); // 将字符串转换回 Date 类型
                fileName1 = fromqf_download1.get订单号(); // 获取订单号，用于文件名拼接
                fromqf_download1.set客户代码("C251"); // 客户代码，目前没有数据，暂时赋值为CS251,测试输出效果
                fromqf_download1.set预交日期(outputFormat.parse(format)); // 设置日期为交易日期，目前没有数据，暂时赋值为订单日期（及下单日期），测试输出效果
                fromqf_download1.set币别("RMB"); // 币种固定赋值为RMB
            } catch (ParseException e) {
                log.error("日期解析错误: " + e.getMessage());
            }
        }
       // long t1 = System.currentTimeMillis();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");//设置文件名后缀需要的时间格式，格式到：年月日小时分钟
        String format = simpleDateFormat.format(date);
       // System.out.println("当前格式时间："+format);
        //fromqf下载Excel的文件名格式：订单号_202411131530.xlsx
        fileName1 = fileName1+"_"+format;
        ExcelUtils.writeExcel(request,response,fileName1, fromqf_download, fromqf_download.class);
        //long t2 = System.currentTimeMillis();
        //system.out.println(String.format("write over! cost:%sms", (t2 - t1)));
    }

    /**
     * Excel导入
     * 2024年11月18日15:14:56 新增导入Excel时，校验中类是否存在数据表，不存在则返回前端提示，并返回前端，提示具体中类不存在
     * @param file
     */
    @PostMapping("/readExcel")
    public int readExcel(@RequestBody MultipartFile file){
        int flag = 0;
        long t1 = System.currentTimeMillis();
        log.info("上传的文件："+file);
        String fileName = file.getOriginalFilename();
        //设置订单号为文件名，切割文件名'.'，拿前面一部分
        // System.out.println("文件名："+fileName);

        fileName = fileName.substring(0,fileName.indexOf("."));//文件名格式：PD24110489.xls PD24110489.xlsx
        //解析Excel数据
        List<fromqf> list = ExcelUtils.readExcel("", fromqf.class, file);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String format = simpleDateFormat.format(new Date());
        // //system.out.println("当前时间："+format);

        //上传Excel之前清空数据库
        FromqfService.truncate();
        // 遍历list，将数据保存到数据库
        //String fromqfjinge ="";
        for (fromqf fromqf : list) {
            //设置订单号为文件名，切割文件名'.'，拿前面一部分

            fromqf.set订单号(fileName);
            fromqf.set日期(format);
            // System.out.println("插入当前fromqf对象为："+fromqf.toString());
            FromqfService.save(fromqf);
        }
        flag = 1;
        // //system.out.println("保存fromqf结束");
        //新增上传结束时，清空原追踪表，把新上传的fromqf数据联合中类，大类，同步数据至追踪表
        //2024年11月19日16:03:59，移除上传fromqf数据同步到追踪表中，改为点击导出符合追踪格式Excel按钮触发
       /* DingdanService.truncate();
        List<dingdan> threeTableAll = DingdanService.getThreeTableAll();
        for (dingdan dingdan : threeTableAll) {//2024年11月19日09:57:37，同步数据时候，同一种编码'WW-NE950-SST-21'匹配到了3个品种，因此需要做判断，如果匹配到3个，则只取第一个，其他两个数据不保存，只做记录，方便后续排查问题

            ////system.out.println("插入当前dingdan对象为："+dingdan.toString());
            // dingdan.set总价();
            DingdanService.save(dingdan);
        }*/
        long t2 = System.currentTimeMillis();
        //system.out.println(String.format("保存fromqf结束!耗时:%ss", (t2 - t1)/1000));
        //上传成功时，返回前端刷新
        return flag;
    }

    /**
     * 处理仓库数据并返回一个包含特定数据对象的列表
     * 这个方法主要用于模拟从仓库中获取数据并封装到fromqf对象列表中
     * 它循环生成一组数据对象，设置其属性，并将它们添加到列表中
     *
     * @return 包含模拟仓库数据的fromqf对象列表
     */
    public List<fromqf> handleRepositoryData() {
        // 创建一个fromqf对象列表，用于存储仓库数据
        List<fromqf> fromqfList = new ArrayList<>();

            fromqfList = FromqfService.getAll();

        // 返回填充好的fromqf对象列表
        return fromqfList;
    }

    /**
     * 前台页面的数据列表
     * @return
     */
    @GetMapping("/getList")
    public Map<String,List<fromqf>> getList(int page,int limit){
        //获取数据库中的查询数据
        List<fromqf> fromqfList = FromqfService.getLimitAll(page,limit);
        Map<String,List<fromqf>> map = new HashMap<String, List<fromqf>>();
        map.put("data",fromqfList);
        return map;
    }


    /**
     * fromqf页面查询数据总数，用于layui 分页
     * @return
     */
    @RequestMapping("/getCount")
    @ResponseBody
    public String getCount(){
        int count = FromqfService.getCount();
        String data = "{\"count\":"+count+"}";
        return data;
    }
}
