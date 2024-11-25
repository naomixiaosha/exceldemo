
package com.reminis.exceldemo.service;

import com.reminis.exceldemo.entity.DaleiZhonglei;
import com.reminis.exceldemo.entity.PingzhongZhonglei;
import com.reminis.exceldemo.entity.Yeji;
import com.reminis.exceldemo.entity.Yeji_download;
import com.reminis.exceldemo.mapper.YejiMapper;
import com.reminis.exceldemo.mapper.PingzhongAndZhongleiMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PingzhongAndZhongleiService {
    @Autowired
    private PingzhongAndZhongleiMapper PingzhongAndZhongleiMapper;

 /**
 * 获取所有dingdan对象
 * 此方法通过调用dingdanMapper的getAll方法来获取数据库中所有的dingdan对象，返回一个包含所有dingdan对象的列表
 * @return 包含所有dingdan对象的列表
 */
public List<PingzhongZhonglei> getLimitAll(int pageInteger,int limitInteger) {
    //pageInteger:代表我们现在在第几页上
    //limitInteger：每页的个数
    int pageIndex = (pageInteger-1) * limitInteger;
    int pageSize = limitInteger;
    return PingzhongAndZhongleiMapper.getLimitAll(pageIndex,pageSize);
}




    public boolean addPingzhongZhonglei(PingzhongZhonglei pingzhongZhonglei) {
        return PingzhongAndZhongleiMapper.addPingzhongZhonglei(pingzhongZhonglei) > 0;
    }

    public int getCount(){
        return PingzhongAndZhongleiMapper.getCount();
    };


    /**
     * 模糊查询，业绩表
     * @return
     */
    public List<PingzhongZhonglei> getLikeList(String like, int page, int limit){
        page= (page-1)*limit;
        return PingzhongAndZhongleiMapper.getLikeList(like,page,limit);
    };

    /**
     * 模糊查询，订单追踪表，导出Excel
     * @return
     */
    public int getLikeCount(String like){
        return PingzhongAndZhongleiMapper.getLikeCount(like);
    };






    //编辑
    public boolean editPingzhong(PingzhongZhonglei PingzhongZhonglei){
        return PingzhongAndZhongleiMapper.editPingzhong(PingzhongZhonglei);
    };
    //2024年11月20日11:03:56 新增，只编辑中类
    public boolean editZhonglei(PingzhongZhonglei PingzhongZhonglei){
        return PingzhongAndZhongleiMapper.editZhonglei(PingzhongZhonglei);
    };

    //品种与中类管理界面新增时，查询是否有重复品种数据
    public String verify(String pingzhong) {


        //2024年11月20日10:17:12 新增输出品种模糊查询校验，新增的品种能在数据库中查出数据，则返回前端存在相似查询品种，请修改

        if (PingzhongAndZhongleiMapper.verifypingzhong(pingzhong) > 0){
           // return "品种中类关系表中，输入的品种已存在";
            return "输入的品种已存在";
        }else if (PingzhongAndZhongleiMapper.verifyLikepingzhong(pingzhong)>0){
            return "输入的品种'"+pingzhong+"'能在品种中类关系表中查出多条中类数据，请修改";
        }

        // 没有重复数据，返回"ok"
        return "验证通过";
    }

    public List<PingzhongZhonglei> getAll() {
        return PingzhongAndZhongleiMapper.getAll();
    }


    public boolean Pingzhongdelete(String zhongleiname){
        return PingzhongAndZhongleiMapper.Pingzhongdelete(zhongleiname);
    };


}

