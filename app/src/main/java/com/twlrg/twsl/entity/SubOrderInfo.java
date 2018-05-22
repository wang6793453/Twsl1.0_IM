package com.twlrg.twsl.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：王先云 on 2018/4/24 21:03
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class SubOrderInfo implements Serializable
{
    private String hotelName;
    private String occupant;
    private String phone;
    private String s_data;
    private String e_data;
    private String buynum;
    private String days;
    private String cancel_policy;
    private String roomTitle;
    private String order_id;
    private String total_feel;
    private String  merchant_id;

    private List<SaleInfo> saleInfoList = new ArrayList<>();


    public String getHotelName()
    {
        return hotelName;
    }

    public void setHotelName(String hotelName)
    {
        this.hotelName = hotelName;
    }

    public String getOccupant()
    {
        return occupant;
    }

    public void setOccupant(String occupant)
    {
        this.occupant = occupant;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getS_data()
    {
        return s_data;
    }

    public void setS_data(String s_data)
    {
        this.s_data = s_data;
    }

    public String getE_data()
    {
        return e_data;
    }

    public void setE_data(String e_data)
    {
        this.e_data = e_data;
    }

    public String getBuynum()
    {
        return buynum;
    }

    public void setBuynum(String buynum)
    {
        this.buynum = buynum;
    }

    public String getDays()
    {
        return days;
    }

    public void setDays(String days)
    {
        this.days = days;
    }

    public String getCancel_policy()
    {
        return cancel_policy;
    }

    public void setCancel_policy(String cancel_policy)
    {
        this.cancel_policy = cancel_policy;
    }

    public List<SaleInfo> getSaleInfoList()
    {
        return saleInfoList;
    }

    public void setSaleInfoList(List<SaleInfo> saleInfoList)
    {
        this.saleInfoList = saleInfoList;
    }

    public String getRoomTitle()
    {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle)
    {
        this.roomTitle = roomTitle;
    }

    public String getOrder_id()
    {
        return order_id;
    }

    public void setOrder_id(String order_id)
    {
        this.order_id = order_id;
    }

    public String getTotal_feel()
    {
        return total_feel;
    }

    public void setTotal_feel(String total_feel)
    {
        this.total_feel = total_feel;
    }

    public String getMerchant_id()
    {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id)
    {
        this.merchant_id = merchant_id;
    }
}
