package com.twlrg.twsl.json;


import org.json.JSONObject;


/**
 * 作者：王先云 on 2016/9/7 13:48
 * 邮箱：wangxianyun1@163.com
 * 描述：返回结果
 */
public class ResultHandler extends JsonHandler
{
    private String content;

    public String getContent()
    {
        return content;
    }

    private String data;

    public String getData()
    {
        return data;
    }

    @Override
    protected void parseJson(JSONObject obj) throws Exception
    {

        if (null != obj)
        {
            content = obj.optString("content");

            data = obj.optString("data");
        }
    }
}
