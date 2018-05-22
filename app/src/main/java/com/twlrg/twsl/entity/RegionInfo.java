package com.twlrg.twsl.entity;

import org.json.JSONObject;

/**
 * 作者：王先云 on 2018/4/27 17:05
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class RegionInfo
{
    private String id;// 3,
    private String name;// "东城区"

    public RegionInfo() {}

    public RegionInfo(JSONObject obj)
    {
        this.id = obj.optString("id");
        this.name = obj.optString("name");
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
}
