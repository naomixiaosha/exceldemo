
package com.reminis.exceldemo.service;

import com.reminis.exceldemo.entity.fromqf;
import com.reminis.exceldemo.mapper.FromqfMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.reminis.exceldemo.entity.fromqf_download;

@Service
public class FromqfService {
    @Autowired
    private FromqfMapper fromqfMapper;

 /**
 * 获取所有fromqf对象
 * 此方法通过调用fromqfMapper的getAll方法来获取数据库中所有的fromqf对象，返回一个包含所有fromqf对象的列表
 * @return 包含所有fromqf对象的列表
 */
public List<fromqf> getLimitAll(int pageInteger,int limitInteger) {
    //pageInteger:代表我们现在在第几页上
    //limitInteger：每页的个数
    int pageIndex = (pageInteger-1) * limitInteger;
    int pageSize = limitInteger;
    return fromqfMapper.getLimitAll(pageIndex,pageSize);
}

    public List<fromqf> getAll() {
        return fromqfMapper.getAll();
    }

    public List<fromqf_download> douloadExcel() {
        return fromqfMapper.douloadExcel();
    }

    /**
     * 获取fromqf对象的数量
     * 此方法通过调用fromqfMapper的getCount方法来获取数据库中fromqf对象的数量，返回一个整数表示数量
     * @return fromqf对象的数量
     */
    public int getCount(){
        return fromqfMapper.getCount();
    };

    /**
     * 保存fromqf对象
     * 此方法通过调用fromqfMapper的save方法来保存fromqf对象，返回保存成功的状态
     * @param fromqf 需要保存的fromqf对象
     * @return 保存是否成功
     */
    public boolean save(fromqf fromqf) {
        return fromqfMapper.save(fromqf) > 0;
    }
    public void truncate() {
        fromqfMapper.truncate();
    }


}

