package com.twlrg.twsl.json;


import org.json.JSONObject;

/**
 * 作者：王先云 on 2016/9/7 13:48
 * 邮箱：wangxianyun1@163.com
 * 描述：注册
 */
public class RegisterHandler extends JsonHandler
{


    private String uid;

    public String getUid()
    {
        return uid;
    }

    @Override
    protected void parseJson(JSONObject jsonObj) throws Exception
    {
        try
        {
            JSONObject obj = jsonObj.optJSONObject("data");
            if (null != obj)
                uid = obj.optString("uid");

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
