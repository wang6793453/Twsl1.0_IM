package com.twlrg.twsl.entity;

import com.twlrg.twsl.utils.StringUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：王先云 on 2018/4/17 20:36
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class ConferenceInfo
{
    private String id;//8,
    private String merchant_id;//4,
    private String title;//会议厅1
    private String area;//480,//会场面积
    private String ckg;//30m*16m*9m //会场长宽高规格
    private String floor;//2 //楼层
    private String led;//0,//0为无LED屏，1为有LED屏
    private String theatre;//500,//剧院式容纳人数
    private String desk;//300,//课桌式容纳人数
    private String banquet;//240,//宴会式容纳人数
    private String fishbone;//200,//鱼骨式容纳人数
    private int    price;//30000,//参考价
    private String pic1;///Uploads/place/7/59094aac24d65.jpg
    private String pic2;///Uploads/place/7/59094ab43bb6a.jpg
    private String pic3;///Uploads/place/7/59094abc88590.jpg
    private String pic4;//
    private String pic5;//
    private String pic6;//
    private String status;//0,
    private String isdelete;//0
    private List<String> picList = new ArrayList<>();
    public ConferenceInfo(){}
    public ConferenceInfo(JSONObject obj)
    {
        this.id = obj.optString("id");
        this.merchant_id = obj.optString("merchant_id");
        this.title = obj.optString("title");
        this.area = obj.optString("area");
        this.ckg = obj.optString("ckg");
        this.floor = obj.optString("floor");
        this.led = obj.optString("led");
        this.theatre = obj.optString("theatre");
        this.desk = obj.optString("desk");
        this.banquet = obj.optString("banquet");
        this.fishbone = obj.optString("fishbone");
        this.fishbone = obj.optString("fishbone");
        this.price = obj.optInt("price");
        this.pic1 = obj.optString("pic1");
        this.pic2 = obj.optString("pic2");
        this.pic3 = obj.optString("pic3");
        this.pic4 = obj.optString("pic4");
        this.pic5 = obj.optString("pic5");
        this.pic6 = obj.optString("pic6");
        this.status = obj.optString("status");
        this.isdelete = obj.optString("isdelete");
    }

    public List<String> getPicList()
    {
        if (!StringUtils.stringIsEmpty(pic1))
        {
            picList.add(pic1);
        }
        if (!StringUtils.stringIsEmpty(pic2))
        {
            picList.add(pic2);
        }
        if (!StringUtils.stringIsEmpty(pic3))
        {
            picList.add(pic3);
        }
        if (!StringUtils.stringIsEmpty(pic4))
        {
            picList.add(pic4);
        }
        if (!StringUtils.stringIsEmpty(pic5))
        {
            picList.add(pic5);
        }
        if (!StringUtils.stringIsEmpty(pic6))
        {
            picList.add(pic6);
        }

        return picList;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getMerchant_id()
    {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id)
    {
        this.merchant_id = merchant_id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getArea()
    {
        return area;
    }

    public void setArea(String area)
    {
        this.area = area;
    }

    public String getCkg()
    {
        return ckg;
    }

    public void setCkg(String ckg)
    {
        this.ckg = ckg;
    }

    public String getFloor()
    {
        return floor;
    }

    public void setFloor(String floor)
    {
        this.floor = floor;
    }

    public String getLed()
    {
        return led;
    }

    public void setLed(String led)
    {
        this.led = led;
    }

    public String getTheatre()
    {
        return theatre;
    }

    public void setTheatre(String theatre)
    {
        this.theatre = theatre;
    }

    public String getDesk()
    {
        return desk;
    }

    public void setDesk(String desk)
    {
        this.desk = desk;
    }

    public String getBanquet()
    {
        return banquet;
    }

    public void setBanquet(String banquet)
    {
        this.banquet = banquet;
    }

    public String getFishbone()
    {
        return fishbone;
    }

    public void setFishbone(String fishbone)
    {
        this.fishbone = fishbone;
    }

    public int getPrice()
    {
        return price;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }

    public String getPic1()
    {
        return pic1;
    }

    public void setPicList(List<String> picList)
    {
        this.picList = picList;
    }

    public void setPic1(String pic1)
    {
        this.pic1 = pic1;
    }

    public String getPic2()
    {
        return pic2;
    }

    public void setPic2(String pic2)
    {
        this.pic2 = pic2;
    }

    public String getPic3()
    {
        return pic3;
    }

    public void setPic3(String pic3)
    {
        this.pic3 = pic3;
    }

    public String getPic4()
    {
        return pic4;
    }

    public void setPic4(String pic4)
    {
        this.pic4 = pic4;
    }

    public String getPic5()
    {
        return pic5;
    }

    public void setPic5(String pic5)
    {
        this.pic5 = pic5;
    }

    public String getPic6()
    {
        return pic6;
    }

    public void setPic6(String pic6)
    {
        this.pic6 = pic6;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getIsdelete()
    {
        return isdelete;
    }

    public void setIsdelete(String isdelete)
    {
        this.isdelete = isdelete;
    }
}
