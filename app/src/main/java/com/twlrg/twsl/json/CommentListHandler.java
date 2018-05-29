package com.twlrg.twsl.json;


import com.twlrg.twsl.entity.CommentInfo;
import com.twlrg.twsl.entity.ReplyInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class CommentListHandler extends JsonHandler
{
    private List<CommentInfo> commentInfoList = new ArrayList<>();

    public List<CommentInfo> getCommentInfoList()
    {
        return commentInfoList;
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
                    CommentInfo mCommentInfo = new CommentInfo(arr.optJSONObject(i));


                    JSONArray subArr = arr.optJSONObject(i).getJSONArray("sub_data");

                    List<ReplyInfo> replyInfoList = new ArrayList<>();

                    if (null != subArr)
                    {
                        for (int j = 0; j < subArr.length(); j++)
                        {
                            ReplyInfo mReplyInfo = new ReplyInfo(subArr.getJSONObject(j));
                            replyInfoList.add(mReplyInfo);
                        }
                    }
                    mCommentInfo.setReplyInfoList(replyInfoList);
                    commentInfoList.add(mCommentInfo);
                }
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
