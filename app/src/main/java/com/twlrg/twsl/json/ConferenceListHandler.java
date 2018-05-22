package com.twlrg.twsl.json;



import com.twlrg.twsl.entity.ConferenceInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class ConferenceListHandler extends JsonHandler
{

    private List<ConferenceInfo> conferenceInfoList = new ArrayList<>();

    public List<ConferenceInfo> getConferenceInfoList()
    {
        return conferenceInfoList;
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
                    ConferenceInfo mConferenceInfo = new ConferenceInfo(arr.optJSONObject(i));
                    conferenceInfoList.add(mConferenceInfo);
                }
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
