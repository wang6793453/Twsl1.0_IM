package com.twlrg.twsl.entity;

import org.json.JSONObject;

/**
 * 作者：王先云 on 2018/4/23 21:00
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class CommentInfo
{

    private String merchant_id;// 29
    private String nickname;//13421303923
    private String portrait;//\/Uploads\/portrait\/1523938668_545631468
    private String content;//very good
    private String create_time;//2018-04-23 20:44:54、

    public CommentInfo(JSONObject obj)
    {
        this.content = obj.optString("content");
        this.merchant_id = obj.optString("merchant_id");
        this.nickname = obj.optString("nickname");
        this.portrait = obj.optString("portrait");
        this.create_time = obj.optString("create_time");
    }

    public String getMerchant_id()
    {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id)
    {
        this.merchant_id = merchant_id;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public String getPortrait()
    {
        return portrait;
    }

    public void setPortrait(String portrait)
    {
        this.portrait = portrait;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getCreate_time()
    {
        return create_time;
    }

    public void setCreate_time(String create_time)
    {
        this.create_time = create_time;
    }
}
