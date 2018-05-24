package com.twlrg.twsl.widget.calendar;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twlrg.twsl.R;
import com.twlrg.twsl.activity.HotelTimeActivity;
import com.twlrg.twsl.utils.StringUtils;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 */
public class DayTimeAdapter extends RecyclerView.Adapter<DayTimeViewHolder>
{

    private ArrayList<DayTimeEntity> days;
    private Context                  context;

    public DayTimeAdapter(ArrayList<DayTimeEntity> days, Context context)
    {
        this.days = days;
        this.context = context;

    }

    @Override
    public DayTimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        DayTimeViewHolder ret = null;
        // 不需要检查是否复用，因为只要进入此方法，必然没有复用
        // 因为RecyclerView 通过Holder检查复用
        View v = LayoutInflater.from(context).inflate(R.layout.item_recycler_selectday, parent, false);
        ret = new DayTimeViewHolder(v);

        return ret;
    }

    @Override
    public void onBindViewHolder(final DayTimeViewHolder holder, final int position)
    {
        final DayTimeEntity dayTimeEntity = days.get(position);

        String str1 = StringUtils.getCurrentTime();
        String str2 = dayTimeEntity.getYear() + "-" + dayTimeEntity.getMonth() + "-" + dayTimeEntity.getDay();

        //显示日期
        if (dayTimeEntity.getDay() != 0)
        {

            holder.select_txt_day.setText(dayTimeEntity.getDay() + "");


        }
        else
        {
            holder.select_ly_day.setEnabled(false);
            holder.select_txt_day.setTextColor(ContextCompat.getColor(context, R.color.grayA));
        }
        holder.select_ly_day.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (HotelTimeActivity.startDay.getYear() == 0)
                {          // 第一次点击开始的位置，因为开始默认参数是 0,0,0,0
                    HotelTimeActivity.startDay.setDay(dayTimeEntity.getDay());           // 该item 天数的 年月日等信息  赋给  开始日期
                    HotelTimeActivity.startDay.setMonth(dayTimeEntity.getMonth());
                    HotelTimeActivity.startDay.setYear(dayTimeEntity.getYear());
                    HotelTimeActivity.startDay.setMonthPosition(dayTimeEntity.getMonthPosition());
                    HotelTimeActivity.startDay.setDayPosition(position);
                }
                else if (HotelTimeActivity.startDay.getYear() > 0 && HotelTimeActivity.stopDay.getYear() == -1)
                {      //已经点击了开始 ，点击结束位置，（默认结束位置-1,-1,-1,-1 说明还没有点击结束位置）
                    if (dayTimeEntity.getYear() > HotelTimeActivity.startDay.getYear())
                    {
                        //如果选中的年份大于开始的年份，说明结束日期肯定大于开始日期 ，合法的 ，将该item的天数的 信息  赋给 结束日期
                        HotelTimeActivity.stopDay.setDay(dayTimeEntity.getDay());
                        HotelTimeActivity.stopDay.setMonth(dayTimeEntity.getMonth());
                        HotelTimeActivity.stopDay.setYear(dayTimeEntity.getYear());
                        HotelTimeActivity.stopDay.setMonthPosition(dayTimeEntity.getMonthPosition());
                        HotelTimeActivity.stopDay.setDayPosition(position);
                    }
                    else if (dayTimeEntity.getYear() == HotelTimeActivity.startDay.getYear())
                    {
                        //如果选中的年份 等于 选中的年份
                        if (dayTimeEntity.getMonth() > HotelTimeActivity.startDay.getMonth())
                        {
                            //如果改item的天数的月份大于开始日期的月份，说明结束日期肯定大于开始日期 ，合法的 ，将该item的天数的 信息  赋给 结束日期
                            HotelTimeActivity.stopDay.setDay(dayTimeEntity.getDay());
                            HotelTimeActivity.stopDay.setMonth(dayTimeEntity.getMonth());
                            HotelTimeActivity.stopDay.setYear(dayTimeEntity.getYear());
                            HotelTimeActivity.stopDay.setMonthPosition(dayTimeEntity.getMonthPosition());
                            HotelTimeActivity.stopDay.setDayPosition(position);
                        }
                        else if (dayTimeEntity.getMonth() == HotelTimeActivity.startDay.getMonth())
                        {
                            //年份月份 都相等
                            if (dayTimeEntity.getDay() >= HotelTimeActivity.startDay.getDay())
                            {
                                //判断天数 ，如果 该item的天数的 日子大于等于 开始日期的 日子 ，说明结束日期合法的 ，将该item的天数的 信息  赋给 结束日期
                                HotelTimeActivity.stopDay.setDay(dayTimeEntity.getDay());
                                HotelTimeActivity.stopDay.setMonth(dayTimeEntity.getMonth());
                                HotelTimeActivity.stopDay.setYear(dayTimeEntity.getYear());
                                HotelTimeActivity.stopDay.setMonthPosition(dayTimeEntity.getMonthPosition());
                                HotelTimeActivity.stopDay.setDayPosition(position);
                            }
                            else
                            {
                                //天数小与初始  从新选择开始  ，结束日期重置，开始日期为当前的位置的天数的信息
                                HotelTimeActivity.startDay.setDay(dayTimeEntity.getDay());
                                HotelTimeActivity.startDay.setMonth(dayTimeEntity.getMonth());
                                HotelTimeActivity.startDay.setYear(dayTimeEntity.getYear());
                                HotelTimeActivity.startDay.setMonthPosition(dayTimeEntity.getMonthPosition());
                                HotelTimeActivity.startDay.setDayPosition(position);
                                HotelTimeActivity.stopDay.setDay(-1);
                                HotelTimeActivity.stopDay.setMonth(-1);
                                HotelTimeActivity.stopDay.setYear(-1);
                                HotelTimeActivity.stopDay.setMonthPosition(-1);
                                HotelTimeActivity.stopDay.setDayPosition(-1);
                            }
                        }
                        else
                        {
                            //选中的月份 比开始日期的月份还小，说明 结束位置不合法，结束日期重置，开始日期为当前的位置的天数的信息
                            HotelTimeActivity.startDay.setDay(dayTimeEntity.getDay());
                            HotelTimeActivity.startDay.setMonth(dayTimeEntity.getMonth());
                            HotelTimeActivity.startDay.setYear(dayTimeEntity.getYear());
                            HotelTimeActivity.startDay.setMonthPosition(dayTimeEntity.getMonthPosition());
                            HotelTimeActivity.startDay.setDayPosition(position);
                            HotelTimeActivity.stopDay.setDay(-1);
                            HotelTimeActivity.stopDay.setMonth(-1);
                            HotelTimeActivity.stopDay.setYear(-1);
                            HotelTimeActivity.stopDay.setMonthPosition(-1);
                            HotelTimeActivity.stopDay.setDayPosition(-1);
                        }

                    }
                    else
                    {
                        //选中的年份 比开始日期的年份还小，说明 结束位置不合法，结束日期重置，开始日期为当前的位置的天数的信息
                        HotelTimeActivity.startDay.setDay(dayTimeEntity.getDay());
                        HotelTimeActivity.startDay.setMonth(dayTimeEntity.getMonth());
                        HotelTimeActivity.startDay.setYear(dayTimeEntity.getYear());
                        HotelTimeActivity.startDay.setMonthPosition(dayTimeEntity.getMonthPosition());
                        HotelTimeActivity.startDay.setDayPosition(position);
                        HotelTimeActivity.stopDay.setDay(-1);
                        HotelTimeActivity.stopDay.setMonth(-1);
                        HotelTimeActivity.stopDay.setYear(-1);
                        HotelTimeActivity.stopDay.setMonthPosition(-1);
                        HotelTimeActivity.stopDay.setDayPosition(-1);
                    }
                }
                else if (HotelTimeActivity.startDay.getYear() > 0 && HotelTimeActivity.startDay.getYear() > 1)
                {      //已经点击开始和结束   第三次点击 ，重新点击开始
                    HotelTimeActivity.startDay.setDay(dayTimeEntity.getDay());
                    HotelTimeActivity.startDay.setMonth(dayTimeEntity.getMonth());
                    HotelTimeActivity.startDay.setYear(dayTimeEntity.getYear());
                    HotelTimeActivity.startDay.setMonthPosition(dayTimeEntity.getMonthPosition());
                    HotelTimeActivity.startDay.setDayPosition(position);
                    HotelTimeActivity.stopDay.setDay(-1);
                    HotelTimeActivity.stopDay.setMonth(-1);
                    HotelTimeActivity.stopDay.setYear(-1);
                    HotelTimeActivity.stopDay.setMonthPosition(-1);
                    HotelTimeActivity.stopDay.setDayPosition(-1);
                }
                EventBus.getDefault().post(new UpdataCalendar()); // 发消息刷新适配器，目的为了显示日历上各个日期的背景颜色
            }
        });


        if (HotelTimeActivity.startDay.getYear() == dayTimeEntity.getYear() && HotelTimeActivity.startDay.getMonth() == dayTimeEntity.getMonth() &&
                HotelTimeActivity.startDay.getDay() == dayTimeEntity.getDay()
                && HotelTimeActivity.stopDay.getYear() == dayTimeEntity.getYear() && HotelTimeActivity.stopDay.getMonth() == dayTimeEntity.getMonth() &&
                HotelTimeActivity.stopDay.getDay() == dayTimeEntity.getDay())
        {
            //开始和结束同一天
            holder.select_ly_day.setBackgroundResource(R.drawable.bg_time_startstop);
            holder.select_txt_day.setTextColor(context.getResources().getColor(R.color.white));
        }
        else if (HotelTimeActivity.startDay.getYear() == dayTimeEntity.getYear() && HotelTimeActivity.startDay.getMonth() == dayTimeEntity.getMonth() &&
                HotelTimeActivity.startDay.getDay() == dayTimeEntity.getDay())
        {
            //该item是 开始日期
            holder.select_ly_day.setBackgroundResource(R.drawable.bg_time_start);
            holder.select_txt_day.setTextColor(context.getResources().getColor(R.color.white));
        }
        else if (HotelTimeActivity.stopDay.getYear() == dayTimeEntity.getYear() && HotelTimeActivity.stopDay.getMonth() == dayTimeEntity.getMonth() &&
                HotelTimeActivity.stopDay.getDay() == dayTimeEntity.getDay())
        {
            //该item是 结束日期
            holder.select_ly_day.setBackgroundResource(R.drawable.bg_time_stop);
            holder.select_txt_day.setTextColor(context.getResources().getColor(R.color.white));
        }
        else if (dayTimeEntity.getMonthPosition() >= HotelTimeActivity.startDay.getMonthPosition() && dayTimeEntity.getMonthPosition() <= HotelTimeActivity
                .stopDay.getMonthPosition())
        {
            //处于开始和结束之间的点
            if (dayTimeEntity.getMonthPosition() == HotelTimeActivity.startDay.getMonthPosition() && dayTimeEntity.getMonthPosition() == HotelTimeActivity
                    .stopDay.getMonthPosition())
            {
                //开始和结束是一个月份
                if (dayTimeEntity.getDay() > HotelTimeActivity.startDay.getDay() && dayTimeEntity.getDay() < HotelTimeActivity.stopDay.getDay())
                {
                    holder.select_ly_day.setBackgroundResource(R.color.blueC);
                    holder.select_txt_day.setTextColor(context.getResources().getColor(R.color.white));
                }
                else
                {
                    holder.select_ly_day.setBackgroundResource(R.color.white);
                    holder.select_txt_day.setTextColor(context.getResources().getColor(R.color.blackD));
                }
            }
            else if (HotelTimeActivity.startDay.getMonthPosition() != HotelTimeActivity.stopDay.getMonthPosition())
            {
                // 日期和 开始 不是一个月份
                if (dayTimeEntity.getMonthPosition() == HotelTimeActivity.startDay.getMonthPosition() && dayTimeEntity.getDay() > HotelTimeActivity.startDay
                        .getDay())
                {
                    //和初始相同月  天数往后
                    holder.select_ly_day.setBackgroundResource(R.color.blueC);
                    holder.select_txt_day.setTextColor(context.getResources().getColor(R.color.white));
                }
                else if (dayTimeEntity.getMonthPosition() == HotelTimeActivity.stopDay.getMonthPosition() && dayTimeEntity.getDay() < HotelTimeActivity
                        .stopDay.getDay())
                {
                    //和结束相同月   天数往前
                    if (dayTimeEntity.getDay() <= 0)
                    {
                        holder.select_ly_day.setBackgroundResource(R.color.white);
                        holder.select_txt_day.setTextColor(context.getResources().getColor(R.color.blackD));
                    }
                    else
                    {
                        holder.select_ly_day.setBackgroundResource(R.color.blueC);
                        holder.select_txt_day.setTextColor(context.getResources().getColor(R.color.white));
                    }
                }
                else if (dayTimeEntity.getMonthPosition() != HotelTimeActivity.startDay.getMonthPosition() && dayTimeEntity.getMonthPosition() !=
                        HotelTimeActivity.stopDay.getMonthPosition())
                {
                    //和 开始结束都不是同一个月
                    holder.select_ly_day.setBackgroundResource(R.color.blueC);
                    holder.select_txt_day.setTextColor(context.getResources().getColor(R.color.white));
                }
                else
                {
                    holder.select_ly_day.setBackgroundResource(R.color.white);
                    holder.select_txt_day.setTextColor(context.getResources().getColor(R.color.blackD));
                }
            }

        }
        else
        {
            holder.select_ly_day.setBackgroundResource(R.color.white);
            holder.select_txt_day.setTextColor(context.getResources().getColor(R.color.blackD));
        }

        if ("1".equals(dayTimeEntity.getType()))
        {
            holder.select_ly_day.setEnabled(true);
            holder.select_txt_day.setTextColor(ContextCompat.getColor(context, R.color.black));
        }
        else
        {

            if (StringUtils.compare_date(str2, str1) >= 0)
            {
                holder.select_ly_day.setEnabled(true);
                holder.select_txt_day.setTextColor(ContextCompat.getColor(context, R.color.black));
            }
            else
            {
                holder.select_ly_day.setEnabled(false);
                holder.select_txt_day.setTextColor(ContextCompat.getColor(context, R.color.grayA));
            }
        }
    }

    @Override
    public int getItemCount()
    {
        int ret = 0;
        if (days != null)
        {
            ret = days.size();
        }
        return ret;
    }
}
