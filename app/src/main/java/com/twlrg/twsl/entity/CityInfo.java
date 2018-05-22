package com.twlrg.twsl.entity;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.twlrg.twsl.widget.sidebar.PinYinUtil;

import org.json.JSONObject;
import java.io.Serializable;

/**
 * 作者：王先云 on 2018/4/25 15:32
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class CityInfo implements Comparable<CityInfo>,Serializable
{
    private String id;
    private String name;
    private String mPinyin;
    public  CityInfo(JSONObject obj)
    {
        this.id = obj.optString("id");
        this.name=obj.optString("name");
        mPinyin = PinYinUtil.toPinyin(name);
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

    public String getPinyin()
    {
        return mPinyin;
    }

    public void setmPinyin(String mPinyin)
    {
        this.mPinyin = mPinyin;
    }

    @Override
    public int compareTo(@NonNull CityInfo mCityInfo) {
        String name = mCityInfo.getName();
        String pinyin = mCityInfo.getPinyin();
        if (TextUtils.isEmpty(mPinyin)) {
            return name.compareToIgnoreCase(TextUtils.isEmpty(pinyin) ? name : pinyin);
        } else {
            return mPinyin.compareToIgnoreCase(TextUtils.isEmpty(pinyin) ? name : pinyin);
        }
    }
}
