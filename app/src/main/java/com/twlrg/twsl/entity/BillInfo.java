package com.twlrg.twsl.entity;

import org.json.JSONObject;

/**
 * 作者：王先云 on 2018/5/15 22:28
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class BillInfo
{


    private String startDate;
    private String endDate;
    private String totalIncome;
    private String status;

    public BillInfo() {}

    public BillInfo(JSONObject obj)
    {
        this.startDate = obj.optString("s_date");
        this.endDate = obj.optString("e_date");
        this.totalIncome = obj.optString("total");
    }


    public String getStartDate()
    {
        return startDate;
    }

    public void setStartDate(String startDate)
    {
        this.startDate = startDate;
    }

    public String getEndDate()
    {
        return endDate;
    }

    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }

    public String getTotalIncome()
    {
        return totalIncome;
    }

    public void setTotalIncome(String totalIncome)
    {
        this.totalIncome = totalIncome;
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
