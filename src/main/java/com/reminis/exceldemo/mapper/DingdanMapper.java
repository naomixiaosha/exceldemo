
package com.reminis.exceldemo.mapper;

import com.reminis.exceldemo.entity.CheckCout;
import com.reminis.exceldemo.entity.dingdan;
import com.reminis.exceldemo.entity.dingdan_download;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DingdanMapper {

    //获取分页查询需要的 订单追踪表数据库数据总数
    @Select("select count(*) from 订单追踪表")
    int getCount();

    //分页查询查询订单追踪表
    @Select("SELECT * FROM 订单追踪表 limit #{param1},#{param2}")
    List<dingdan> getLimitAll(int page,int limit);

    //获取所有数据，用于导出Excel
    @Select("SELECT t.唯一号码,t.订单号,t.编码,t.大类,t.中类,t.数量,t.单位,t.单价,t.总价,t.日期 FROM 订单追踪表 as t")
    List<dingdan> getAll();

    //上传插入追踪表数据库
   // @Insert("INSERT INTO fromqf (唯一编号,purchase_number,material_number,material_name,purchase_quantity,unit,unit_price,amount) VALUES (#{id},#{purchase_number},#{material_number},#{material_name},#{purchase_quantity},#{unit},#{unit_price},#{amount})")
    @Insert("insert into 订单追踪表 (唯一号码,注意,日期,订单号,产前样,交期备注,编码,大类,中类,数量,单位,单价,总价,备注1,备注2,预计交期,交期,备注3,备注4) values(#{唯一号码},#{注意},#{日期},#{订单号},#{产前样},#{交期备注},#{编码},#{大类},#{中类},#{数量},#{单位},#{单价},#{总价},#{备注1},#{备注2},#{预计交期},#{交期},#{备注3},#{备注4})")
    Integer save(dingdan dingdan);

    //新增联合查询，fromqf中的材料名称(WW-)，中类，大类，三表联合查询，插入追踪表,2024年11月20日09:46:36修改，将join改为 left join，修复当传入fromqf编码为空时不生成数据到追踪表中
   // @Select(select f.订单号,f.日期,f.编码,f.数量,f.单位,f.单价,f.总价,t.中类,t.大类 from fromqf as f,(select a.品种,a.中类,b.大类 from 品种中类关系表 as a,大类中类关系表 as b where a.中类 = b.中类) as t where f.编码 LIKE t.品种)
    @Select("select f.订单号,f.日期,f.编码,f.数量,f.单位,f.单价,f.金额 as 总价,t.中类,t.大类 from fromqf as f  left join (select a.品种,a.中类,b.大类 from 品种中类关系表 as a,大类中类关系表 as b where a.中类 = b.中类) as t on f.编码 LIKE t.品种")
    List<dingdan> getThreeTableAll();

    //模糊查询，模糊查询订单追踪表
    @Select("SELECT * FROM 订单追踪表 where 编码 like CONCAT('%', #{param1}, '%') limit #{param2},#{param3}")
    List<dingdan> getLikeList(String like,int page,int limit);

    //获取模糊分页查询需要的 订单追踪表数据库数据总数
    @Select("select count(*) from 订单追踪表 where 编码 like CONCAT('%', #{param1}, '%')")
    int getLikeCount(String like);

    //下单日期查询

    @SelectProvider(type = LikeSelectProvider.class,method = "getxiadanDateSelec")
    List<dingdan> getxiadanDateSelec(String start,String end,int page,int limit);

    //下单日期查询数据总数
    @SelectProvider(type = LikeSelectProvider.class,method = "getxiadanCount")
    int getxiadanCount(String start,String end);

    //@SelectProvider一般用于多条件查询使用，多表查询可以直接使用@Select去·写如showFive；
    //多条件查询可以在类文件中写，Mybatis支持在类文件中写动态SQL；
    //一个类可以有多个SQL，type 是类文件名称，method是方法指定里面的SQL
    //模糊查询，订单追踪表，导出Excel
   // @SelectProvider(type = LikeSelectProvider.class,method = "LikeSelectExcelDownload")
    //订单追踪表下载，按钮移动至fromqf界面，下载fromqf数据转换成追踪表格式后数据。
    @Select("select * from 订单追踪表 ")
    List<dingdan_download> getLikeSelectDingdan();


    //清空订单追踪表数据库数据
    @Insert("truncate table 订单追踪表")
    void truncate();


    @SelectProvider(type = LikeSelectProvider.class,method = "getDateSelect")
    List<dingdan> getDateSelect(String start,String end);

    @SelectProvider(type = LikeSelectProvider.class,method = "UpdateJiaoqiDate")
    List<dingdan> UpdateJiaoqiDate(String start,String end);


    //新增三个查询，上传Excel时校验中类是否存在
    @Select("select count(*) from 大类中类关系表 where 中类=#{zhonglei}")
    int checkDaleiZhonglei(String zhonglei);

    @Select("select count(*) from 品种中类关系表 where 中类=#{zhonglei}")
    int checkPingzhongZhonglei(String zhonglei);

    @Select("select count(*) from 周业绩表 where 中类=#{zhonglei}")
    int checkZhouYeJi(String zhonglei);

   // @Select("select * from (select a.编码 as 原编码,count(*) as 原数量 from fromqf as a group by a.编码) as i  join (select b.编码,count(*)  as 新数量 from 订单追踪表 as b group by b.编码) as j on i.原编码 = j.编码 where i.原数量 <> j.新数量")
    @Select("select m.原编码,m.原数量,m.新数量,s.品种,s.中类 from (select * from (select a.唯一号码 as 原唯一号码,a.编码 as 原编码,count(*) as 原数量 from fromqf as a group by a.编码) as i  \n" +
            "join (select b.编码,count(*)  as 新数量 from 订单追踪表 as b group by b.编码) as j \n" +
            "on i.原编码 = j.编码 \n" +
            "where i.原数量 <> j.新数量) as m\n" +
            " join (select * from 品种中类关系表) as s\n" +
            "on m.原编码 like s.品种")
    List<CheckCout> checkCount();

}