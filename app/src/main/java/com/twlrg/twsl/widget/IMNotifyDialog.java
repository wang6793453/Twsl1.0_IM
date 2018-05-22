package com.twlrg.twsl.widget;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.activity.LoginActivity;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.ConfigManager;


/**
 * 提示对话框
 */
public class IMNotifyDialog extends DialogFragment {

    String tag = "notifyDialog";
    private String title;
    DialogInterface.OnClickListener okListener;
    DialogInterface.OnClickListener cancelListener;

    public void show(String title, FragmentManager fm, DialogInterface.OnClickListener listener1, DialogInterface.OnClickListener listener2){
        this.title = title;
        okListener = listener1;
        cancelListener = listener2;
        setCancelable(false);
        try{
            show(fm, tag);
        }catch (Exception e){
            Log.e(tag, "show notify dialog error, activity has been destroyed");
        }

    }

    public void show(String title, FragmentManager fm, DialogInterface.OnClickListener listener1){
        this.title = title;
        okListener = listener1;
        setCancelable(false);
        try{
            show(fm, tag);
        }catch (Exception e){
            Log.e(tag, "show notify dialog error, activity has been destroyed");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = new Dialog(getActivity(), R.style.dialogNoAnimation);
        dialog.setCancelable(false);
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_logout, null);
        dialog.setContentView(v);
        TextView mTitle = (TextView) v.findViewById(R.id.tv_title);
        mTitle.setText(title);
        ((RelativeLayout) v.findViewById(R.id.rl_confirm)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();

                getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        v.findViewById(R.id.rl_cancel).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                ConfigManager.instance().setUserId("");
            }
        });
        //Dialog部分
        Window mWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = APPUtils.getScreenWidth(getActivity()) * 7 / 8;
        mWindow.setAttributes(lp);
        return dialog;
    }
}
