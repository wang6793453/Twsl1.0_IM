package com.twlrg.twsl.entity;

import org.json.JSONObject;

/**
 * 作者：王先云 on 2018/5/30 15:35
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class HotelImgInfo
{
    private String id;// 1,
    private String type;// 1,
    private String merchant_id;// 9948,
    private String pic;///hotel.jpg",
    private String status;// 1

    public HotelImgInfo() {}


    public HotelImgInfo(JSONObject obj)
    {
        this.id = obj.optString("id");
        this.type = obj.optString("type");
        this.merchant_id = obj.optString("merchant_id");
        this.pic = obj.optString("pic");
        this.status = obj.optString("status");
    }


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getMerchant_id()
    {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id)
    {
        this.merchant_id = merchant_id;
    }

    public String getPic()
    {
        return pic;
    }

    public void setPic(String pic)
    {
        this.pic = pic;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
