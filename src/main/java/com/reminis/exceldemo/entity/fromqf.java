package com.reminis.exceldemo.entity;

import com.reminis.exceldemo.annotation.ExcelColumn;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 定义实体类
 */
@Data
public class fromqf {

    @ExcelColumn(value = "唯一号码",col = 0)
    private Integer 唯一号码;

    @ExcelColumn(value = "订单号",col = 1)
    private String 订单号;

    @ExcelColumn(value = "日期",col = 2)
    private String 日期;

    @ExcelColumn(value = "材料编号",col = 3)
    private String 材料编号;

    @ExcelColumn(value = "材料名称",col = 4)
    private String 编码;

    @ExcelColumn(value = "图片",col = 5)
    private String 图片;

    @ExcelColumn(value = "采购数量",col = 6)
    private String 数量;

    @ExcelColumn(value = "单位",col = 7)
    private String 单位;

    @ExcelColumn(value = "单价",col = 8)
    private String 单价;

    @ExcelColumn(value = "金额",col = 9)
    private String 金额;

    @ExcelColumn(value = "成份",col = 10)
    private String 成份;

    @ExcelColumn(value = "备注",col = 11)
    private String 备注;

    @Override
    public String toString() {
        return "fromqf{" +
                "唯一号码=" + 唯一号码 +
                ", 订单号='" + 订单号 + '\'' +
                ", 日期='" + 日期 + '\'' +
                ", 材料编号='" + 材料编号 + '\'' +
                ", 编码='" + 编码 + '\'' +
                ", 图片='" + 图片 + '\'' +
                ", 数量='" + 数量 + '\'' +
                ", 单位='" + 单位 + '\'' +
                ", 单价='" + 单价 + '\'' +
                ", 金额='" + 金额 + '\'' +
                ", 成份='" + 成份 + '\'' +
                ", 备注='" + 备注 + '\'' +
                '}';
    }

    public void set唯一号码(Integer 唯一号码) {
        this.唯一号码 = 唯一号码;
    }

    public void set订单号(String 订单号) {
        this.订单号 = 订单号;
    }

    public void set日期(String 日期) {
        this.日期 = 日期;
    }

    public void set材料编号(String 材料编号) {
        this.材料编号 = 材料编号;
    }

    public void set编码(String 编码) {
        this.编码 = 编码;
    }

    public void set图片(String 图片) {
        this.图片 = 图片;
    }

    public void set数量(String 数量) {
        this.数量 = 数量;
    }

    public void set单位(String 单位) {
        this.单位 = 单位;
    }

    public void set单价(String 单价) {
        this.单价 = 单价;
    }

    public void set金额(String 金额) {
        this.金额 = 金额;
    }

    public void set成份(String 成份) {
        this.成份 = 成份;
    }

    public void set备注(String 备注) {
        this.备注 = 备注;
    }

    public Integer get唯一号码() {
        return 唯一号码;
    }

    public String get订单号() {
        return 订单号;
    }

    public String get日期() {
        return 日期;
    }

    public String get材料编号() {
        return 材料编号;
    }

    public String get编码() {
        return 编码;
    }

    public String get图片() {
        return 图片;
    }

    public String get数量() {
        return 数量;
    }

    public String get单位() {
        return 单位;
    }

    public String get单价() {
        return 单价;
    }

    public String get金额() {
        return 金额;
    }

    public String get成份() {
        return 成份;
    }

    public String get备注() {
        return 备注;
    }
}
