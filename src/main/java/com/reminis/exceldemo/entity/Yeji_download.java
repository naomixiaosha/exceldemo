package com.reminis.exceldemo.entity;

import com.reminis.exceldemo.annotation.ExcelColumn;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Yeji_download {
    @ExcelColumn(value = "唯一号码")
    private String 唯一号码;

    @ExcelColumn(value = "中类中文",col = 1)
    private String 中类中文;

    @ExcelColumn(value = "中类",col = 2)
    private String 中类;

    @ExcelColumn(value = "单位",col = 3)
    private String 单位;

    @ExcelColumn(value = "下单数量",col = 4)
    private Double 下单数量;

    @ExcelColumn(value = "下单金额",col = 5)
    private BigDecimal 下单金额;

    @ExcelColumn(value = "发货数量",col = 6)
    private Double 发货数量;

    @ExcelColumn(value = "发货金额",col = 7)
    private BigDecimal 发货金额;


}
