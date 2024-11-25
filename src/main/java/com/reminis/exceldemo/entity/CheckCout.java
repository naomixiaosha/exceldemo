package com.reminis.exceldemo.entity;

public class CheckCout {


    private String 原编码;
    private int 原数量;
    private int 新数量;
    private String 品种;
    private String 中类;


    @Override
    public String toString() {
        return "CheckCout{" +
                "原编码='" + 原编码 + '\'' +
                ", 原数量=" + 原数量 +
                ", 新数量=" + 新数量 +
                ", 品种='" + 品种 + '\'' +
                ", 中类='" + 中类 + '\'' +
                '}';
    }

    public void set原编码(String 原编码) {
        this.原编码 = 原编码;
    }

    public void set原数量(int 原数量) {
        this.原数量 = 原数量;
    }

    public void set新数量(int 新数量) {
        this.新数量 = 新数量;
    }

    public void set品种(String 品种) {
        this.品种 = 品种;
    }

    public void set中类(String 中类) {
        this.中类 = 中类;
    }

    public String get原编码() {
        return 原编码;
    }

    public int get原数量() {
        return 原数量;
    }

    public int get新数量() {
        return 新数量;
    }

    public String get品种() {
        return 品种;
    }

    public String get中类() {
        return 中类;
    }
}
