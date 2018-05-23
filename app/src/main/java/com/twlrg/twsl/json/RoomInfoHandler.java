package com.twlrg.twsl.json;


import com.twlrg.twsl.entity.OrderInfo;
import com.twlrg.twsl.entity.RoomInfo;

import org.json.JSONObject;


/**
 */
public class RoomInfoHandler extends JsonHandler
{

    private RoomInfo roomInfo;

    public RoomInfo getRoomInfo()
    {
        return roomInfo;
    }

    @Override
    protected void parseJson(JSONObject jsonObj) throws Exception
    {
        try
        {
            roomInfo = new RoomInfo(jsonObj.optJSONObject("data"));

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
