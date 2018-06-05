package com.twlrg.twsl.json;


import android.content.Context;


import com.twlrg.twsl.utils.ConstantUtil;

import org.json.JSONObject;


public abstract class JsonHandler
{

    private String resultCode = null;
    private String resultMsg  = null;

    protected abstract void parseJson(JSONObject obj) throws Exception;

    public void parseJson(Context mContext, String jsonString)
    {
        try
        {
            if (null == jsonString)
            {
                setResultCode(ConstantUtil.RESULT_FAIL);
            }
            else
            {
                JSONObject jsonObject = new JSONObject(jsonString);

                String ret = jsonObject.optString("ret");
                String msg = jsonObject.optString("msg");
                if (ret.endsWith("01") || msg.contains("成功"))
                {
                    setResultCode(ConstantUtil.RESULT_SUCCESS);
                }
                else
                {

                    if (ret.endsWith("02") && ret.startsWith("20"))
                    {
                        setResultCode(ConstantUtil.RESULT_SUCCESS);
                    }
                    else
                    {
                        setResultCode(ret);

                    }
                    setResultMsg(jsonObject.optString("msg"));
                }

                if (null != jsonObject) parseJson(jsonObject);
            }
        } catch (Exception e)
        {
            setResultCode(ConstantUtil.RESULT_FAIL);
            setResultMsg("网络请求失败...");
        }

    }

    public String getResultCode()
    {
        return resultCode;
    }

    public void setResultCode(String resultCode)
    {
        this.resultCode = resultCode;
    }

    public String getResultMsg()
    {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg)
    {
        this.resultMsg = resultMsg;
    }

}
