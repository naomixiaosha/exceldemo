
package com.reminis.exceldemo.service;

import com.reminis.exceldemo.entity.PingzhongZhonglei;
import com.reminis.exceldemo.entity.Yeji;
import com.reminis.exceldemo.entity.DaleiZhonglei;
import com.reminis.exceldemo.entity.Yeji_download;
import com.reminis.exceldemo.mapper.YejiMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YejiService {
    @Autowired
    private YejiMapper YejiMapper;

 /**
 * 获取所有dingdan对象
 * 此方法通过调用dingdanMapper的getAll方法来获取数据库中所有的dingdan对象，返回一个包含所有dingdan对象的列表
 * @return 包含所有dingdan对象的列表
 */
public List<Yeji> getLimitAll(int pageInteger,int limitInteger) {
    //pageInteger:代表我们现在在第几页上
    //limitInteger：每页的个数
    int pageIndex = (pageInteger-1) * limitInteger;
    int pageSize = limitInteger;
    return YejiMapper.getLimitAll(pageIndex,pageSize);
}
/**
 * 获取所有dingdan对象，在后台下载Excel时调用
 * 此方法通过调用dingdanMapper的getAll方法来获取数据库中所有的dingdan对象，返回一个包含所有dingdan对象的列表
 * @return 包含所有dingdan对象的列表
 */
public List<Yeji> getAll() {
    return YejiMapper.getAll();
}

    /**
     * 新增周业绩表的中类类别
     */
    public boolean addyeji(Yeji yeji) {
        return YejiMapper.addyeji(yeji) > 0;
    }



    public int getCount(){
        return YejiMapper.getCount();
    };


    /**
     * 清空业绩表
     * 此方法用于清空周业绩表中的统计数量及金额数据
     */
    public void truncate() {
        YejiMapper.truncate();
    }

    /**
     * 模糊查询，业绩表
     * @return
     */
    public List<Yeji> getLikeList(String like, int page, int limit){
        page = (page-1)*limit;
        return YejiMapper.getLikeList(like,page,limit);
    };

    /**
     * 模糊查询，订单追踪表，导出Excel
     * @return
     */
    public int getLikeCount(String like){
        return YejiMapper.getLikeCount(like);
    };


    /**
     * 模糊查询，订单追踪表，导出模糊查询数据写入Excel
     * @return
     */
    public List<Yeji_download> getLikeSelectDingdan(String like){
        return YejiMapper.getLikeSelectDingdan(like);
    };

    // 更新周业绩表，根据唯一号码清空对应数据
    public boolean UpdateZhongleiNameDate(String zhongleiname){
        return YejiMapper.UpdateZhongleiNameDate(zhongleiname);
    };

    //周业绩编辑
    public boolean UpdateZhongleAll(Yeji_download yeji){
        return YejiMapper.UpdateZhongleAll(yeji);
    };

    //周业绩界面新增时，查询是否有重复数据，2024年11月19日15:13:53 修改后，只校验周业绩表中中类中文，中类2个字段是否重复，不再3表联合校验
    public String verify(String zhongleiname,String zhonglei) {
        //2024年11月19日14:52:52 添加周业绩界面新增时，查询是否有重复数据，只验证中类中文，中类
        int verifyzhongleiname = YejiMapper.verifyzhongleiname(zhongleiname);
        if (verifyzhongleiname > 0){
            return "周业绩表中，输入的：'"+zhongleiname+"'中类中文已存在";
        }

        int verifyyjzhonglei = YejiMapper.verifyyjzhonglei(zhonglei);
        if (verifyyjzhonglei > 0){
            return "周业绩表中，输入的：'"+zhonglei+"'中类已存在";
        }

        // 没有重复数据，返回"ok"
        return "验证通过";
    }


}

