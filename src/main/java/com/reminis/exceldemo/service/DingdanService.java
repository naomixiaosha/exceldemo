
package com.reminis.exceldemo.service;

import com.reminis.exceldemo.entity.CheckCout;
import com.reminis.exceldemo.entity.Yeji;
import com.reminis.exceldemo.entity.dingdan;
import com.reminis.exceldemo.entity.dingdan_download;
import com.reminis.exceldemo.mapper.DingdanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DingdanService {
    @Autowired
    private DingdanMapper dingdanMapper;

 /**
 * 获取所有dingdan对象
 * 此方法通过调用dingdanMapper的getAll方法来获取数据库中所有的dingdan对象，返回一个包含所有dingdan对象的列表
 * @return 包含所有dingdan对象的列表
 */
public List<dingdan> getLimitAll(int pageInteger,int limitInteger) {
    //pageInteger:代表我们现在在第几页上
    //limitInteger：每页的个数
    int pageIndex = (pageInteger-1) * limitInteger;
    int pageSize = limitInteger;
    return dingdanMapper.getLimitAll(pageIndex,pageSize);
}
/**
 * 获取所有dingdan对象，在后台下载Excel时调用
 * 此方法通过调用dingdanMapper的getAll方法来获取数据库中所有的dingdan对象，返回一个包含所有dingdan对象的列表
 * @return 包含所有dingdan对象的列表
 */
public List<dingdan> getAll() {
    return dingdanMapper.getAll();
}

    /**
     * 保存dingdan对象
     * 此方法通过调用dingdanfMapper的save方法来保存dingdan对象，返回保存成功的状态
     * @param dingdan 需要保存的dingdan对象
     * @return 保存是否成功
     */
    public boolean save(dingdan dingdan) {
        return dingdanMapper.save(dingdan) > 0;
    }


    public int getCount(){
        return dingdanMapper.getCount();
    };


    /**
     * 清空订单表
     * 此方法用于清空订单表中的所有数据该操作应谨慎使用，因为它会删除表中的所有记录
     * 通常在需要重置订单系统或进行系统测试时会使用此方法
     */
    public void truncate() {
        dingdanMapper.truncate();
    }

    /**
     * 模糊查询，订单追踪表
     * @return
     */
    public List<dingdan> getLikeList(String like,int page,int limit){
        return dingdanMapper.getLikeList(like,page,limit);
    };


    public int getLikeCount(String like){
        return dingdanMapper.getLikeCount(like);
    };
    /**
     * 新增联合查询，fromqf中的材料名称(WW-)，中类，大类，三表联合查询，插入dingdan追踪表
     * @return
     */
    public List<dingdan> getThreeTableAll(){
        return dingdanMapper.getThreeTableAll();
    }

    /**
     * 模糊查询，周业绩表，导出模糊查询数据写入Excel
     * @return
     */
    public List<dingdan_download> getLikeSelectDingdan(){
        return dingdanMapper.getLikeSelectDingdan();
    };

    /**
     * 新增，订单追踪表根据下单日期，按中类分组查询下单数量及金额，同步更新数据至订周业绩表
     * @return
     */
    public List<dingdan> getDateSelect(String start,String end){
        return dingdanMapper.getDateSelect(start,end);
    }
    /**
     * 新增，订单追踪表根据下单日期，按中类分组查询下单数量及金额，同步更新数据至订周业绩表
     * @return
     */
    public List<dingdan> UpdateJiaoqiDate(String start, String end){
        return dingdanMapper.UpdateJiaoqiDate(start,end);
    }

    public List<dingdan> getxiadanDateSelec(String start,String end,int pageInteger,int limitInteger){
        int pageIndex =  (pageInteger-1)* limitInteger;
        int pageSize = limitInteger;
        return dingdanMapper.getxiadanDateSelec(start,end,pageIndex,pageSize);
    }

    public int getxiadanCount(String start,String end){
        return dingdanMapper.getxiadanCount(start,end);
    }

    public String check(String zhonglei) {
        int daleizhonglei = dingdanMapper.checkDaleiZhonglei(zhonglei);

        int pingzhongZhonglei = dingdanMapper.checkPingzhongZhonglei(zhonglei);

        int yeji = dingdanMapper.checkZhouYeJi(zhonglei);
        // 如果在三表中都没有该中类存在，则返回验证不通过 判断 pingzhongZhonglei 是否大于0
        if (!(pingzhongZhonglei > 0)){
            return "品种中类关系表中，Excel中的'"+zhonglei+"'中类不存在！";
        }
        if (!(daleizhonglei > 0)){
            return "大类中类关系表中，Excel中的'"+zhonglei+"'中类不存在！";
        }
        if (!(yeji > 0)){
            return "周业绩表中，Excel中的'"+zhonglei+"'中类不存在！";
        }
        // 在三表中都有该中类存在，则返回验证通过
        return "验证通过";
    }

   /* 备份
   public String checkCount() {
        List<CheckCout> checkCouts = dingdanMapper.checkCount();
        if (!checkCouts.isEmpty()) {//不为空，则存在多条数据
            StringBuilder stringBuilder = new StringBuilder();//定义一个字符串，用于拼接一条或多条没有匹配到品种信息的记录
            for (CheckCout checkCout : checkCouts) {
                    return "材料编码为：'"+checkCout.get原编码()+"'在fromqf表只有"+checkCout.get原数量()+"条数据，导入为追踪表格式时匹配到了多个品种，有"+checkCout.get新数量()+"条数据存在";
            }
        }
        return "校验通过";
    }*/
   public String checkCount() {
       List<CheckCout> checkCouts = dingdanMapper.checkCount();
       if (!checkCouts.isEmpty()) {//不为空，则存在多条数据
           StringBuilder yuanbianma = new StringBuilder();//定义一个字符串，用于拼接一条或多条没有匹配到品种信息的记录
           StringBuilder yuanshuliang =new StringBuilder();
           StringBuilder pingzhong = new StringBuilder();//定义一个字符串，用于拼接一条或多条没有匹配到品种信息的记录
           StringBuilder xinshuliang = new StringBuilder();
           StringBuilder baozhuang = new StringBuilder();
           HashMap<HashSet<String>, String> stringStringHashMap = new HashMap<>();
           HashSet<String> set = new HashSet<String>();
                for (CheckCout checkCout : checkCouts) {
                    set.add(checkCout.get原编码());

                        if(pingzhong.indexOf(checkCout.get品种())<0) {
                            pingzhong.append(  checkCout.get品种() + ",");
                        }
                        if(yuanbianma.indexOf(checkCout.get原编码())<0) {
                            yuanbianma.append(checkCout.get原编码() + ",");
                            yuanshuliang.append(checkCout.get原数量()+ ",");
                            xinshuliang.append(checkCout.get新数量()+ ",");
                        }
                }
           String yuanbianmaStr = yuanbianma.substring(0, yuanbianma.length() - 1);
           String yuanshuliangStr = yuanshuliang.substring(0, yuanshuliang.length() - 1);
           String pingzhongStr = pingzhong.substring(0, pingzhong.length() - 1);
           String xinshuliangStr = xinshuliang.substring(0, xinshuliang.length() - 1);
           String s1=  "材料编码为："+yuanbianmaStr+"在fromqf表只有"+yuanshuliangStr+"条数据，导入为追踪表格式时生成了"+xinshuliangStr+"条数据，匹配到了" + pingzhongStr+"多个品种";

        //  System.out.println("测试打印s1："+s1);
           return s1;
       }
       return "校验通过";
   }
}

