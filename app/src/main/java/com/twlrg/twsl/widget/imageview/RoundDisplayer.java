package com.twlrg.twsl.widget.imageview;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.twlrg.twsl.utils.ImageUtils;

public class RoundDisplayer implements BitmapDisplayer
{

    private int radius;

    public RoundDisplayer(int radius) {
        this.radius = radius;
    }

    @Override
    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        imageAware.setImageBitmap(ImageUtils.roundBitmap(bitmap,radius));
    }
}
