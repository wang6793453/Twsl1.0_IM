package com.twlrg.twsl.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 作者：王先云 on 2018/4/17 21:57
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class ObservableScrollView extends ScrollView
{
    private OnScrollListener mOnScrollListener;

    public ObservableScrollView(Context context)
    {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 监听ScroView的滑动情况
     *
     * @param l    变化后的X轴位置
     * @param t    变化后的Y轴的位置
     * @param oldl 原先的X轴的位置
     * @param oldt 原先的Y轴的位置
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        super.onScrollChanged(l, t, oldl, oldt);
        if (null != mOnScrollListener)
        {
            mOnScrollListener.onScroll(t);
        }
    }


    /**
     * 设置滚动接口
     *
     * @param listener
     */
    public void setOnScrollListener(OnScrollListener listener)
    {
        this.mOnScrollListener = listener;
    }

    /**
     * 滚动的回调接口
     */
    public interface OnScrollListener
    {
        /**
         * MyScrollView滑动的Y方向距离变化时的回调方法
         *
         * @param scrollY
         */
        void onScroll(int scrollY);

    }
}
