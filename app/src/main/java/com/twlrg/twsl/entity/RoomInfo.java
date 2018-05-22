package com.twlrg.twsl.entity;

import com.twlrg.twsl.utils.StringUtils;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：王先云 on 2018/4/17 20:24
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class RoomInfo implements Serializable
{
    private String id;//4,
    private String merchant_id;//4,
    private String title;//大床房
    private String area;//35 //面积
    private String check_in;//2,//允许入住人数
    private String floor;//7-23 //楼层
    private String add_breakfast;//72 //加餐价格
    private String bed_type;//130cm*200cm*1 床型规格
    private String add_bed;//0,//加床价格
    private String smokeless;//1,//0为无烟房，1为有烟房
    private String wifi;//0,//0为无WIFI，1为有WIFI
    private String window;//1,//0为无窗，1为有窗
    private String pic1;///Uploads/room/4/5902be6137a9c.jpg
    private String pic2;///Uploads/room/4/590164d6443ec.jpg
    private String pic3;///Uploads/room/4/590164ee15e83.jpg
    private String pic4;///Uploads/room/4/590164f641eae.jpg
    private String pic5;///Uploads/room/4/590164fcb36c1.jpg
    private String pic6;///Uploads/room/4/59016503241ce.jpg
    private String status;//0,
    private String isdelete;//0,
    private int price;//200,
    private String price_type;//wz
    private List<String> picList = new ArrayList<>();

    public RoomInfo(){}

    public RoomInfo(JSONObject obj)
    {
        this.id = obj.optString("id");
        this.merchant_id = obj.optString("merchant_id");
        this.title = obj.optString("title");
        this.area = obj.optString("area");
        this.check_in = obj.optString("check_in");
        this.floor = obj.optString("floor");
        this.add_breakfast = obj.optString("add_breakfast");
        this.bed_type = obj.optString("bed_type");
        this.add_bed = obj.optString("add_bed");
        this.smokeless = obj.optString("smokeless");
        this.wifi = obj.optString("wifi");
        this.window = obj.optString("window");
        this.pic1 = obj.optString("pic1");
        this.pic2 = obj.optString("pic2");
        this.pic3 = obj.optString("pic3");
        this.pic4 = obj.optString("pic4");
        this.pic5 = obj.optString("pic5");
        this.pic6 = obj.optString("pic6");
        this.status = obj.optString("status");
        this.isdelete = obj.optString("isdelete");
        this.price_type = obj.optString("price_type");
        this.price=obj.optInt("price");

    }

    public List<String> getPicList()
    {
        if(!StringUtils.stringIsEmpty(pic1))
        {
            picList.add(pic1);
        }
        if(!StringUtils.stringIsEmpty(pic2))
        {
            picList.add(pic2);
        }
        if(!StringUtils.stringIsEmpty(pic3))
        {
            picList.add(pic3);
        }
        if(!StringUtils.stringIsEmpty(pic4))
        {
            picList.add(pic4);
        }
        if(!StringUtils.stringIsEmpty(pic5))
        {
            picList.add(pic5);
        }
        if(!StringUtils.stringIsEmpty(pic6))
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

    public String getCheck_in()
    {
        return check_in;
    }

    public void setCheck_in(String check_in)
    {
        this.check_in = check_in;
    }

    public String getFloor()
    {
        return floor;
    }

    public void setFloor(String floor)
    {
        this.floor = floor;
    }

    public String getAdd_breakfast()
    {
        return add_breakfast;
    }

    public void setAdd_breakfast(String add_breakfast)
    {
        this.add_breakfast = add_breakfast;
    }

    public String getBed_type()
    {
        return bed_type;
    }

    public void setBed_type(String bed_type)
    {
        this.bed_type = bed_type;
    }

    public String getAdd_bed()
    {
        return add_bed;
    }

    public void setAdd_bed(String add_bed)
    {
        this.add_bed = add_bed;
    }

    public String getSmokeless()
    {
        return smokeless;
    }

    public void setSmokeless(String smokeless)
    {
        this.smokeless = smokeless;
    }

    public String getWifi()
    {
        return wifi;
    }

    public void setWifi(String wifi)
    {
        this.wifi = wifi;
    }

    public String getWindow()
    {
        return window;
    }

    public void setWindow(String window)
    {
        this.window = window;
    }

    public String getPic1()
    {
        return pic1;
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

    public int getPrice()
    {
        return price;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }

    public String getPrice_type()
    {
        return price_type;
    }

    public void setPrice_type(String price_type)
    {
        this.price_type = price_type;
    }

    public void setPicList(List<String> picList)
    {
        this.picList = picList;
    }
}
