package com.twlrg.twsl.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.OrderInfo;
import com.twlrg.twsl.listener.MyOnClickListener;


/**
 * Date:
 */
public class OrderPriceHolder extends RecyclerView.ViewHolder
{


    private TextView                             mDateTv;
    private EditText                             mPriceEt;
    private TextView                             mPriceTypeTv;
    private MyOnClickListener.OnEditCallBackListener listener;
    private Context                              mContext;

    public OrderPriceHolder(View rootView, Context mContext, MyOnClickListener.OnEditCallBackListener listener)
    {
        super(rootView);
        mDateTv = (TextView) rootView.findViewById(R.id.tv_date);
        mPriceEt = (EditText) rootView.findViewById(R.id.et_price);
        mPriceTypeTv = (TextView) rootView.findViewById(R.id.tv_price_type);
        this.listener = listener;
        this.mContext = mContext;
    }


    public void setOrderInfo(final OrderInfo mOrderInfo)
    {
        mDateTv.setText(mOrderInfo.getDate());
        mPriceEt.setText(mOrderInfo.getPrice());
        mPriceEt.setEnabled(mOrderInfo.isPriceModify());
        mPriceTypeTv.setText(getZc(mOrderInfo.getPrice_type()));

        mPriceEt.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (s.length() == 0)
                {

                    if (!"0".equals(mOrderInfo.getPrice()))
                    {
                        listener.onSubmit(mOrderInfo.getId(), "0");
                    }
                }
                else
                {
                    if (!s.toString().equals(mOrderInfo.getPrice()))
                    {
                        listener.onSubmit(mOrderInfo.getId(), s.toString());
                    }

                }

                mPriceEt.setSelection(s.length());
            }
        });
    }

    private String getZc(String price_type)
    {
        String zc = "无早";
        if ("wz".equals(price_type))
        {
            zc = "无早";
        }
        else if ("dz".equals(price_type))
        {
            zc = "单早";
        }
        else if ("sz".equals(price_type))
        {
            zc = "双早";
        }

        return zc;
    }
}
