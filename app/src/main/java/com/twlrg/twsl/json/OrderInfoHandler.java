package com.twlrg.twsl.json;


import com.twlrg.twsl.entity.OrderInfo;

import org.json.JSONObject;


/**
 */
public class OrderInfoHandler extends JsonHandler
{

    private OrderInfo orderInfo;

    public OrderInfo getOrderInfo()
    {
        return orderInfo;
    }

    @Override
    protected void parseJson(JSONObject jsonObj) throws Exception
    {
        try
        {
            orderInfo = new OrderInfo(jsonObj.optJSONObject("data"));

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
