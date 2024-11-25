package com.reminis.exceldemo.web;

import com.reminis.exceldemo.entity.PingzhongZhonglei;
import com.reminis.exceldemo.entity.Yeji;
import com.reminis.exceldemo.entity.DaleiZhonglei;
import com.reminis.exceldemo.entity.Yeji_download;
import com.reminis.exceldemo.service.YejiService;
import com.reminis.exceldemo.util.ExcelUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/yeji")
public class ZhouyejiController {

    private static final Logger log = LogManager.getLogger(ZhouyejiController.class);

    @Autowired
    private YejiService YejiService;
    /**
     * 业绩表Excel导出
     * @param response
     */
    @GetMapping("/exportExcel")
    public void exportExcel(HttpServletRequest request,HttpServletResponse response){
       // System.out.println("业绩表 start");
        String like = request.getParameter("like");
       // System.out.println("打印模糊查询参数："+like);
        // 创建一个dingdanList对象列表，用于存储仓库数据
        List<Yeji_download> yejiList = new ArrayList<>();
        yejiList = YejiService.getLikeSelectDingdan(like);
      //  System.out.println("业绩表List"+yejiList.toString());
        long t1 = System.currentTimeMillis();
        String fileName = "周业绩表";
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(date);
        fileName=fileName+format;
        //注意，实体类ExcelColumn注解的col为写入Excel的顺序，如注解不写col则不会显示在Excel中
        ExcelUtils.writeExcel(request,response, fileName,yejiList, Yeji_download.class);
       // ExcelUtils.writeExcel(request,response, fromqfList, fromqf.class);
        long t2 = System.currentTimeMillis();
       // System.out.println(String.format("write over! cost:%sms", (t2 - t1)));
    }



    /**
     * 处理dingdan仓库数据并返回一个包含特定数据对象的列表
     * 这个方法主要用于模拟从仓库中获取数据并封装到dingdan对象列表中
     */
    public List<Yeji> DingdanHandleRepositoryData() {
        // 创建一个dingdanList对象列表，用于存储仓库数据
        List<Yeji> yejiList = new ArrayList<>();

        yejiList = YejiService.getAll();
        for (int i = 0; i <yejiList.size() ; i++) {
          //  System.out.println("打印循环参数yejiList:"+yejiList.get(i).toString());
        }
        // 返回填充好的fromqf对象列表
        return yejiList;
    }


    /**
     * 周业绩表页面分页查询
     * @return
     */
    @GetMapping("/getList")
    public Map<String,List<Yeji>> getList(int page,int limit){
        //获取数据库中的查询数据
        List<Yeji> yejiList = YejiService.getLimitAll(page,limit);
        Map<String,List<Yeji>> map = new HashMap<String, List<Yeji>>();
        map.put("data",yejiList);
        return map;
    }


    /**
     * 周业绩表页面 like 模糊分页查询
     * @return
     */
    @GetMapping("/getLikeList")
    public Map<String,List<Yeji>> getLikeList(String like,int page,int limit){
        //获取数据库中的查询数据
      //  System.out.println("打印yejiList模糊查询参数："+like);
        // page-1，因为layui的page从1开始，数据库从0开始,所以page-1用于前端分页

        //page =page-1;
        List<Yeji> yejiList = YejiService.getLikeList(like,page,limit);
      //  System.out.println("结束yejiList模糊查询打印："+yejiList.toString());
        Map<String,List<Yeji>> map = new HashMap<String, List<Yeji>>();
        map.put("data",yejiList);
        return map;
    }


    /**
     * 模糊查询总数，用于layui 分页
     * @return
     */
    @RequestMapping("/getLikeCount")
    @ResponseBody
    public String getLikeCount(String like){
      //  System.out.println("模糊查询总数参数："+like);
        int count = YejiService.getLikeCount(like);
       // System.out.println("模糊查询总数结果："+count);
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
        int count = YejiService.getCount();
        String data = "{\"count\":"+count+"}";
        return data;
    }


    @RequestMapping("/addZhouyeji")
    @ResponseBody
    public String addZhouyeji(String zhongleiname,String zhonglei,String danwei){
        //判断中类是否已存在
        //判断中类中文是否已存在
        //判断品种
        zhongleiname = zhongleiname.trim();
        String verify = YejiService.verify(zhongleiname.trim(),zhonglei);//中类中文，品种，去除空格校验，防止空格影响查询
        if(verify.equals("验证通过")){
            Yeji yeji = new Yeji();
            yeji.set中类中文(zhongleiname);
            yeji.set中类(zhonglei);
            yeji.set单位(danwei);
            boolean addyeji = YejiService.addyeji(yeji);
            //    System.out.println("打印添加结果："+add);
            if (addyeji ) {
              //  System.out.println("打印添加OK结果："+addyeji);
                return "添加成功";
            }
        }
        //返回添加失败原因
      //  System.out.println("打印添加失败原因："+verify);
            return verify;
    }

    @RequestMapping("/Yejidelete")
    @ResponseBody
    public String Yejidelete(String weiyihaoma) {
     //   System.out.println("打印删除参数：" + weiyihaoma);

            boolean deleted = YejiService.UpdateZhongleiNameDate(weiyihaoma);
     //   System.out.println("打印s删除结果："+deleted);
        if (deleted) {
            return "ok";
        }
        return "error";
    }

    /**
     * 周业绩编辑界面
     * @param zhongleiname
     * @return
     */
    @RequestMapping("/editZhouyeji")
    @ResponseBody
    public String Yejidelete(HttpServletRequest request) {
      //  System.out.println("打印业绩编辑对象："+request.getParameter("weiyihaoma"));
        Yeji_download yeji = new Yeji_download();
        yeji.set唯一号码(request.getParameter("weiyihaoma"));
        yeji.set中类中文(request.getParameter("zhongleiname"));
        yeji.set中类(request.getParameter("zhonglei"));
        yeji.set单位(request.getParameter("danwei"));

        yeji.set下单数量(Double.parseDouble(request.getParameter("xiadanshuliang")));
        yeji.set下单金额(BigDecimal.valueOf(Double.parseDouble(request.getParameter("xiadanjinge"))));
        yeji.set发货数量(Double.parseDouble(request.getParameter("fahuoshuliang")));
        yeji.set发货金额(BigDecimal.valueOf(Double.parseDouble(request.getParameter("fahuojinge"))));
        boolean flag = YejiService.UpdateZhongleAll(yeji);

        if (flag) {
            return "ok";
        }
        return "error";
    }
}
