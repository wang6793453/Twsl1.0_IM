package com.twlrg.twsl.entity;

import org.json.JSONObject;

/**
 * 作者：王先云 on 2018/5/29 13:20
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class FacilitiesInfo
{
    private String id;
    private String pid;
    private String name;
    private int    isSelected;

    public FacilitiesInfo(JSONObject obj)
    {
        this.id = obj.optString("id");
        this.name = obj.optString("title");
        this.isSelected = obj.optInt("selected");
        this.pid = obj.optString("pid");
    }

    public String getPid()
    {
        return pid;
    }

    public void setPid(String pid)
    {
        this.pid = pid;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getIsSelected()
    {
        return isSelected;
    }

    public void setIsSelected(int isSelected)
    {
        this.isSelected = isSelected;
    }
}
