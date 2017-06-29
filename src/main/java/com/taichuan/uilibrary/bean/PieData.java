package com.taichuan.uilibrary.bean;

/**
 * Created by gui on 2017/6/29.
 * 扇形组件
 */
public class PieData {

    private String name;
    private float percent;


    public PieData(String name, float percent) {
        this.name = name;
        this.percent = percent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }
}
