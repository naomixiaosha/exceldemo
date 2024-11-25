
package com.reminis.exceldemo.entity;

import lombok.Data;

@Data
public class PingzhongZhonglei {

    private int 唯一号码;
    //品种名称
    private String 品种;
    //中类名称
    private String 中类;

    @Override
    public String toString() {
        return "PingzhongZhonglei{" +
                "唯一号码=" + 唯一号码 +
                ", 品种='" + 品种 + '\'' +
                ", 中类='" + 中类 + '\'' +
                '}';
    }

    public void set唯一号码(int 唯一号码) {
        this.唯一号码 = 唯一号码;
    }

    public void set品种(String 品种) {
        this.品种 = 品种;
    }

    public void set中类(String 中类) {
        this.中类 = 中类;
    }

    public int get唯一号码() {
        return 唯一号码;
    }

    public String get品种() {
        return 品种;
    }

    public String get中类() {
        return 中类;
    }
}