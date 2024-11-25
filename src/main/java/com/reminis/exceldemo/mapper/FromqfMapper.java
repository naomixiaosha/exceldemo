
package com.reminis.exceldemo.mapper;

import com.reminis.exceldemo.entity.fromqf;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import java.util.List;
import com.reminis.exceldemo.entity.fromqf_download;

@Mapper
public interface FromqfMapper {

    //查询fromqf
    @Select("SELECT * FROM fromqf limit #{param1},#{param2}" )
    List<fromqf> getLimitAll(int page,int limit);

    //查询fromqf
    @Select("SELECT * FROM fromqf" )
    List<fromqf> getAll();

    //查询fromqf,douloadExcel
    @Select("SELECT * FROM fromqf" )
    List<fromqf_download> douloadExcel();

    //获取分页查询需要的 订单追踪表数据库数据总数
    @Select("select count(*) from fromqf")
    int getCount();
    //上传Excel插入数据库
   // @Insert("INSERT INTO fromqf (唯一号码,purchase_number,material_number,material_name,purchase_quantity,unit,unit_price,amount) VALUES (#{id},#{purchase_number},#{material_number},#{material_name},#{purchase_quantity},#{unit},#{unit_price},#{amount})")
    @Insert("INSERT INTO fromqf (唯一号码,订单号,日期,材料编号,编码,图片,数量,单位,单价,金额,成份,备注) VALUES (#{唯一号码},#{订单号},#{日期},#{材料编号},#{编码},#{图片},#{数量},#{单位},#{单价},#{金额},#{成份},#{备注})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer save(fromqf fromqf);

    //清空数据库数据
    @Insert("truncate table fromqf")
    void truncate();




}