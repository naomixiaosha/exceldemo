package com.reminis.exceldemo.web;

import com.reminis.exceldemo.entity.PingzhongZhonglei;
import com.reminis.exceldemo.service.PingzhongAndZhongleiService;
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
@RequestMapping("/api/pingzhong")
public class PingzhongAndZhongleiController {

    private static final Logger log = LogManager.getLogger(PingzhongAndZhongleiController.class);

    @Autowired
    private PingzhongAndZhongleiService PingzhongAndZhongleiService;




    /**
     * 处理dingdan仓库数据并返回一个包含特定数据对象的列表
     * 这个方法主要用于模拟从仓库中获取数据并封装到dingdan对象列表中
     */
    public List<PingzhongZhonglei> DingdanHandleRepositoryData() {
        // 创建一个dingdanList对象列表，用于存储仓库数据
        List<PingzhongZhonglei> pingzhongZhongleiList = new ArrayList<>();

        pingzhongZhongleiList = PingzhongAndZhongleiService.getAll();
        for (int i = 0; i <pingzhongZhongleiList.size() ; i++) {
          //  System.out.println("打印循环参数yejiList:"+yejiList.get(i).toString());
        }
        // 返回填充好的fromqf对象列表
        return pingzhongZhongleiList;
    }


    /**
     * 品种与中类管理页面分页查询
     * @return
     */
    @GetMapping("/getList")
    public Map<String,List<PingzhongZhonglei>> getList(int page,int limit){
        //获取数据库中的查询数据
        List<PingzhongZhonglei> pingzhongZhongleiList = PingzhongAndZhongleiService.getLimitAll(page,limit);
        Map<String,List<PingzhongZhonglei>> map = new HashMap<String, List<PingzhongZhonglei>>();
        map.put("data",pingzhongZhongleiList);
        return map;
    }


    /**
     * 周业绩表页面 like 模糊分页查询
     * @return
     */
    @GetMapping("/getLikeList")
    public Map<String,List<PingzhongZhonglei>> getLikeList(String like,int page,int limit){
        //获取数据库中的查询数据
      //  System.out.println("打印yejiList模糊查询参数："+like);
        // page-1，因为layui的page从1开始，数据库从0开始,所以page-1用于前端分页
        List<PingzhongZhonglei> pingzhongZhongleiList = PingzhongAndZhongleiService.getLikeList(like.trim(),page,limit);
      //  System.out.println("结束yejiList模糊查询打印："+yejiList.toString());
        Map<String,List<PingzhongZhonglei>> map = new HashMap<String, List<PingzhongZhonglei>>();
        map.put("data",pingzhongZhongleiList);
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
        int count = PingzhongAndZhongleiService.getLikeCount(like);
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
        int count = PingzhongAndZhongleiService.getCount();
        String data = "{\"count\":"+count+"}";
        return data;
    }


    @RequestMapping("/addPingzhong")
    @ResponseBody
    public String addPingzhong(String pingzhong,String zhonglei){
        //判断品种
        String verify = PingzhongAndZhongleiService.verify(pingzhong.trim());//查询是否有重复，去除空格空格，防止空格影响查询
        if(verify.equals("验证通过")){
            PingzhongZhonglei pingzhongZhonglei = new PingzhongZhonglei();
            pingzhongZhonglei.set中类(zhonglei);
            pingzhongZhonglei.set品种(pingzhong);
            //向品种中类关系表中添加数据
            boolean addPingzhongZhonglei = PingzhongAndZhongleiService.addPingzhongZhonglei(pingzhongZhonglei);
            if ( addPingzhongZhonglei) {
                return "添加成功";
            }
        }
            return verify;
    }


    /**
     * 品种与中类编辑界面
     * @param zhongleiname
     * @return
     */
    @RequestMapping("/editPingzhong")
    @ResponseBody
    public String editPingzhong(HttpServletRequest request) {
        PingzhongZhonglei pingzhongZhonglei = new PingzhongZhonglei();
        int weiyihaoma = Integer.parseInt(request.getParameter("weiyihaoma"));
        pingzhongZhonglei.set唯一号码(weiyihaoma);
        String pingzhong = request.getParameter("pingzhong").trim();//查询是否有重复，去除空格空格，防止空格影响查询
        pingzhongZhonglei.set品种(pingzhong);
        pingzhongZhonglei.set中类(request.getParameter("zhonglei"));
    //    System.out.println("打印编辑参数："+request.getParameter("fahuoshuliang"));
//判断品种
        String verify = PingzhongAndZhongleiService.verify(pingzhong);//查询品种与中类表是否有重复品种数据，不能修改已存在中类
        if(verify.equals("验证通过")){
        boolean flag = PingzhongAndZhongleiService.editPingzhong(pingzhongZhonglei);
            if ( flag) {
                return "ok";
            }
        }
        return verify;
    }

    /**
     *
     * @param weiyihaoma
     * @return
     */
    @RequestMapping("/editZhonglei")
    @ResponseBody
    public String editZhonglei(HttpServletRequest request) {//根据唯一编号改掉中类
        PingzhongZhonglei pingzhongZhonglei = new PingzhongZhonglei();
        int weiyihaoma = Integer.parseInt(request.getParameter("weiyihaoma"));
        pingzhongZhonglei.set唯一号码(weiyihaoma);
        pingzhongZhonglei.set中类(request.getParameter("zhonglei"));
        //    System.out.println("打印编辑参数："+request.getParameter("fahuoshuliang"));
//判断品种
        boolean b = PingzhongAndZhongleiService.editZhonglei(pingzhongZhonglei);
        if ( b){

            return "ok";
        }
        return  "仅修改中类失败，请重试";

    }



    @RequestMapping("/Pingzhongdelete")
    @ResponseBody
    public String Pingzhongdelete(String weiyihaoma) {
        //   System.out.println("打印删除参数：" + weiyihaoma);

        boolean deleted = PingzhongAndZhongleiService.Pingzhongdelete(weiyihaoma);
        //   System.out.println("打印s删除结果："+deleted);
        if (deleted) {
            return "ok";
        }
        return "error";
    }
}
