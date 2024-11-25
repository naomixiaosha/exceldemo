package com.reminis.exceldemo.entity;

import com.reminis.exceldemo.annotation.ExcelColumn;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 定义fromqf导出Excel需要的格式实体类
 */
@Data
public class fromqf_download {

    @ExcelColumn(value = "唯一号码")
    private Integer 唯一号码;

    @ExcelColumn(value = "订单日期",col = 1)
    private Date 日期;

    @ExcelColumn(value = "客户代码",col = 2)
    private String 客户代码;

    @ExcelColumn(value = "预交日期",col = 3)
    private Date 预交日期;

    @ExcelColumn(value = "预交备注",col = 4)
    private String 预交备注;

    @ExcelColumn(value = "扣税类别",col = 5)
    private String 扣税类别;

    @ExcelColumn(value = "客户订单",col = 6)
    private String 订单号;

    @ExcelColumn(value = "成份",col = 7)
    private String 成份;

    @ExcelColumn(value = "品名",col = 8)
    private String 编码;

    @ExcelColumn(value = "币别",col = 9)
    private String 币别;

    @ExcelColumn(value = "汇率",col = 10)
    private String 汇率;

    @ExcelColumn(value = "数量",col = 11)
    private Double 数量;

    @ExcelColumn(value = "单位",col = 12)
    private String 单位;

    @ExcelColumn(value = "单价",col = 13)
    private BigDecimal 单价;

    @ExcelColumn(value = "未税金额",col = 14)
    private String 未税金额;

    @ExcelColumn(value = "税额",col = 15)
    private String 税额;

    @ExcelColumn(value = "金额",col = 16)
    private BigDecimal 金额;

    @ExcelColumn(value = "税率",col = 17)
    private String 税率;

    @ExcelColumn(value = "表身备注",col = 18)
    private String 备注;

    @ExcelColumn(value = "材料编号")
    private String 材料编号;

    @ExcelColumn(value = "图片")
    private String 图片;

    @Override
    public String toString() {
        return "fromqf_download{" +
                "唯一号码=" + 唯一号码 +
                ", 日期='" + 日期 + '\'' +
                ", 客户代码='" + 客户代码 + '\'' +
                ", 预交日期='" + 预交日期 + '\'' +
                ", 预交备注='" + 预交备注 + '\'' +
                ", 扣税类别='" + 扣税类别 + '\'' +
                ", 订单号='" + 订单号 + '\'' +
                ", 成份='" + 成份 + '\'' +
                ", 编码='" + 编码 + '\'' +
                ", 币别='" + 币别 + '\'' +
                ", 汇率='" + 汇率 + '\'' +
                ", 数量=" + 数量 +
                ", 单位='" + 单位 + '\'' +
                ", 单价=" + 单价 +
                ", 未税金额='" + 未税金额 + '\'' +
                ", 税额='" + 税额 + '\'' +
                ", 金额=" + 金额 +
                ", 税率='" + 税率 + '\'' +
                ", 备注='" + 备注 + '\'' +
                ", 材料编号='" + 材料编号 + '\'' +
                ", 图片='" + 图片 + '\'' +
                '}';
    }

    public Integer get唯一号码() {
        return 唯一号码;
    }

    public Date get日期() {
        return 日期;
    }

    public String get客户代码() {
        return 客户代码;
    }

    public Date get预交日期() {
        return 预交日期;
    }

    public String get预交备注() {
        return 预交备注;
    }

    public String get扣税类别() {
        return 扣税类别;
    }

    public String get订单号() {
        return 订单号;
    }

    public String get成份() {
        return 成份;
    }

    public String get编码() {
        return 编码;
    }

    public String get币别() {
        return 币别;
    }

    public String get汇率() {
        return 汇率;
    }

    public Double get数量() {
        return 数量;
    }

    public String get单位() {
        return 单位;
    }

    public BigDecimal get单价() {
        return 单价;
    }

    public String get未税金额() {
        return 未税金额;
    }

    public String get税额() {
        return 税额;
    }

    public BigDecimal get金额() {
        return 金额;
    }

    public String get税率() {
        return 税率;
    }

    public String get备注() {
        return 备注;
    }

    public String get材料编号() {
        return 材料编号;
    }

    public String get图片() {
        return 图片;
    }

    public void set唯一号码(Integer 唯一号码) {
        this.唯一号码 = 唯一号码;
    }

    public void set日期(Date 日期) {
        this.日期 = 日期;
    }

    public void set客户代码(String 客户代码) {
        this.客户代码 = 客户代码;
    }

    public void set预交日期(Date 预交日期) {
        this.预交日期 = 预交日期;
    }

    public void set预交备注(String 预交备注) {
        this.预交备注 = 预交备注;
    }

    public void set扣税类别(String 扣税类别) {
        this.扣税类别 = 扣税类别;
    }

    public void set订单号(String 订单号) {
        this.订单号 = 订单号;
    }

    public void set成份(String 成份) {
        this.成份 = 成份;
    }

    public void set编码(String 编码) {
        this.编码 = 编码;
    }

    public void set币别(String 币别) {
        this.币别 = 币别;
    }

    public void set汇率(String 汇率) {
        this.汇率 = 汇率;
    }

    public void set数量(Double 数量) {
        this.数量 = 数量;
    }

    public void set单位(String 单位) {
        this.单位 = 单位;
    }

    public void set单价(BigDecimal 单价) {
        this.单价 = 单价;
    }

    public void set未税金额(String 未税金额) {
        this.未税金额 = 未税金额;
    }

    public void set税额(String 税额) {
        this.税额 = 税额;
    }

    public void set金额(BigDecimal 金额) {
        this.金额 = 金额;
    }

    public void set税率(String 税率) {
        this.税率 = 税率;
    }

    public void set备注(String 备注) {
        this.备注 = 备注;
    }

    public void set材料编号(String 材料编号) {
        this.材料编号 = 材料编号;
    }

    public void set图片(String 图片) {
        this.图片 = 图片;
    }
}
