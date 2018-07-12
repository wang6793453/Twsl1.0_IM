package com.twlrg.twsl.entity;

/**
 * 作者：王先云 on 2018/7/12 21:47
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class LngLat
{
    private double longitude;//经度
    private double lantitude;//维度

    public LngLat() {
    }

    public LngLat(double longitude, double lantitude) {
        this.longitude = longitude;
        this.lantitude = lantitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLantitude() {
        return lantitude;
    }

    public void setLantitude(double lantitude) {
        this.lantitude = lantitude;
    }

    @Override
    public String toString() {
        return "LngLat{" +
                "longitude=" + longitude +
                ", lantitude=" + lantitude +
                '}';
    }

}
