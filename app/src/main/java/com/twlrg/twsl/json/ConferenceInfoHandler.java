package com.twlrg.twsl.json;


import com.twlrg.twsl.entity.ConferenceInfo;
import com.twlrg.twsl.entity.RoomInfo;

import org.json.JSONObject;


/**
 */
public class ConferenceInfoHandler extends JsonHandler
{

    private ConferenceInfo conferenceInfo;

    public ConferenceInfo getConferenceInfo()
    {
        return conferenceInfo;
    }

    @Override
    protected void parseJson(JSONObject jsonObj) throws Exception
    {
        try
        {
            conferenceInfo = new ConferenceInfo(jsonObj.optJSONObject("data"));

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
