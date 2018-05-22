package com.twlrg.twsl.json;


import com.twlrg.twsl.entity.HotelInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class HotelInfoListHandler extends JsonHandler
{

    private List<HotelInfo> hotelInfoList = new ArrayList<>();

    public List<HotelInfo> getHotelInfoList()
    {
        return hotelInfoList;
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
                    HotelInfo hotelInfo = new HotelInfo(arr.optJSONObject(i));
                    hotelInfoList.add(hotelInfo);
                }
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
