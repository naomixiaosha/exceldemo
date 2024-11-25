package com.reminis.exceldemo.web;

import com.reminis.exceldemo.entity.DaleiZhonglei;
import com.reminis.exceldemo.entity.PingzhongZhonglei;
import com.reminis.exceldemo.service.DaleiAndZhongleiService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/daleiandzhonglei")
public class DaleiAndZhongleiController {

    private static final Logger log = LogManager.getLogger(DaleiAndZhongleiController.class);

    @Autowired
    private DaleiAndZhongleiService daleiAndZhongleiService;


    /**
     * 处理dingdan仓库数据并返回一个包含特定数据对象的列表
     * 这个方法主要用于模拟从仓库中获取数据并封装到dingdan对象列表中
     */
    public List<DaleiZhonglei> DingdanHandleRepositoryData() {
        // 创建一个dingdanList对象列表，用于存储仓库数据
        List<DaleiZhonglei> daleiZhongleis = new ArrayList<>();

        daleiZhongleis = daleiAndZhongleiService.getAll();
        for (int i = 0; i <daleiZhongleis.size() ; i++) {
          //  System.out.println("打印循环参数yejiList:"+yejiList.get(i).toString());
        }
        // 返回填充好的fromqf对象列表
        return daleiZhongleis;
    }


    /**
     * 品种与中类管理页面分页查询
     * @return
     */
    @GetMapping("/getList")
    public Map<String,List<DaleiZhonglei>> getList(int page,int limit){
        //获取数据库中的查询数据
        List<DaleiZhonglei> daleiZhongleis = daleiAndZhongleiService.getLimitAll(page,limit);
        Map<String,List<DaleiZhonglei>> map = new HashMap<String, List<DaleiZhonglei>>();
        map.put("data",daleiZhongleis);
        return map;
    }


    /**
     * 周业绩表页面 like 模糊分页查询
     * @return
     */
    @GetMapping("/getLikeList")
    public Map<String,List<DaleiZhonglei>> getLikeList(String like,int page,int limit){
        //获取数据库中的查询数据
      //  System.out.println("打印yejiList模糊查询参数："+like);
        // page-1，因为layui的page从1开始，数据库从0开始,所以page-1用于前端分页
        List<DaleiZhonglei> daleiZhongleis = daleiAndZhongleiService.getLikeList(like,page,limit);
      //  System.out.println("结束yejiList模糊查询打印："+yejiList.toString());
        Map<String,List<DaleiZhonglei>> map = new HashMap<String, List<DaleiZhonglei>>();
        map.put("data",daleiZhongleis);
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
        int count = daleiAndZhongleiService.getLikeCount(like);
       // System.out.println("模糊查询总数结果："+count);
        String data = "{\"count\":"+count+"}";
        return data;
    }
    /**
     * 订单追踪页面查询数据总数，用于layui 分页,首次进入该页面默认渲染所有数据
     * @return
     */
    @RequestMapping("/getCount")
    @ResponseBody
    public String getCount(){
        int count = daleiAndZhongleiService.getCount();
        String data = "{\"count\":"+count+"}";
        return data;
    }


    @RequestMapping("/addDaleiZhonglei")
    @ResponseBody
    public String addPingzhong(String dalei,String zhonglei){
        //判断品种，新增时判断大类中类表中是否存在重复的中类，如果存在返回'中类已存在'，否则添加
        zhonglei = zhonglei.trim();//查询是否有重复，去除空格空格，防止空格影响查询
        String verify = daleiAndZhongleiService.verify(zhonglei);
        if(verify.equals("验证通过")){
            DaleiZhonglei daleiZhonglei = new DaleiZhonglei();
            daleiZhonglei.set大类(dalei);
            daleiZhonglei.set中类(zhonglei);
            //向品种中类关系表中添加数据
            boolean addDaleiZhonglei = daleiAndZhongleiService.addDaleiZhonglei(daleiZhonglei);
            if ( addDaleiZhonglei) {
                return "添加成功";
            }
        }
            return verify;
    }


    /**
     * 大类与中类编辑界面
     * @param zhongleiname
     * @return
     */
    @RequestMapping("/editdaleizhonglei")
    @ResponseBody
    public String editdaleizhonglei(HttpServletRequest request) {
        DaleiZhonglei daleiZhonglei = new DaleiZhonglei();
        int weiyihaoma = Integer.parseInt(request.getParameter("weiyihaoma"));
        daleiZhonglei.set唯一号码(weiyihaoma);
        daleiZhonglei.set大类(request.getParameter("dalei"));
        String zhonglei = request.getParameter("zhonglei").trim();//查询是否有重复，去除空格空格，防止空格影响查询
        daleiZhonglei.set中类(zhonglei);
    //    System.out.println("打印编辑参数："+request.getParameter("fahuoshuliang"));
        String verify = daleiAndZhongleiService.verify(zhonglei);//查询是否有重复中类
        if(verify.equals("验证通过")){

            boolean flag = daleiAndZhongleiService.editdaleizhonglei(daleiZhonglei);
            if ( flag) {
                return "ok";//修改成功，返回ok
            }
        }
        return verify;//验证表中存在重复中类，返回'输入的中类已存在'
    }


    @RequestMapping("/deleteDaleiZhonglei")
    @ResponseBody
    public String deleteDaleiZhonglei(String weiyihaoma) {
        //   System.out.println("打印删除参数：" + weiyihaoma);

        boolean deleted = daleiAndZhongleiService.deleteDaleiZhonglei(weiyihaoma);
        //   System.out.println("打印s删除结果："+deleted);
        if (deleted) {
            return "ok";
        }
        return "error";
    }
}
