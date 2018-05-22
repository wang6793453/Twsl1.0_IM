package com.twlrg.twsl.entity;

import org.json.JSONObject;

/**
 * 作者：王先云 on 2018/4/26 16:31
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class WxPayInfo
{
    private String appid;//wx17e33484aba9fcc2
    private String noncestr;//5NX5M5JiensGRohav0BiYAWFwHQB9NkE
    private String package1;//Sign=WXPay
    private String partnerid;//1498163412
    private String prepayid;//wx26163058653442d87a07f9ee3783099577
    private String timestamp;// 1524731458,
    private String sign;//2B48AED253E31EFDC328A4AA4DF018BF private String



    public WxPayInfo(JSONObject obj)
    {
        this.appid = obj.optString("appid");
        this.noncestr = obj.optString("noncestr");
        this.package1 = obj.optString("package");
        this.partnerid = obj.optString("partnerid");
        this.prepayid = obj.optString("prepayid");
        this.timestamp = obj.optString("timestamp");
        this.sign = obj.optString("sign");

    }

    public String getAppid()
    {
        return appid;
    }

    public void setAppid(String appid)
    {
        this.appid = appid;
    }

    public String getNoncestr()
    {
        return noncestr;
    }

    public void setNoncestr(String noncestr)
    {
        this.noncestr = noncestr;
    }

    public String getPackage1()
    {
        return package1;
    }

    public void setPackage1(String package1)
    {
        this.package1 = package1;
    }

    public String getPartnerid()
    {
        return partnerid;
    }

    public void setPartnerid(String partnerid)
    {
        this.partnerid = partnerid;
    }

    public String getPrepayid()
    {
        return prepayid;
    }

    public void setPrepayid(String prepayid)
    {
        this.prepayid = prepayid;
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getSign()
    {
        return sign;
    }

    public void setSign(String sign)
    {
        this.sign = sign;
    }
}
