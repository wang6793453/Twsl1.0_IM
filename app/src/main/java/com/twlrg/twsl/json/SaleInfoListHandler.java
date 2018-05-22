package com.twlrg.twsl.json;


import com.twlrg.twsl.entity.SaleInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class SaleInfoListHandler extends JsonHandler
{

    private List<SaleInfo> saleInfoList = new ArrayList<>();

    public List<SaleInfo> getSaleInfoList()
    {
        return saleInfoList;
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
                    SaleInfo saleInfo = new SaleInfo(arr.optJSONObject(i));
                    //                    if(i == 0)
                    //                    {
                    //                        saleInfo.setSelected(true);
                    //                    }
                    //                    else
                    //                    {
                    //                        saleInfo.setSelected(false);
                    //                    }
                    saleInfoList.add(saleInfo);
                }
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
