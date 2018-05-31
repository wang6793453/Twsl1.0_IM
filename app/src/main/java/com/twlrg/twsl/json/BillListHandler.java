package com.twlrg.twsl.json;


import com.twlrg.twsl.entity.BillInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class BillListHandler extends JsonHandler
{
    private List<BillInfo> billInfoList = new ArrayList<>();

    public List<BillInfo> getBillInfoList()
    {
        return billInfoList;
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
                    BillInfo mBillInfo = new BillInfo(arr.optJSONObject(i));
                    billInfoList.add(mBillInfo);
                }
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
