package com.reminis.exceldemo.entity;

import com.reminis.exceldemo.annotation.ExcelColumn;
import lombok.Data;

@Data
public class dingdan {
    @ExcelColumn(value = "唯一号码")
    private int 唯一号码;

    @ExcelColumn(value = "注意")
    private  String 注意;

    @ExcelColumn(value = "日期",col = 2)
    private  String 日期;

    @ExcelColumn(value = "订单号",col = 3)
    private  String 订单号;

    @ExcelColumn(value = "产前样",col = 4)
    private  String 产前样;

    @ExcelColumn(value = "交期备注",col = 5)
    private  String 交期备注;

    @ExcelColumn(value = "编码",col = 6)
    private  String 编码;

    @ExcelColumn(value = "大类",col = 7)
    private  String 大类;

    @ExcelColumn(value = "中类",col = 8)
    private  String 中类;

    @ExcelColumn(value = "数量",col = 9)
    private  String 数量;

    @ExcelColumn(value = "单位",col = 10)
    private  String 单位;

    @ExcelColumn(value = "单价",col = 11)
    private  String 单价;

    @ExcelColumn(value = "总价",col = 12)
    private  String 总价;

    @ExcelColumn(value = "备注1",col = 13)
    private  String 备注1;

    @ExcelColumn(value = "备注2",col = 14)
    private  String 备注2;

    @ExcelColumn(value = "预计交期",col = 15)
    private  String 预计交期;

    @ExcelColumn(value = "交期",col = 16)
    private  String 交期;

    @ExcelColumn(value = "备注3",col = 17)
    private  String 备注3;

    @ExcelColumn(value = "备注4",col = 18)
    private  String 备注4;

    @Override
    public String toString() {
        return "dingdan{" +
                "唯一号码=" + 唯一号码 +
                ", 注意='" + 注意 + '\'' +
                ", 日期='" + 日期 + '\'' +
                ", 订单号='" + 订单号 + '\'' +
                ", 产前样='" + 产前样 + '\'' +
                ", 交期备注='" + 交期备注 + '\'' +
                ", 编码='" + 编码 + '\'' +
                ", 大类='" + 大类 + '\'' +
                ", 中类='" + 中类 + '\'' +
                ", 数量='" + 数量 + '\'' +
                ", 单位='" + 单位 + '\'' +
                ", 单价='" + 单价 + '\'' +
                ", 总价='" + 总价 + '\'' +
                ", 备注1='" + 备注1 + '\'' +
                ", 备注2='" + 备注2 + '\'' +
                ", 预计交期='" + 预计交期 + '\'' +
                ", 交期='" + 交期 + '\'' +
                ", 备注3='" + 备注3 + '\'' +
                ", 备注4='" + 备注4 + '\'' +
                '}';
    }

    public void set唯一号码(int 唯一号码) {
        this.唯一号码 = 唯一号码;
    }

    public void set注意(String 注意) {
        this.注意 = 注意;
    }

    public void set日期(String 日期) {
        this.日期 = 日期;
    }

    public void set订单号(String 订单号) {
        this.订单号 = 订单号;
    }

    public void set产前样(String 产前样) {
        this.产前样 = 产前样;
    }

    public void set交期备注(String 交期备注) {
        this.交期备注 = 交期备注;
    }

    public void set编码(String 编码) {
        this.编码 = 编码;
    }

    public void set大类(String 大类) {
        this.大类 = 大类;
    }

    public void set中类(String 中类) {
        this.中类 = 中类;
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

    public void set总价(String 总价) {
        this.总价 = 总价;
    }

    public void set备注1(String 备注1) {
        this.备注1 = 备注1;
    }

    public void set备注2(String 备注2) {
        this.备注2 = 备注2;
    }

    public void set预计交期(String 预计交期) {
        this.预计交期 = 预计交期;
    }

    public void set交期(String 交期) {
        this.交期 = 交期;
    }

    public void set备注3(String 备注3) {
        this.备注3 = 备注3;
    }

    public void set备注4(String 备注4) {
        this.备注4 = 备注4;
    }

    public int get唯一号码() {
        return 唯一号码;
    }

    public String get注意() {
        return 注意;
    }

    public String get日期() {
        return 日期;
    }

    public String get订单号() {
        return 订单号;
    }

    public String get产前样() {
        return 产前样;
    }

    public String get交期备注() {
        return 交期备注;
    }

    public String get编码() {
        return 编码;
    }

    public String get大类() {
        return 大类;
    }

    public String get中类() {
        return 中类;
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

    public String get总价() {
        return 总价;
    }

    public String get备注1() {
        return 备注1;
    }

    public String get备注2() {
        return 备注2;
    }

    public String get预计交期() {
        return 预计交期;
    }

    public String get交期() {
        return 交期;
    }

    public String get备注3() {
        return 备注3;
    }

    public String get备注4() {
        return 备注4;
    }
}
