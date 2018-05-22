package com.twlrg.twsl.widget;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.twlrg.twsl.R;
import com.twlrg.twsl.adapter.FilterAdapter;
import com.twlrg.twsl.entity.FilterInfo;
import com.twlrg.twsl.listener.MyItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 条件筛选
 */
public class FilterPopupWindow extends PopupWindow implements PopupWindow.OnDismissListener
{
    View rootView;
    private NoScrollListView mListView;
    private Activity         mContext;
    private FilterAdapter    mAdapter;

    private MyItemClickListener listener;
    private int                 mHeight;
    List<FilterInfo> mFilterList = new ArrayList<>();

    public FilterPopupWindow(Activity context, List<FilterInfo> mFilterList, MyItemClickListener listener)
    {
        super(context);
        this.mContext = context;
        this.listener = listener;
        this.mFilterList = mFilterList;
        rootView = LayoutInflater.from(context).inflate(R.layout.pop_list, null);
        setContentView(rootView);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable());
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        initView();
        initEvent();
        // WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        //lp.alpha = 0.5f; //0.0-1.0
        //mContext.getWindow().setAttributes(lp);


        mAdapter = new FilterAdapter(mFilterList, mContext);
        mListView.setAdapter(mAdapter);
        setListViewHeightBasedOnChildren(mListView);
        mAdapter.notifyDataSetChanged();

    }


    private void initView()
    {
        mListView = (NoScrollListView) rootView.findViewById(R.id.lv_choice);

    }

    private void initEvent()
    {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                listener.onItemClick(view, position);

                for (int i = 0; i < mFilterList.size(); i++)
                {
                    if (i == position)
                    {
                        mFilterList.get(i).setSelected(true);
                    }
                    else
                    {
                        mFilterList.get(i).setSelected(false);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    public void show(View view)
    {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1] - mHeight);

    }
    //
    //    public void setData(List<FilterInfo> list)
    //    {
    //        mFilterList.clear();
    //        mFilterList.addAll(list);
    //        mAdapter = new FilterAdapter(mFilterList, mContext);
    //        mListView.setAdapter(mAdapter);
    //        setListViewHeightBasedOnChildren(mListView);
    //        mAdapter.notifyDataSetChanged();
    //    }

    public void setListViewHeightBasedOnChildren(ListView listView)
    {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
        {
            return;
        }

        int totalHeight = 0;


        for (int i = 0; i < listAdapter.getCount(); i++)
        {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        ((ViewGroup.MarginLayoutParams) params).setMargins(0, 0, 0, 0);
        listView.setLayoutParams(params);
        mHeight = params.height + 50;
    }

    @Override
    public void onDismiss()
    {
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = 1.0f; //0.0-1.0
        mContext.getWindow().setAttributes(lp);
    }
}
