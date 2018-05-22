package com.twlrg.twsl.entity;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 作者：王先云 on 2018/4/24 21:06
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class SaleInfo implements Serializable
{
    private String  id;// 499,
    private String  portrait;//\/Uploads\/portrait\/1521947823_20180325111702.jpeg
    private String  nickname;//刘瑛明
    private String  position;//农民
    private boolean isSelected;
    private String  role_type;
    private String  phone;

    public SaleInfo(JSONObject obj)
    {
        this.id = obj.optString("id");
        this.portrait = obj.optString("portrait");
        this.nickname = obj.optString("nickname");
        this.position = obj.optString("position");
        this.phone = obj.optString("mobile");
        this.role_type = obj.optString("role_type");

    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getPortrait()
    {
        return portrait;
    }

    public void setPortrait(String portrait)
    {
        this.portrait = portrait;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public String getPosition()
    {
        return position;
    }

    public void setPosition(String position)
    {
        this.position = position;
    }

    public boolean isSelected()
    {
        return isSelected;
    }

    public void setSelected(boolean selected)
    {
        isSelected = selected;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getRole_type()
    {
        return role_type;
    }

    public void setRole_type(String role_type)
    {
        this.role_type = role_type;
    }
}
