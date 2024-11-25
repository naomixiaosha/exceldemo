
package com.reminis.exceldemo.mapper;

import com.reminis.exceldemo.entity.DaleiZhonglei;
import com.reminis.exceldemo.entity.PingzhongZhonglei;
import com.reminis.exceldemo.entity.Yeji;
import com.reminis.exceldemo.entity.Yeji_download;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PingzhongAndZhongleiMapper {

    //获取分页查询需要的 品种中类关系表数据库数据总数
    @Select("select count(*) from 品种中类关系表")
    int getCount();

    //分页查询查询品种中类关系表
    @Select("SELECT * FROM 品种中类关系表 limit #{param1},#{param2}")
    List<PingzhongZhonglei> getLimitAll(int page,int limit);


    //获取所有数据
    @Select("SELECT * FROM 品种中类关系表 ")
    List<PingzhongZhonglei> getAll();

    //2024年11月9日13:19:44 新增品种中类关系表
    @Insert("insert into 品种中类关系表 (品种,中类) values(#{品种},#{中类})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer addPingzhongZhonglei(PingzhongZhonglei pingzhongZhonglei);

    //模糊查询，模糊查询订品种中类关系表
    @Select("SELECT * FROM 品种中类关系表 where 品种 like CONCAT('%', #{param1}, '%') limit #{param2},#{param3}")
    List<PingzhongZhonglei> getLikeList(String like,int page,int limit);

    //获取模糊分页查询需要的 品种中类关系表据库数据总数
    @Select("select count(*) from 品种中类关系表 where 品种 like CONCAT('%', #{param1}, '%')")
    int getLikeCount(String like);


    //根据唯一号码，前端点击编辑，编辑周业绩表数据
    @Update("UPDATE 品种中类关系表 as z SET z.品种=#{品种},z.中类=#{中类} where 唯一号码=#{唯一号码}")
    boolean editPingzhong(PingzhongZhonglei PingzhongZhonglei);



    //根据唯一号码，前端点击仅修改中类，只改掉中类
    @Update("UPDATE 品种中类关系表 as z SET z.中类=#{中类} where 唯一号码=#{唯一号码}")
    boolean editZhonglei(PingzhongZhonglei PingzhongZhonglei);

    //校验查询





    //查询品种中类关系表中，品种是否存在
    @Select("select count(*) from 品种中类关系表 where 品种 = #{param1}")
    int verifypingzhong(String pingzhong);
    //2024年11月20日10:20:04 新增品种中类关系表校验，模糊查询,校验输出的品种
    @Select("select count(*) from 品种中类关系表 where 品种 like CONCAT('%', #{param1}, '%')")
    int verifyLikepingzhong(String pingzhong);

    //根据唯一号码，前端点击删除，清空周业绩表中的统计数量及金额数据
    @Delete("DELETE FROM 品种中类关系表 where 唯一号码=#{param1}")
    boolean Pingzhongdelete(String zhongleiname);


}