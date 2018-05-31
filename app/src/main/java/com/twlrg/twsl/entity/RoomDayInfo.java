package com.twlrg.twsl.entity;

import android.text.TextUtils;

import org.json.JSONObject;

/**
 * 作者：王先云 on 2018/5/14 21:02
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class RoomDayInfo
{
    private String room_id;
    private String merchant_id;
    private String id;
    private int    year;
    private int    month;
    private int    day;
    private int    status;

    private String wz_price;
    private String dz_price;
    private String sz_price;

    public RoomDayInfo() {}

    public RoomDayInfo(JSONObject obj)
    {
        this.id = obj.optString("id");
        this.wz_price = obj.optString("wz");
        this.dz_price = obj.optString("dz");
        this.sz_price = obj.optString("sz");
        this.room_id = obj.optString("room_id");
        this.merchant_id = obj.optString("merchant_id");
        String date = obj.optString("date");

        if (!TextUtils.isEmpty(date))
        {
            String[] time = date.split("-");
            this.year = Integer.parseInt(time[0]);
            this.month = Integer.parseInt(time[1]);
            this.day = Integer.parseInt(time[2]);
        }

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


    public String getWz_price()
    {
        return wz_price;
    }

    public void setWz_price(String wz_price)
    {
        this.wz_price = wz_price;
    }

    public String getDz_price()
    {
        return dz_price;
    }

    public void setDz_price(String dz_price)
    {
        this.dz_price = dz_price;
    }

    public String getSz_price()
    {
        return sz_price;
    }

    public void setSz_price(String sz_price)
    {
        this.sz_price = sz_price;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }
}
