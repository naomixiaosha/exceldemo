package com.reminis.exceldemo.web;

import com.alibaba.fastjson.JSON;
import com.reminis.exceldemo.entity.Yeji;
import com.reminis.exceldemo.entity.dingdan;
import com.reminis.exceldemo.entity.dingdan_download;
import com.reminis.exceldemo.entity.fromqf;
import com.reminis.exceldemo.service.DingdanService;
import com.reminis.exceldemo.service.FromqfService;
import com.reminis.exceldemo.service.YejiService;
import com.reminis.exceldemo.util.ExcelUtils;
import com.reminis.exceldemo.util.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/dingdan")
public class DingdanController {

    private static final Logger log = LogManager.getLogger(DingdanController.class);

    @Autowired
    private DingdanService DingdanService;
    @Autowired
    private YejiService YejiService;
    @Autowired
    private FromqfService FromqfService;



    //下载追踪表Excel前置校验数据，逻辑：前端点击导出符合追踪格式时，清除追踪表数据，再同步数据，同步完成后对比fromqf与追踪表数据，返回校验结果
    @RequestMapping("/check")
    @ResponseBody
    public String ExcelDownloadCheck(){
        String verify = "";
        //fromqf页面点击导出符合追踪表格式时，先执行联合3表把fromqf数据同步到订单追踪表中
        //点击导出符合追踪格式前，清除旧表数据，再同步数据
        DingdanService.truncate();
        //联合查询fromqf表,品种中类关系表，大类中类关系表，同步到订单追踪表中
        List<dingdan> threeTableAll = DingdanService.getThreeTableAll();
        for (dingdan dingdan : threeTableAll) {//2024年11月19日09:57:37，同步数据时候，同一种编码'WW-NE950-SST-21'匹配到了3个品种，因此需要做判断，如果匹配到3个，则只取第一个，其他两个数据不保存，只做记录，方便后续排查问题
            ////system.out.println("插入当前dingdan对象为："+dingdan.toString());
            // dingdan.set总价();
            DingdanService.save(dingdan);
        }
        //获取fromqf数据总量
        int fromqfcount = FromqfService.getCount();
        //获取追踪表同步结束后的数据总量
        int dindancount = DingdanService.getCount();
        // 从订单追踪表中获取所有数据，与fromqf表进行比对,如果数量不一致，则返回失败原因
        //这里匹配加个校验哦，查出来多条提示导出失败有多条存在
        //或没查到就提示导出失败，品种中类缺少匹配项

        //新增校验两表数据，根据编码，及编码分组数量，join查询 ，查询条件 on i.原编码 = j.编码  where i.原数量 <> j.新数量;
        String checkCount = DingdanService.checkCount();

        return checkCount;
    }
    /**
     * 订单追踪Excel导出
     * @param response
     */
    @GetMapping("/exportExcel")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response){

      /*  //点击导出符合追踪格式前，清除旧表数据，再同步数据
        DingdanService.truncate();
        //联合查询fromqf表,品种中类关系表，大类中类关系表，同步到订单追踪表中
        List<dingdan> threeTableAll = DingdanService.getThreeTableAll();
        for (dingdan dingdan : threeTableAll) {//2024年11月19日09:57:37，同步数据时候，同一种编码'WW-NE950-SST-21'匹配到了3个品种，因此需要做判断，如果匹配到3个，则只取第一个，其他两个数据不保存，只做记录，方便后续排查问题
            ////system.out.println("插入当前dingdan对象为："+dingdan.toString());
            // dingdan.set总价();
            DingdanService.save(dingdan);
        }*/

      //  System.out.println("进入追踪表导出Excel");
        List<dingdan_download> dingdanList = new ArrayList<>();
        dingdanList = DingdanService.getLikeSelectDingdan();
        String fileName = "";
        for(dingdan_download dingdan_download : dingdanList){
            fileName = dingdan_download.get订单号();
        }

        ////system.out.println("dingdanList"+dingdanList.toString());
        //注意，实体类ExcelColumn注解的col为写入Excel的顺序，如注解不写col则不会显示在Excel中
      //  ExcelUtils.writeExcel(request,response, dingdanList, dingdan.class);
        //导出符合追踪数据：追踪表_订单号_202411131530.xlsx
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");//导出文件时间格式，格式到：年月日小时分钟
        String format = simpleDateFormat.format(date);
        fileName= "追踪表"+"_"+fileName+"_"+format;
       // System.out.println("导出文件名："+fileName);
        ExcelUtils.writeExcel(request, response,fileName, dingdanList, dingdan_download.class);
       // ExcelUtils.writeExcel(request,response, fromqfList, fromqf.class);
    }


    /**
     * Excel导入
     * @param file
     */
    @PostMapping("/readExcel")
    @ResponseBody
    public HashMap<String,String>  readExcel(@RequestBody MultipartFile file){
        int flag = 0;//0表示失败，1表示成功
        long t1 = System.currentTimeMillis();
        log.info("上传的文件："+file);

        //新增返回前端提示信息，返回前端提示code=1，表示成功，code=0表示失败
        HashMap<String,String> hashMap = new HashMap<>();

        //解析Excel数据
        List<dingdan> list = ExcelUtils.readExcel("", dingdan.class, file);
        //2024年11月18日15:22:02 新增上传追踪表数据Excel时，校验Excel中类是否存在,否则返回前端提示，并返回前端，提示具体哪个表中类不存在
        for (dingdan dingdanlist : list){
            String check = DingdanService.check(dingdanlist.get中类());
         //   System.out.println("打印追踪表上传Excel验证结果："+check);
            if (check.equals("验证通过")){
                ////system.out.println("验证通过");
                flag = 1;
            }else {
                ////system.out.println("验证不通过");
                ////system.out.println("验证不通过，返回前端提示："+check);
                // 验证不通过，返回前端提示
                hashMap.put("code","0");
                hashMap.put("message",check);
                return hashMap;
                 }
            }

        //日期格式正则表达式，用于匹配日期格式，如：2024/11/09
        String key ="\\d{4}/\\d{2}/\\d{2}";

        // 遍历list，正则匹配日期格式
        for (dingdan dingdan : list) {
                //System.out.println("打印当前dingdan对象日期："+dingdan.get日期());
                boolean matches = dingdan.get日期().matches(key);
                if (matches){
                    flag =1;
                }else {
                    flag = 0;//日期格式不正确,返回前端具体错误日期参数
                    System.out.println("日期格式不正确，请检查Excel数据:"+dingdan.get日期());
                    hashMap.put("code","0");
                    hashMap.put("message","日期格式不正确，请检查Excel中日期列数据:"+dingdan.get日期());
                    return hashMap;
                }
        }

        //上传Excel之前清空数据库
        DingdanService.truncate();


        if (flag == 1){//flag==1，表示上传Excel的日期格式正确，验证通过，导入数据
            // 验证通过，导入数据
            for (dingdan dingdan : list) {
                ////system.out.println("遍历list,插入当前对象为：" + dingdan.get日期());
                DingdanService.save(dingdan);
            }

            hashMap.put("code","1");
            hashMap.put("message","上传成功");
        }

        long t2 = System.currentTimeMillis();
        //////system.out.println(String.format("保存结束! cost:%sms", (t2 - t1)));
        ////system.out.println(String.format("保存订单追踪表结束!耗时:%ss", (t2 - t1)/1000));


        return hashMap;
    }
    /**
     * 处理dingdan仓库数据并返回一个包含特定数据对象的列表
     * 这个方法主要用于模拟从仓库中获取数据并封装到dingdan对象列表中
     */
    public List<dingdan> DingdanHandleRepositoryData() {
        // 创建一个dingdanList对象列表，用于存储仓库数据
        List<dingdan> dingdanList = new ArrayList<>();

        dingdanList = DingdanService.getAll();
        for (int i = 0; i <dingdanList.size() ; i++) {
          //  ////system.out.println("打印循环参数dingdanList:"+dingdanList.get(i).toString());
        }
        // 返回填充好的fromqf对象列表
        return dingdanList;
    }


    /**
     * 订单追踪页面分页查询
     * @return
     */
    @GetMapping("/getList")
    public Map<String,List<dingdan>> getList(int page,int limit){
        //获取数据库中的查询数据
        List<dingdan> dingdanfList = DingdanService.getLimitAll(page,limit);
        Map<String,List<dingdan>> map = new HashMap<String, List<dingdan>>();
        map.put("data",dingdanfList);
        return map;
    }


    /**
     * 订单追踪页面模糊分页查询
     * @return
     */
    @GetMapping("/getLikeList")
    public Map<String,List<dingdan>> getLikeList(String like,int page,int limit){
        //获取数据库中的查询数据
        //system.out.println("打印模糊查询参数："+like);
        // page-1，因为layui的page从1开始，数据库从0开始,所以page-1用于前端分页
        List<dingdan> dingdanfList = DingdanService.getLikeList(like,page-1,limit);
        //system.out.println("结束模糊查询打印："+dingdanfList.toString());
        Map<String,List<dingdan>> map = new HashMap<String, List<dingdan>>();
        map.put("data",dingdanfList);
        return map;
    }

    /**
     * 根据日期生成下单周期数据，update到周业绩表
     * @return
     */
    @GetMapping("/getDateSelect")
    public Map<String,List<dingdan>> getDateSelect(String start,String end){
        //获取数据库中的查询数据
        //system.out.println("打印查询参数："+start+","+end);
        //清除周业绩表旧数据
        YejiService.truncate();
        // 将前端传递的日期转换为yyyy/MM/dd格式
        start = start.replace("-","/");
        end = end.replace("-","/");
        // 调用更新方法,根据下单日期生成周业绩表下单数量，下单金额
        List<dingdan> dingdanfList=  DingdanService.getDateSelect(start, end);

        // 调用更新方法,根据交期生成周业绩表发货数量，发货金额
        DingdanService.UpdateJiaoqiDate(start, end);
        Map<String,List<dingdan>> map = new HashMap<String, List<dingdan>>();
        map.put("data",dingdanfList);
        return map;
    }
    //下单日期查询
    @GetMapping("/getxiadanDateSelect")
    public Map<String,List<dingdan>> getxiadanDateSelec(String start,String end,int page,int limit){

        //清除周期表旧数据
       // YejiService.truncate();
        // 将前端传递的日期转换为yyyy/MM/dd格式
        start = start.replace("-","/");
        end = end.replace("-","/");
        // 根据下单日期查询范围数据
       // System.out.println("打印下单日期查询参数："+start+","+end+","+page+","+limit);
        List<dingdan> dingdanfList=  DingdanService.getxiadanDateSelec(start, end,page,limit);

        // 调用更新方法,根据交期生成周业绩表发货数量，发货金额
       // DingdanService.UpdateJiaoqiDate(start, end);
        Map<String,List<dingdan>> map = new HashMap<String, List<dingdan>>();
        map.put("data",dingdanfList);
        return map;
    }

    @RequestMapping("/getxiadanCount")
    @ResponseBody
    public String getxiadanCount(String start,String end){
        //system.out.println("模糊查询总数参数："+like);
        //日期类型转换，将前端传递的日期格式yyyy-MM-dd转换为yyyy/MM/dd格式
        start = start.replace("-","/");
        end = end.replace("-","/");
      //  system.out.println("订单追踪页，下单日期查询记录总行数，打印查询参数：："+start+","+end);
        int count = DingdanService.getxiadanCount(start,end);
       // system.out.println("订单追踪页,日期查询记录总行数结果："+count);
        String data = "{\"count\":"+count+"}";
        return data;
    }
    /**
     * 模糊查询总数，用于layui 分页
     * @return
     */
    @RequestMapping("/getLikeCount")
    @ResponseBody
    public String getLikeCount(String like){
      //  ////system.out.println("模糊查询总数参数："+like);
        int count = DingdanService.getLikeCount(like);
       // ////system.out.println("模糊查询总数结果："+count);
        String data = "{\"count\":"+count+"}";
        return data;
    }
    /**
     * 订单追踪页面查询数据总数，用于layui 分页
     * @return
     */
    @RequestMapping("/getCount")
    @ResponseBody
    public String getCount(){
        int count = DingdanService.getCount();
        String data = "{\"count\":"+count+"}";
        return data;
    }
}
