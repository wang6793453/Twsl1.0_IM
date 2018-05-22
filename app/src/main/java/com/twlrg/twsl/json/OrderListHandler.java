package com.twlrg.twsl.json;


import com.twlrg.twsl.entity.OrderInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class OrderListHandler extends JsonHandler
{

    private List<OrderInfo> orderInfoList = new ArrayList<>();

    public List<OrderInfo> getOrderInfoList()
    {
        return orderInfoList;
    }

    @Override
    protected void parseJson(JSONObject jsonObj) throws Exception
    {
        try
        {
            JSONArray arr = jsonObj.optJSONArray("data");


            if (null != arr)
            {
                for (int i = 0; i < arr.length(); i++)
                {
                    OrderInfo orderInfo = new OrderInfo(arr.optJSONObject(i));
                    orderInfoList.add(orderInfo);
                }
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
