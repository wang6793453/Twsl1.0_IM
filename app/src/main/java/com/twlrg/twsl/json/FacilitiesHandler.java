package com.twlrg.twsl.json;


import com.twlrg.twsl.entity.FacilitiesInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class FacilitiesHandler extends JsonHandler
{
    private List<FacilitiesInfo> hotelFacilities = new ArrayList<>();

    public List<FacilitiesInfo> getHotelFacilities()
    {
        return hotelFacilities;
    }


    @Override
    protected void parseJson(JSONObject jsonObj) throws Exception
    {
        try
        {
            JSONArray hotelArr = jsonObj.optJSONArray("data");

            if (null != hotelArr)
            {
                for (int i = 0; i < hotelArr.length(); i++)
                {
                    hotelFacilities.add(new FacilitiesInfo(hotelArr.optJSONObject(i)));
                }
            }




        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
