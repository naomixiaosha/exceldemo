
package com.reminis.exceldemo.mapper;

import com.reminis.exceldemo.entity.DaleiZhonglei;
import com.reminis.exceldemo.entity.PingzhongZhonglei;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DaleiAndZhongleiMapper {

    //获取分页查询需要的 品种中类关系表数据库数据总数
    @Select("select count(*) from 大类中类关系表")
    int getCount();

    //分页查询查询品种中类关系表
    @Select("SELECT * FROM 大类中类关系表 limit #{param1},#{param2}")
    List<DaleiZhonglei> getLimitAll(int page,int limit);


    //获取所有数据
    @Select("SELECT * FROM 大类中类关系表 ")
    List<DaleiZhonglei> getAll();

    //2024年11月9日13:19:44 新增品种中类关系表
    @Insert("insert into 大类中类关系表 (大类,中类) values(#{大类},#{中类})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer addDaleiZhonglei(DaleiZhonglei daleiZhonglei);

    //模糊查询，模糊查询订品种中类关系表
    @Select("SELECT * FROM 大类中类关系表 where 大类 like CONCAT('%', #{param1}, '%') limit #{param2},#{param3}")
    List<DaleiZhonglei> getLikeList(String like,int page,int limit);

    //获取模糊分页查询需要的 大类中类关系表据库数据总数
    @Select("select count(*) from 大类中类关系表 where 大类 like CONCAT('%', #{param1}, '%')")
    int getLikeCount(String like);


    //根据唯一号码，前端点击编辑，编辑大类中类关系表
    @Update("UPDATE 大类中类关系表 as z SET z.大类=#{大类},z.中类=#{中类} where 唯一号码=#{唯一号码}")
    boolean editdaleizhonglei(DaleiZhonglei daleiZhonglei);

    //校验查询

    //查询大类中类关系表中，中类是否存在，新增是调用验证
    @Select("select count(*) from 大类中类关系表 where 中类 = #{param1}")
    int verifyDaleiZhonglei(String zhonglei);

    //根据唯一号码，前端点击删除，删除一行数据
    @Delete("DELETE FROM 大类中类关系表 where 唯一号码=#{param1}")
    boolean deleteDaleiZhonglei(String weiyihaoma);


}