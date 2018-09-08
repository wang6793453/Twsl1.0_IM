package com.twlrg.twsl.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.twlrg.twsl.R;
import com.twlrg.twsl.activity.RoomPriceDetailActivity;
import com.twlrg.twsl.adapter.CategoryAdapter;
import com.twlrg.twsl.adapter.PriceDetailAdapter;
import com.twlrg.twsl.entity.OrderInfo;
import com.twlrg.twsl.listener.MyItemClickListener;
import com.twlrg.twsl.listener.MyOnClickListener;
import com.twlrg.twsl.widget.EmptyDecoration;
import com.twlrg.twsl.widget.FullyLinearLayoutManager;
import com.twlrg.twsl.widget.NoScrollListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：王先云 on 2017/12/1 10:07
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class DialogUtils
{
    //    //性别选择
    //    public static Dialog showSexDialog(final Context mContext, final OnClickListenerUtils.OnSubmitBtnClickListerner listerner)
    //    {
    //        final Dialog dialog = new Dialog(mContext, R.style.DialogStyle);
    //        dialog.setCancelable(false);
    //        final View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_sex, null);
    //        dialog.setContentView(view);
    //
    //        LinearLayout mManLayout = (LinearLayout) view.findViewById(R.id.ll_man);
    //        LinearLayout mWomenLayout = (LinearLayout) view.findViewById(R.id.ll_women);
    //
    //        mManLayout.setOnClickListener(new View.OnClickListener()
    //        {
    //            @Override
    //            public void onClick(View v)
    //            {
    //                listerner.onSubmit("0");
    //                dialog.dismiss();
    //            }
    //        });
    //
    //        mWomenLayout.setOnClickListener(new View.OnClickListener()
    //        {
    //            @Override
    //            public void onClick(View v)
    //            {
    //                listerner.onSubmit("1");
    //                dialog.dismiss();
    //            }
    //        });
    //        Window mWindow = dialog.getWindow();
    //        WindowManager.LayoutParams lp = mWindow.getAttributes();
    //        DisplayMetrics dm = new DisplayMetrics();
    //        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
    //        lp.width = dm.widthPixels * 3 / 4;
    //        mWindow.setGravity(Gravity.CENTER);
    //        //        mWindow.setWindowAnimations(R.style.dialogAnim);
    //        mWindow.setAttributes(lp);
    //        dialog.setCancelable(true);
    //        dialog.show();
    //        return dialog;
    //    }


    public static Dialog showCategoryDialog(Context mContext, List<String> list, final MyItemClickListener mMyItemClickListener)
    {
        final Dialog dialog = new Dialog(mContext, R.style.dialogNoAnimation);
        dialog.setCancelable(false);
        final View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_category, null);
        dialog.setContentView(view);

        NoScrollListView mListView = (NoScrollListView) view.findViewById(R.id.lv_type);
        CategoryAdapter mCategoryAdapter = new CategoryAdapter(mContext, list);
        mListView.setAdapter(mCategoryAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                mMyItemClickListener.onItemClick(view, position);
                dialog.dismiss();
            }
        });

        //Dialog部分
        Window mWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        lp.width = (int) (dm.widthPixels * 0.75);
        mWindow.setGravity(Gravity.CENTER);
        mWindow.setAttributes(lp);
        dialog.setCancelable(true);
        dialog.show();
        return dialog;
    }

    public static void showVersionUpdateDialog(Context mContext, String content, final MyOnClickListener.OnSubmitListener listener)
    {
        final Dialog dialog = new Dialog(mContext, R.style.dialogNoAnimation);
        dialog.setCancelable(false);
        final View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_version, null);
        dialog.setContentView(view);
        TextView mContent = (TextView) view.findViewById(R.id.tv_content);
        Button mSubmitBtn = (Button) view.findViewById(R.id.btn_submit);
        Button mCancelBtn = (Button) view.findViewById(R.id.btn_cancel);

        mContent.setText(content);
        mSubmitBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                dialog.dismiss();
                listener.onSubmit("1");
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                listener.onSubmit("2");
            }
        });
        //Dialog部分
        Window mWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        lp.width = (int) (dm.widthPixels * 0.75);
        mWindow.setGravity(Gravity.CENTER);
        mWindow.setAttributes(lp);
        dialog.setCancelable(false);
        dialog.show();
    }


    /**
     * 提示框
     *
     * @return
     */
    public static void showPromptDialog(Context mContext, String title, final MyItemClickListener listener)
    {
        final Dialog dialog = new Dialog(mContext, R.style.dialogNoAnimation);
        dialog.setCancelable(true);
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_prompt, null);
        dialog.setContentView(v);
        ((TextView) v.findViewById(R.id.tv_content)).setText(title);
        v.findViewById(R.id.tv_submit).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onItemClick(v, 0);
                dialog.dismiss();
            }
        });

        //Dialog部分
        Window mWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = APPUtils.getScreenWidth(mContext) * 3 / 4;
        mWindow.setAttributes(lp);
        dialog.setCancelable(false);
        dialog.show();
    }


    public static Dialog createLoadingDialog(Context context, String msg)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loading1, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v
                .findViewById(R.id.dialog_loading_view);// 加载布局
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, R.style.MyDialogStyle);// 创建自定义样式dialog
        loadingDialog.setCancelable(true); // 是否可以按“返回键”消失
        loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        /**
         *将显示Dialog的方法封装在这里面
         */
        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        return loadingDialog;
    }

    /**
     * 温馨提示
     *
     * @return
     */
    public static void showToastDialog2Button(Context mContext, String str, final View.OnClickListener onClickListener)
    {
        final Dialog dialog = new Dialog(mContext, R.style.dialogNoAnimation);
        dialog.setCancelable(false);
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_toast_2_button, null);
        dialog.setContentView(v);
        TextView mTitle = (TextView) v.findViewById(R.id.tv_dialog_title);
        mTitle.setText(str);
        ((RelativeLayout) v.findViewById(R.id.rl_confirm)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                onClickListener.onClick(v);
            }
        });

        v.findViewById(R.id.rl_cancel).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        //Dialog部分
        Window mWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = APPUtils.getScreenWidth(mContext) * 7 / 8;
        mWindow.setAttributes(lp);
        dialog.show();
    }

    /**
     * 温馨提示
     *
     * @return
     */
    public static Dialog showToastDialog2Button(Context mContext, String str, final View.OnClickListener onClickListener, final View.OnClickListener
            onClickListener1)
    {
        final Dialog dialog = new Dialog(mContext, R.style.dialogNoAnimation);
        dialog.setCancelable(false);
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_toast_2_button, null);
        dialog.setContentView(v);
        TextView mTitle = (TextView) v.findViewById(R.id.tv_dialog_title);
        mTitle.setText(str);

        RelativeLayout mConfirmLayout = (RelativeLayout) v.findViewById(R.id.rl_confirm);

        mConfirmLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                onClickListener.onClick(v);
            }
        });


        RelativeLayout mCancelLayout = (RelativeLayout) v.findViewById(R.id.rl_cancel);

        mCancelLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                onClickListener1.onClick(v);
            }
        });
        //Dialog部分
        Window mWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = APPUtils.getScreenWidth(mContext) * 7 / 8;
        mWindow.setAttributes(lp);
        return dialog;
    }

    /**
     * 房间数
     *
     * @return
     */
    public static void showRoomCountDialog(final Context mContext, final MyOnClickListener.OnSubmitListener listener)
    {
        final Dialog dialog = new Dialog(mContext, R.style.dialogNoAnimation);
        dialog.setCancelable(false);
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_room_count, null);
        dialog.setContentView(v);
        final EditText etRoom = (EditText) v.findViewById(R.id.et_room);
        ((RelativeLayout) v.findViewById(R.id.rl_confirm)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                String roomCount = etRoom.getText().toString();

                if (StringUtils.stringIsEmpty(roomCount))
                {
                    ToastUtil.show(mContext, "请输入房间数");
                    return;
                }
                if (Integer.parseInt(roomCount) <= 10)
                {
                    ToastUtil.show(mContext, "请输入房间数大于10");
                    return;
                }
                listener.onSubmit(roomCount);
                dialog.dismiss();
            }
        });

        v.findViewById(R.id.rl_cancel).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        //Dialog部分
        Window mWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = APPUtils.getScreenWidth(mContext) * 7 / 8;
        mWindow.setAttributes(lp);
        dialog.show();
    }


    /**
     * 价格
     *
     * @return
     */
    public static void showPriceDetailDialog(final Context mContext, List<OrderInfo> orderInfoList)
    {
        final Dialog dialog = new Dialog(mContext);
        dialog.setCancelable(true);
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_order_detail, null);
        dialog.setContentView(v);

        TextView tvDays = (TextView) v.findViewById(R.id.tv_days);
        TextView tvPrice = (TextView) v.findViewById(R.id.tv_price);
        TextView tvTotalPrice = (TextView) v.findViewById(R.id.tv_total_price);
        RecyclerView mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new FullyLinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new EmptyDecoration(mContext, ""));
        mRecyclerView.setAdapter(new PriceDetailAdapter(orderInfoList));
        tvDays.setText(orderInfoList.size() + "晚总价");
        int allPrice = 0;

        for (int i = 0; i < orderInfoList.size(); i++)
        {
            allPrice += Integer.parseInt(orderInfoList.get(i).getPrice()) * Integer.parseInt(orderInfoList.get(i).getBuynum());
        }

        tvPrice.setText("￥" + allPrice);
        tvTotalPrice.setText("￥" + allPrice);

        v.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        //Dialog部分
        Window mWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.alpha = 0.7f;
        mWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWindow.setAttributes(lp);
        dialog.show();


    }


    /**
     * 价格
     *
     * @return
     */
    public static void showPayDialog(final Context mContext, final MyItemClickListener listener)
    {
        final Dialog dialog = new Dialog(mContext);
        dialog.setCancelable(true);
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_pay, null);
        dialog.setContentView(v);


        v.findViewById(R.id.rl_wechat).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onItemClick(v, 0);
                dialog.dismiss();
            }
        });


        v.findViewById(R.id.rl_alipay).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onItemClick(v, 1);
                dialog.dismiss();
            }
        });

        v.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        //Dialog部分
        Window mWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        mWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mWindow.setAttributes(lp);
        dialog.show();
    }

    /**
     * @return
     */
    public static void showRoomPriceDialog(String startTime, String endTime, final RoomPriceDetailActivity mRoomPriceDetailActivity, final
    MyOnClickListener.OnSubmitListener listener)
    {

        int days = StringUtils.differentDaysByMillisecond(startTime, endTime);

        final Dialog dialog = new Dialog(mRoomPriceDetailActivity);
        dialog.setCancelable(true);
        View v = LayoutInflater.from(mRoomPriceDetailActivity).inflate(R.layout.dialog_modify_room_price_list, null);
        dialog.setContentView(v);

        TextView mStartTimeTv = (TextView) v.findViewById(R.id.tv_start_date);
        TextView mEndTimeTv = (TextView) v.findViewById(R.id.tv_end_date);
        TextView mWeekTv1 = (TextView) v.findViewById(R.id.tv_week_1);
        TextView mWeekTv2 = (TextView) v.findViewById(R.id.tv_week_2);
        TextView mWeekTv3 = (TextView) v.findViewById(R.id.tv_week_3);
        TextView mWeekTv4 = (TextView) v.findViewById(R.id.tv_week_4);
        TextView mWeekTv5 = (TextView) v.findViewById(R.id.tv_week_5);
        TextView mWeekTv6 = (TextView) v.findViewById(R.id.tv_week_6);
        TextView mWeekTv7 = (TextView) v.findViewById(R.id.tv_week_7);

        EditText mWzPriceEt = (EditText) v.findViewById(R.id.et_wz);
        EditText mDzPriceEt = (EditText) v.findViewById(R.id.et_dz);
        EditText mSzPriceEt = (EditText) v.findViewById(R.id.et_sz);
        LinearLayout mWeekLayout = (LinearLayout) v.findViewById(R.id.ll_week);


        mStartTimeTv.setText("开始时间:" + startTime);
        mEndTimeTv.setText("结束时间:" + endTime);

        if (days < 2)
        {
            mWeekLayout.setVisibility(View.GONE);
        }
        else
        {
            mWeekLayout.setVisibility(View.VISIBLE);
        }


        final List<TextView> mWeekViewList = new ArrayList<>();

        mWeekViewList.add(mWeekTv1);
        mWeekViewList.add(mWeekTv2);
        mWeekViewList.add(mWeekTv3);
        mWeekViewList.add(mWeekTv4);
        mWeekViewList.add(mWeekTv5);
        mWeekViewList.add(mWeekTv6);
        mWeekViewList.add(mWeekTv7);

        for (int i = 0; i < mWeekViewList.size(); i++)
        {
            final TextView mWeekView = mWeekViewList.get(i);
            mWeekViewList.get(i).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (mWeekView.isSelected())
                    {
                        mWeekView.setSelected(false);
                    }
                    else
                    {
                        mWeekView.setSelected(true);
                    }
                }
            });
        }


        v.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onSubmit("");
                dialog.dismiss();
            }
        });

        //Dialog部分
        Window mWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        mWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mWindow.setAttributes(lp);
        dialog.show();
    }


    /**
     * 价格
     *
     * @return
     */
    public static void showReplyDialog(final Context mContext, final MyOnClickListener.OnSubmitListener listener)
    {
        final Dialog dialog = new Dialog(mContext);
        dialog.setCancelable(true);
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_reply, null);
        dialog.setContentView(v);

        final EditText mContentEt = (EditText) v.findViewById(R.id.et_content);

        v.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String content = mContentEt.getText().toString();

                if (StringUtils.stringIsEmpty(content))
                {
                    ToastUtil.show(mContext, "请输入回复内容");
                    return;
                }

                listener.onSubmit(content);
                dialog.dismiss();
            }
        });


        //Dialog部分
        Window mWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        mWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mWindow.setAttributes(lp);
        dialog.show();
    }


    /**
     * 价格
     *
     * @return
     */
    public static void showShareDialog(final Context mContext, final MyOnClickListener.OnSubmitListener listener)
    {
        final Dialog dialog = new Dialog(mContext);
        dialog.setCancelable(true);
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_share, null);
        dialog.setContentView(v);


        v.findViewById(R.id.ll_wx).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                listener.onSubmit("0");
                dialog.dismiss();
            }
        });
        v.findViewById(R.id.ll_pyq).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                listener.onSubmit("1");
                dialog.dismiss();
            }
        });

        //Dialog部分
        Window mWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        mWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mWindow.setAttributes(lp);
        dialog.show();
    }
}
