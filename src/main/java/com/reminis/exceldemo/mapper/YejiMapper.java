
package com.reminis.exceldemo.mapper;

import com.reminis.exceldemo.entity.PingzhongZhonglei;
import com.reminis.exceldemo.entity.Yeji;
import com.reminis.exceldemo.entity.DaleiZhonglei;
import com.reminis.exceldemo.entity.Yeji_download;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface YejiMapper {

    //获取分页查询需要的 周业绩表数据库数据总数
    @Select("select count(*) from 周业绩表")
    int getCount();

    //分页查询查询周业绩表
    @Select("SELECT * FROM 周业绩表 limit #{param1},#{param2}")
    List<Yeji> getLimitAll(int page,int limit);

    //获取所有数据，用于导出Excel
    @Select("SELECT * FROM 周业绩表 ")
    List<Yeji> getAll();


    //新增周业绩表中类型，中类，单位
    @Insert("insert into 周业绩表 (中类中文,中类,单位) values(#{中类中文},#{中类},#{单位})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer addyeji(Yeji yeji);


    //模糊查询，模糊查询订周业绩表
    @Select("SELECT * FROM 周业绩表 where 中类中文 like CONCAT('%', #{param1}, '%') limit #{param2},#{param3}")
    List<Yeji> getLikeList(String like,int page,int limit);

    //获取模糊分页查询需要的 周业绩表据库数据总数
    @Select("select count(*) from 周业绩表 where 中类中文 like CONCAT('%', #{param1}, '%')")
    int getLikeCount(String like);

    //@SelectProvider一般用于多条件查询使用，多表查询可以直接使用@Select去·写如showFive；
    //多条件查询可以在类文件中写，Mybatis支持在类文件中写动态SQL；
    //一个类可以有多个SQL，type 是类文件名称，method是方法指定里面的SQL
    //根据中类中文字段进行查询周业绩表，导出Excel
    @SelectProvider(type = LikeSelectProvider.class,method = "yejiLikeSelectExcelDownload")
    List<Yeji_download> getLikeSelectDingdan(String like);


    //清空周业绩表数据库数据,订单追踪界面点击生成周业绩时，将周业绩表统计数量及金额清空为null
    @Insert("UPDATE 周业绩表 as z SET z.下单数量 = NULL,z.下单金额 = NULL,z.发货数量 = NULL,z.发货金额 = NULL")
    void truncate();

    //根据交期，更新数据到周业绩表中
    @SelectProvider(type = LikeSelectProvider.class,method = "UpdateJiaoqiDate")
    List<Yeji> UpdateJiaoqiDate(String start, String end);

    //根据唯一号码，前端点击删除，清空周业绩表中的统计数量及金额数据
    @Update("UPDATE 周业绩表 as z SET z.下单数量 = null,z.下单金额=null,z.发货数量=null,z.发货金额=null where 唯一号码=#{param1}")
    boolean UpdateZhongleiNameDate(String zhongleiname);

    //根据唯一号码，前端点击编辑，编辑周业绩表数据
    @Update("UPDATE 周业绩表 as z SET z.中类中文 = #{中类中文},z.中类=#{中类},z.单位 = #{单位},z.下单数量=#{下单数量},z.下单金额=#{下单金额},z.发货数量=#{发货数量},z.发货金额=#{发货金额} where 唯一号码=#{唯一号码}")
    boolean UpdateZhongleAll(Yeji_download yeji);


    //校验查询，新增周业绩表时，调用方法

    //查询周业绩表中，中类中文是否存在
    @Select("select count(*) from 周业绩表 where 中类中文 = #{param1}")
    int verifyzhongleiname(String zhongleiname);
    //查询周业绩表中，中类是否存在
    @Select("select count(*) from 周业绩表 where 中类 = #{param1}")
    int verifyyjzhonglei(String zhonglei);






}