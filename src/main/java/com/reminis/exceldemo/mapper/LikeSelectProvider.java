   package com.reminis.exceldemo.mapper;

   import org.apache.ibatis.jdbc.SQL;

   public class LikeSelectProvider {

       /**
        * like查询，用于下载订单追踪Excel，2024年11月13日15:54:02废弃，改为下载全表
        * @param like
        * @return
        */
      /* public String LikeSelectExcelDownload() {
           String sql = "select * from 订单追踪表 ";
               // 下载Excel时不需要在进行分页，下载所有like查询内容
               //拼接limit
                  //   sql+=" limit "+(page-1)+","+limit;

            return sql;
       }*/

       //业绩表导出，不带参数默认全部导出，带参数则根据中类中文模糊查询
       public String yejiLikeSelectExcelDownload(String like) {
           String sql = "select * from 周业绩表";
           if (like != null && !like.isEmpty()) {
               sql+=" WHERE 中类中文 like '%"+like+"%'";
           }
           // 下载Excel时不需要在进行分页，下载所有like查询内容
           //拼接limit
           //   sql+=" limit "+(page-1)+","+limit;

           return sql;
       }
       /**
        * 根据下单日期，更新数据到周业绩表中,新增判断条件 and a.总价 !=''
        *   2024年11月12日10:53:44 新增 ignore关键字，解决1292的错误，有可能并不是错误，而是警告提示，忽略即可
        *   2024年11月12日15:35:18 ，业务生成数量统计，有两条总价金额无数据，导出时与业务方导出数量差24条，（原因生成周业绩时判断总价!=""）
        */
       public String getDateSelect(String start,String end) {
          // String sql = "UPDATE 周业绩表 as z INNER JOIN (select a.中类,SUM(a.数量)as 数量,SUM(CAST(a.总价 AS DECIMAL(10, 5))) as 总价 from 订单追踪表 as a where a.日期 BETWEEN '"+start+"' and '"+end+"' GROUP BY a.中类) AS t SET z.下单数量 =t.数量 , z.下单金额=t.总价 where z.中类 =t.中类;";
           String sql =  "UPDATE ignore 周业绩表 as z INNER JOIN (select a.中类,SUM(a.数量)as 数量,SUM(CAST(a.总价 AS DECIMAL(10, 4))) as 总价 from 订单追踪表 as a where a.日期 BETWEEN '"+start+"' and '"+end+"'  GROUP BY a.中类) AS t SET z.下单数量 =t.数量 , z.下单金额=t.总价 where z.中类 =t.中类;";
           return sql;
       }

       /**
        * 根据交期，更新数据到周业绩表中,新增判断条件 and a.总价 !=''
        * 2024年11月12日10:53:44 新增 ignore关键字，解决1292的错误，有可能并不是错误，而是警告提示，忽略即可
        */
       public String UpdateJiaoqiDate(String start,String end) {
           String sql = "UPDATE ignore 周业绩表 as z INNER JOIN (select a.中类,SUM(a.数量)as 数量,SUM(CAST(a.总价 AS DECIMAL(10, 4))) as 总价 from 订单追踪表 as a where a.交期 BETWEEN '"+start+"' and '"+end+"'  GROUP BY a.中类) AS t SET z.发货数量 =t.数量 , z.发货金额=t.总价 where z.中类 =t.中类;";
           return sql;
       }
       public String getxiadanDateSelec(String start,String end,int page,int limit) {
           String sql = "SELECT * FROM 订单追踪表 as a where a.日期 BETWEEN '"+start+"' and '"+end+"' limit "+page+","+limit+";";
           return sql;
       }

       public String getxiadanCount(String start,String end) {
           String sql = "select count(*) from 订单追踪表 as a where a.日期 BETWEEN '"+start+"' and '"+end+"';";
           return sql;
       }
   }
   