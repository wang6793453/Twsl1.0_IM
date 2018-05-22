package com.twlrg.twsl.utils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.twlrg.twsl.widget.imageview.RoundDisplayer;
import com.twlrg.twsl.widget.imageview.drawable.RoundDrawable;


public class ImageOptions
{

    private static DisplayImageOptions.Builder getBuilder()
    {
        return new DisplayImageOptions.Builder().cacheInMemory(false).cacheOnDisk(true).showImageOnLoading(new ColorDrawable(Color.GRAY));
    }

    public static DisplayImageOptions getStandard()
    {
        return getBuilder().build();
    }

    public static DisplayImageOptions getRound(int radius)
    {
        RoundDrawable drawable = new RoundDrawable(Color.GRAY, radius);
        return getBuilder().displayer(new RoundDisplayer(radius)).showImageOnFail(drawable).showImageForEmptyUri(drawable).showImageOnLoading(drawable).build();
    }

    public static DisplayImageOptions getRoundCorner(int cornerPixels)
    {
        ColorDrawable drawable = new ColorDrawable(Color.GRAY);

        return getBuilder().showImageOnFail(drawable).showImageForEmptyUri(drawable).showImageOnLoading(drawable).displayer(new RoundedBitmapDisplayer(cornerPixels)).build();
    }

//    public static DisplayImageOptions getVideoCoverInstance()
//    {
//
//        return getBuilder().displayer(new RoundedBitmapDisplayer(5)).showImageOnFail(R.drawable.icon_stub).showImageForEmptyUri(R.drawable.icon_stub).showImageOnLoading(R.drawable.icon_stub).build();
//    }
}
