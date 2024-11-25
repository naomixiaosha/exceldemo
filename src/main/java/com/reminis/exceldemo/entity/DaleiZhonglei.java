package com.reminis.exceldemo.entity;

import lombok.Data;

@Data
public class DaleiZhonglei {

    private int 唯一号码;

    private String 大类;

    private String 中类;


    @Override
    public String toString() {
        return "DaleiZhonglei{" +
                "唯一号码=" + 唯一号码 +
                ", 大类='" + 大类 + '\'' +
                ", 中类='" + 中类 + '\'' +
                '}';
    }

    public void set唯一号码(int 唯一号码) {
        this.唯一号码 = 唯一号码;
    }

    public void set大类(String 大类) {
        this.大类 = 大类;
    }

    public void set中类(String 中类) {
        this.中类 = 中类;
    }



    public int get唯一号码() {
        return 唯一号码;
    }

    public String get大类() {
        return 大类;
    }

    public String get中类() {
        return 中类;
    }


}
