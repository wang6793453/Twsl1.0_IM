package com.twlrg.twsl.json;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class FacilitiesHandler extends JsonHandler
{
    private List<String> hotelFacilities      = new ArrayList<>();
    private List<String> activityFacilities   = new ArrayList<>();
    private List<String> roomFacilities       = new ArrayList<>();
    private List<String> conferenceFacilities = new ArrayList<>();
    private List<String> diningFacilities     = new ArrayList<>();

    public List<String> getHotelFacilities()
    {
        return hotelFacilities;
    }

    public List<String> getActivityFacilities()
    {
        return activityFacilities;
    }

    public List<String> getRoomFacilities()
    {
        return roomFacilities;
    }

    public List<String> getConferenceFacilities()
    {
        return conferenceFacilities;
    }

    public List<String> getDiningFacilities()
    {
        return diningFacilities;
    }

    @Override
    protected void parseJson(JSONObject jsonObj) throws Exception
    {
        try
        {
            JSONObject obj = jsonObj.optJSONObject("data");
            JSONArray hotelArr = obj.optJSONArray("hotel_facilities");
            JSONArray activityArr = obj.optJSONArray("activity_facilities");
            JSONArray roomArr = obj.optJSONArray("room_facilities");
            JSONArray conferenceArr = obj.optJSONArray("conference_facilities");
            JSONArray diningArr = obj.optJSONArray("dining_facilities");
            if (null != hotelArr)
            {
                for (int i = 0; i < hotelArr.length(); i++)
                {
                    hotelFacilities.add(hotelArr.optString(i));
                }
            }

            if (null != activityArr)
            {
                for (int i = 0; i < activityArr.length(); i++)
                {
                    activityFacilities.add(activityArr.optString(i));
                }
            }

            if (null != roomArr)
            {
                for (int i = 0; i < roomArr.length(); i++)
                {
                    roomFacilities.add(roomArr.optString(i));
                }
            }

            if (null != conferenceArr)
            {
                for (int i = 0; i < conferenceArr.length(); i++)
                {
                    conferenceFacilities.add(conferenceArr.optString(i));
                }
            }

            if (null != diningArr)
            {
                for (int i = 0; i < diningArr.length(); i++)
                {
                    diningFacilities.add(diningArr.optString(i));
                }
            }


        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
