package com.twlrg.twsl.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.twlrg.twsl.R;


/**
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class SelectMapPopupWindow extends PopupWindow implements View.OnClickListener
{

    private TextView mOpenGaodeTv, mOpenBaiudTv, cancelBtn;
    private View               mMenuView;
    private PopupWindow        popupWindow;
    private OnSelectedListener mOnSelectedListener;

    public SelectMapPopupWindow(Context context, OnSelectedListener mOnSelectedListener)
    {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pop_selector_map, null);

        mOpenGaodeTv = (TextView) mMenuView.findViewById(R.id.openGaode);
        mOpenBaiudTv = (TextView) mMenuView.findViewById(R.id.openBaidu);
        cancelBtn = (TextView) mMenuView.findViewById(R.id.cancel);
        // 设置按钮监听
        mOpenGaodeTv.setOnClickListener(this);
        mOpenBaiudTv.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        this.mOnSelectedListener = mOnSelectedListener;

    }

    /**
     * 把一个View控件添加到PopupWindow上并且显示
     *
     * @param activity
     */
    public void showPopupWindow(Activity activity)
    {
        popupWindow = new PopupWindow(mMenuView,    // 添加到popupWindow
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // ☆ 注意： 必须要设置背景，播放动画有一个前提 就是窗体必须有背景
        // popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER | Gravity.BOTTOM, 0, 0);
        popupWindow.setAnimationStyle(android.R.style.Animation_InputMethod);   // 设置窗口显示的动画效果
        popupWindow.setFocusable(false);                                        // 点击其他地方隐藏键盘 popupWindow
        popupWindow.update();
    }

    /**
     * 移除PopupWindow
     */
    public void dismissPopupWindow()
    {
        if (popupWindow != null && popupWindow.isShowing())
        {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.openGaode:
                if (null != mOnSelectedListener)
                {
                    mOnSelectedListener.OnSelected(v, 0);
                }
                break;
            case R.id.openBaidu:
                if (null != mOnSelectedListener)
                {
                    mOnSelectedListener.OnSelected(v, 1);
                }
                break;
            case R.id.cancel:
                if (null != mOnSelectedListener)
                {
                    mOnSelectedListener.OnSelected(v, 2);
                }
                break;
        }
    }


    /**
     * 选择监听接口
     */
    public interface OnSelectedListener
    {
        void OnSelected(View v, int position);
    }

}
