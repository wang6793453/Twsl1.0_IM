package com.twlrg.twsl.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.imsdk.TIMConversationType;
import com.twlrg.twsl.MyApplication;
import com.twlrg.twsl.R;
import com.twlrg.twsl.activity.LoginActivity;
import com.twlrg.twsl.entity.SaleInfo;
import com.twlrg.twsl.im.ui.ChatActivity;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.StringUtils;
import com.twlrg.twsl.utils.ToastUtil;
import com.twlrg.twsl.utils.Urls;


/**
 * Date:
 */
public class HotelSaleHolder extends RecyclerView.ViewHolder
{
    private ImageView mUserPicIv;
    private TextView  mUserNameTv;
    private TextView  mPositionTv;
    private ImageView mPhoneIv;
    private ImageView mMessageIv;
    private TextView  mRoleTypetTv;
    private Context   mContext;

    public HotelSaleHolder(View rootView, Context mContext)
    {
        super(rootView);
        this.mContext = mContext;
        mUserPicIv = (ImageView) rootView.findViewById(R.id.iv_user_head);
        mUserNameTv = (TextView) rootView.findViewById(R.id.tv_salesperson);
        mPositionTv = (TextView) rootView.findViewById(R.id.tv_position);
        mPhoneIv = (ImageView) rootView.findViewById(R.id.iv_phone);
        mMessageIv = (ImageView) rootView.findViewById(R.id.iv_message);
        mRoleTypetTv = (TextView) rootView.findViewById(R.id.tv_role_type);
    }


    public void setSaleInfo(final SaleInfo mSaleInfo)
    {
        ImageLoader.getInstance().displayImage(Urls.getImgUrl(mSaleInfo.getPortrait()), mUserPicIv);
        mUserNameTv.setText(mSaleInfo.getNickname());
        mPositionTv.setText(mSaleInfo.getPosition());

        mRoleTypetTv.setText(getRoleType(mSaleInfo.getRole_type()));

        mPhoneIv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (null != mSaleInfo && !StringUtils.stringIsEmpty(mSaleInfo.getPhone()))
                {
                    APPUtils.callPhone(mContext, mSaleInfo.getPhone());
                }
                else
                {
                    ToastUtil.show(mContext, "暂时无法电话联系销售人员,请尝试其他方式");
                }
            }
        });


        mMessageIv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (MyApplication.getInstance().isLogin())
                {
                    ChatActivity.navToChat(mContext, "slbl_serve_" + mSaleInfo.getId(), TIMConversationType.C2C);
                }
                else
                {
                    LoginActivity.start(mContext, true);
                }
            }
        });
    }

    private String getRoleType(String type)
    {


        if (StringUtils.stringIsEmpty(type))
        {
            return "";
        }
        StringBuffer sb = new StringBuffer();

        String[] typeArr = type.split(",");
        for (int i = 0; i < typeArr.length; i++)
        {
            if (!StringUtils.stringIsEmpty(typeArr[i]))
            {
                sb.append(getbusiness(Integer.parseInt(typeArr[i])));
                sb.append("  ");
            }
        }

        return sb.toString();

    }

    private String getbusiness(int p)
    {
        switch (p)
        {
            case 1:
                return "散房";
            case 2:
                return "团房";
            case 3:
                return "宴会";
            case 4:
                return "会议";

        }

        return "散房";
    }

}
