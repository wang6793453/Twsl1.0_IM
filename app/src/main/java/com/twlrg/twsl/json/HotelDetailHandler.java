package com.twlrg.twsl.json;


import com.twlrg.twsl.entity.ConferenceInfo;
import com.twlrg.twsl.entity.HotelImgInfo;
import com.twlrg.twsl.entity.HotelInfo;
import com.twlrg.twsl.entity.RoomInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class HotelDetailHandler extends JsonHandler
{

    private HotelInfo mHotelInfo;

    private List<RoomInfo> roomInfoList = new ArrayList<>();

    private List<ConferenceInfo> conferenceInfoList = new ArrayList<>();

    public HotelInfo getHotelInfo()
    {
        return mHotelInfo;
    }

    public List<RoomInfo> getRoomInfoList()
    {
        return roomInfoList;
    }

    public List<ConferenceInfo> getConferenceInfoList()
    {
        return conferenceInfoList;
    }

    @Override
    protected void parseJson(JSONObject jsonObj) throws Exception
    {
        try
        {
            JSONObject obj = jsonObj.optJSONObject("data");

            JSONArray roomArr = obj.optJSONArray("room");
            JSONArray conferenceArr = obj.optJSONArray("conference");

            JSONObject hotelObj = obj.optJSONObject("hotel");

            if (null != hotelObj)
            {
                mHotelInfo = new HotelInfo(hotelObj);
            }

            JSONArray arr = obj.optJSONArray("hotel_images_list");

            List<HotelImgInfo> hotelImgInfoList = new ArrayList<>();
            if (null != arr)
            {

                for (int i = 0; i < arr.length(); i++)
                {
                    HotelImgInfo mHotelImgInfo = new HotelImgInfo(arr.optJSONObject(i));
                    hotelImgInfoList.add(mHotelImgInfo);
                }
            }
            mHotelInfo.setHotelImgInfoList(hotelImgInfoList);

            if (null != roomArr)
            {
                for (int i = 0; i < roomArr.length(); i++)
                {
                    RoomInfo mRoomInfo = new RoomInfo(roomArr.optJSONObject(i));
                    roomInfoList.add(mRoomInfo);
                }
            }


            if (null != conferenceArr)
            {
                for (int i = 0; i < conferenceArr.length(); i++)
                {
                    ConferenceInfo mConferenceInfo = new ConferenceInfo(conferenceArr.optJSONObject(i));
                    conferenceInfoList.add(mConferenceInfo);
                }
            }


        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
