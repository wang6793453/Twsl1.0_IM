package com.twlrg.twsl.entity;

import java.util.List;

/**
 * 作者：王先云 on 2018/5/14 21:05
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class RoomMonthInfo
{
    private int year;
    private int  month;

    private List<RoomDayInfo> roomDayInfoList;


    public RoomMonthInfo(){}

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

    public List<RoomDayInfo> getRoomDayInfoList()
    {
        return roomDayInfoList;
    }

    public void setRoomDayInfoList(List<RoomDayInfo> roomDayInfoList)
    {
        this.roomDayInfoList = roomDayInfoList;
    }
}
