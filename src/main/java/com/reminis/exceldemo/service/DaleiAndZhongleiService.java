
package com.reminis.exceldemo.service;

import com.reminis.exceldemo.entity.DaleiZhonglei;
import com.reminis.exceldemo.mapper.DaleiAndZhongleiMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DaleiAndZhongleiService {
    @Autowired
    private DaleiAndZhongleiMapper DaleiAndZhongleiMapper;

 /**
 * 获取所有dingdan对象
 * 此方法通过调用dingdanMapper的getAll方法来获取数据库中所有的dingdan对象，返回一个包含所有dingdan对象的列表
 * @return 包含所有dingdan对象的列表
 */
public List<DaleiZhonglei> getLimitAll(int pageInteger, int limitInteger) {
    //pageInteger:代表我们现在在第几页上
    //limitInteger：每页的个数
    int pageIndex = (pageInteger-1) * limitInteger;
    int pageSize = limitInteger;
    return DaleiAndZhongleiMapper.getLimitAll(pageIndex,pageSize);
}




    public boolean addDaleiZhonglei(DaleiZhonglei daleiZhonglei) {
        return DaleiAndZhongleiMapper.addDaleiZhonglei(daleiZhonglei) > 0;
    }

    public int getCount(){
        return DaleiAndZhongleiMapper.getCount();
    };


    /**
     * 模糊查询，业绩表
     * @return
     */
    public List<DaleiZhonglei> getLikeList(String like, int page, int limit){
        page= (page-1)*limit;
        return DaleiAndZhongleiMapper.getLikeList(like,page,limit);
    };

    /**
     * 模糊查询，订单追踪表，导出Excel
     * @return
     */
    public int getLikeCount(String like){
        return DaleiAndZhongleiMapper.getLikeCount(like);
    };






    //周业绩编辑
    public boolean editdaleizhonglei(DaleiZhonglei PingzhongZhonglei){
        return DaleiAndZhongleiMapper.editdaleizhonglei(PingzhongZhonglei);
    };

    //品种与中类管理界面新增时，查询是否有重复品种数据
    public String verify(String zhonglei) {

        int verifyDaleiZhonglei = DaleiAndZhongleiMapper.verifyDaleiZhonglei(zhonglei);
        if (verifyDaleiZhonglei > 0){
           // return "品种中类关系表中，输入的品种已存在";
            return "输入的中类已存在";
        }
        // 没有重复数据，返回"ok"
        return "验证通过";
    }

    public List<DaleiZhonglei> getAll() {
        return DaleiAndZhongleiMapper.getAll();
    }


    public boolean deleteDaleiZhonglei(String weiyihaoma){
        return DaleiAndZhongleiMapper.deleteDaleiZhonglei(weiyihaoma);
    };
}

