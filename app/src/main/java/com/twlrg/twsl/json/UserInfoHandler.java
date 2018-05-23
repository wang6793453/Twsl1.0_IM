package com.twlrg.twsl.json;


import com.twlrg.twsl.entity.OrderInfo;
import com.twlrg.twsl.entity.UserInfo;

import org.json.JSONObject;


/**
 */
public class UserInfoHandler extends JsonHandler
{

    private UserInfo userInfo;

    public UserInfo getUserInfo()
    {
        return userInfo;
    }

    @Override
    protected void parseJson(JSONObject jsonObj) throws Exception
    {
        try
        {
            userInfo = new UserInfo(jsonObj.optJSONObject("data"));

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
