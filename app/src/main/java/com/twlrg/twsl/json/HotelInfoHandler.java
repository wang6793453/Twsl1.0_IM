package com.twlrg.twsl.json;


import com.twlrg.twsl.entity.HotelInfo;
import com.twlrg.twsl.entity.OrderInfo;

import org.json.JSONObject;


/**
 */
public class HotelInfoHandler extends JsonHandler
{

    private HotelInfo hotelInfo;

    public HotelInfo getHotelInfo()
    {
        return hotelInfo;
    }

    @Override
    protected void parseJson(JSONObject jsonObj) throws Exception
    {
        try
        {
            hotelInfo = new HotelInfo(jsonObj.optJSONObject("data"));

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
