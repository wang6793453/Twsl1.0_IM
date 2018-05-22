package com.twlrg.twsl.entity;

import org.json.JSONObject;

/**
 * 作者：王先云 on 2018/5/14 21:02
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class RoomDayInfo
{
    private int year;
    private int month;
    private int day;
    private int    status;

    private int  wz_price;
    private int  dz_price;
    private int  sz_price;

    public RoomDayInfo() {}

    public RoomDayInfo(JSONObject obj)
    {
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getYear()
    {
        return year;
    }

    public void setYear(int year)
    {
        this.year = year;
    }

    public int getMonth()
    {
        return month;
    }

    public void setMonth(int month)
    {
        this.month = month;
    }

    public int getDay()
    {
        return day;
    }

    public void setDay(int day)
    {
        this.day = day;
    }


    public int getWz_price()
    {
        return wz_price;
    }

    public void setWz_price(int wz_price)
    {
        this.wz_price = wz_price;
    }

    public int getDz_price()
    {
        return dz_price;
    }

    public void setDz_price(int dz_price)
    {
        this.dz_price = dz_price;
    }

    public int getSz_price()
    {
        return sz_price;
    }

    public void setSz_price(int sz_price)
    {
        this.sz_price = sz_price;
    }
}
