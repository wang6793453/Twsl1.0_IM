package com.twlrg.twsl.json;


import com.twlrg.twsl.entity.HotelImgInfo;
import com.twlrg.twsl.entity.OrderInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class HotelImgListHandler extends JsonHandler
{

    private List<HotelImgInfo> hotelImgInfoList = new ArrayList<>();

    public List<HotelImgInfo> getHotelImgInfoList()
    {
        return hotelImgInfoList;
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
                    HotelImgInfo mHotelImgInfo = new HotelImgInfo(arr.optJSONObject(i));
                    hotelImgInfoList.add(mHotelImgInfo);
                }
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
