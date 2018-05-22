package com.twlrg.twsl.json;


import com.twlrg.twsl.entity.WxPayInfo;

import org.json.JSONObject;

/**
 * 作者：王先云 on 2016/9/7 13:48
 * 邮箱：wangxianyun1@163.com
 */
public class WxpayHandler extends JsonHandler
{
    private WxPayInfo wxPayInfo;

    public WxPayInfo getWxPayInfo()
    {
        return wxPayInfo;
    }

    @Override
    protected void parseJson(JSONObject jsonObj) throws Exception
    {
        try
        {
            JSONObject obj = jsonObj.optJSONObject("data");
            wxPayInfo = new WxPayInfo(obj);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
