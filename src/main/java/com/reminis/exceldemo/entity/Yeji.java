package com.reminis.exceldemo.entity;

import com.reminis.exceldemo.annotation.ExcelColumn;
import lombok.Data;

@Data
public class Yeji {
    @ExcelColumn(value = "唯一号码")
    private int 唯一号码;

    @ExcelColumn(value = "中类中文",col = 1)
    private String 中类中文;

    @ExcelColumn(value = "中类",col = 2)
    private String 中类;

    @ExcelColumn(value = "单位",col = 3)
    private String 单位;

    @ExcelColumn(value = "下单数量",col = 4)
    private String 下单数量;

    @ExcelColumn(value = "下单金额",col = 5)
    private String 下单金额;

    @ExcelColumn(value = "发货数量",col = 6)
    private String 发货数量;

    @ExcelColumn(value = "发货金额",col = 7)
    private String 发货金额;

    @Override
    public String toString() {
        return "Yeji{" +
                "唯一号码='" + 唯一号码 + '\'' +
                ", 中类中文='" + 中类中文 + '\'' +
                ", 中类='" + 中类 + '\'' +
                ", 单位='" + 单位 + '\'' +
                ", 下单数量='" + 下单数量 + '\'' +
                ", 下单金额='" + 下单金额 + '\'' +
                ", 发货数量='" + 发货数量 + '\'' +
                ", 发货金额='" + 发货金额 + '\'' +
                '}';
    }

    public int get唯一号码() {
        return 唯一号码;
    }

    public String get中类中文() {
        return 中类中文;
    }

    public String get中类() {
        return 中类;
    }

    public String get单位() {
        return 单位;
    }

    public String get下单数量() {
        return 下单数量;
    }

    public String get下单金额() {
        return 下单金额;
    }

    public String get发货数量() {
        return 发货数量;
    }

    public String get发货金额() {
        return 发货金额;
    }

    public void set唯一号码(int 唯一号码) {
        this.唯一号码 = 唯一号码;
    }

    public void set中类中文(String 中类中文) {
        this.中类中文 = 中类中文;
    }

    public void set中类(String 中类) {
        this.中类 = 中类;
    }

    public void set单位(String 单位) {
        this.单位 = 单位;
    }

    public void set下单数量(String 下单数量) {
        this.下单数量 = 下单数量;
    }

    public void set下单金额(String 下单金额) {
        this.下单金额 = 下单金额;
    }

    public void set发货数量(String 发货数量) {
        this.发货数量 = 发货数量;
    }

    public void set发货金额(String 发货金额) {
        this.发货金额 = 发货金额;
    }
}
