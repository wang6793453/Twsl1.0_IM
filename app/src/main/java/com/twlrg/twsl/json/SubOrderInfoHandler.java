package com.twlrg.twsl.json;


import com.twlrg.twsl.entity.SaleInfo;
import com.twlrg.twsl.entity.SubOrderInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class SubOrderInfoHandler extends JsonHandler
{

    private SubOrderInfo subOrderInfo;

    public SubOrderInfo getSubOrderInfo()
    {
        return subOrderInfo;
    }

    @Override
    protected void parseJson(JSONObject jsonObj) throws Exception
    {
        try
        {
            subOrderInfo = new SubOrderInfo();

            JSONObject obj = jsonObj.optJSONObject("data");
            if (null != obj)
            {
                JSONArray arrDay = obj.optJSONArray("room_price_info");
                JSONArray arrSale = obj.optJSONArray("sale");
                subOrderInfo.setOrder_id(obj.optString("order_id"));
                subOrderInfo.setTotal_feel(obj.optString("total_fee"));
                if (null != arrDay)
                {
                    subOrderInfo.setDays(arrDay.length() + "");
                }
                subOrderInfo.setCancel_policy(obj.optString("cancel_policy"));


                List<SaleInfo> saleInfoList = new ArrayList<>();
                if(null !=arrSale)
                {
                    for (int i = 0; i <arrSale.length() ; i++)
                    {
                        SaleInfo mSaleInfo = new SaleInfo(arrSale.optJSONObject(i));
                        if(i==0)
                        {
                            mSaleInfo.setSelected(true);
                        }
                        else
                        {
                            mSaleInfo.setSelected(false);
                        }
                        saleInfoList.add(mSaleInfo);
                    }
                }
                subOrderInfo.setSaleInfoList(saleInfoList);
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
