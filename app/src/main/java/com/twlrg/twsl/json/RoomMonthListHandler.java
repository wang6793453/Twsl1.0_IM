package com.twlrg.twsl.json;


import com.twlrg.twsl.entity.BillInfo;
import com.twlrg.twsl.entity.RoomDayInfo;
import com.twlrg.twsl.entity.RoomMonthInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 */
public class RoomMonthListHandler extends JsonHandler
{
    private List<RoomMonthInfo> monthInfoList = new ArrayList<>();

    public List<RoomMonthInfo> getMonthInfoList()
    {
        return monthInfoList;
    }

    @Override
    protected void parseJson(JSONObject jsonObj) throws Exception
    {
        try
        {
            JSONArray arr = jsonObj.optJSONArray("data");


            if (null != arr)
            {
                for (int i = 0; i < arr.length(); i++)
                {

                    JSONArray monthArr = arr.getJSONArray(i);
                    List<RoomDayInfo> roomDayInfoList = new ArrayList<>();
                    for (int j = 0; j < monthArr.length(); j++)
                    {
                        RoomDayInfo mRoomDayInfo = new RoomDayInfo(monthArr.optJSONObject(j));
                        roomDayInfoList.add(mRoomDayInfo);

                    }
                    RoomMonthInfo mRoomMonthInfo = new RoomMonthInfo();
                    if (!roomDayInfoList.isEmpty())
                    {
                        mRoomMonthInfo.setMonth(roomDayInfoList.get(0).getMonth());
                        mRoomMonthInfo.setYear(roomDayInfoList.get(0).getYear());
                    }


                    //得到该月份的第一天
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, mRoomMonthInfo.getYear());          //指定年份
                    calendar.set(Calendar.MONTH, mRoomMonthInfo.getMonth() - 1);        //指定月份 Java月份从0开始算
                    calendar.set(Calendar.DAY_OF_MONTH, 1);

                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);             //得到该月份第一天 是星期几
                    for (int j = 0; j < dayOfWeek - 1; j++)
                    {
                        RoomDayInfo mRoomDayInfo = new RoomDayInfo();
                        mRoomDayInfo.setYear(mRoomMonthInfo.getYear());
                        mRoomDayInfo.setMonth(mRoomMonthInfo.getMonth());
                        mRoomDayInfo.setDay(0);
                        roomDayInfoList.add(0, mRoomDayInfo);
                    }


                    int days = roomDayInfoList.size();
                    int buweiDays = 7 - days % 7;

                    if (buweiDays != 7)
                    {
                        for (int j = 0; j < buweiDays; j++)
                        {
                            RoomDayInfo mRoomDayInfo = new RoomDayInfo();
                            mRoomDayInfo.setYear(mRoomMonthInfo.getYear());
                            mRoomDayInfo.setMonth(mRoomMonthInfo.getMonth());
                            mRoomDayInfo.setDay(0);
                            roomDayInfoList.add(mRoomDayInfo);
                        }
                    }
                    mRoomMonthInfo.setRoomDayInfoList(roomDayInfoList);
                    monthInfoList.add(mRoomMonthInfo);
                }


            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
