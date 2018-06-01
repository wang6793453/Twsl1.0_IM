package com.twlrg.twsl.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.http.DataRequest;
import com.twlrg.twsl.http.HttpRequest;
import com.twlrg.twsl.http.IRequestListener;
import com.twlrg.twsl.json.LoginHandler;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.ConfigManager;
import com.twlrg.twsl.utils.ConstantUtil;
import com.twlrg.twsl.utils.StringUtils;
import com.twlrg.twsl.utils.ToastUtil;
import com.twlrg.twsl.utils.Urls;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/4/12 23:12
 * 邮箱：wangxianyun1@163.com
 * 描述：修改密码
 */
public class ModifyPwdActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.tv_title)
    TextView  tvTitle;
    @BindView(R.id.et_oldPwd)
    EditText  etOldPwd;
    @BindView(R.id.et_newPwd)
    EditText  etNewPwd;
    @BindView(R.id.et_newPwd1)
    EditText  etNewPwd1;
    @BindView(R.id.btn_submit)
    Button    btnSubmit;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.topView)
    View      topView;


    private static final int    REQUEST_LOGIN_SUCCESS = 0x01;
    public static final  int    REQUEST_FAIL          = 0x02;
    private static final String USER_LOGIN            = "user_login";


    private BaseHandler mHandler = new BaseHandler(this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {


                case REQUEST_LOGIN_SUCCESS:
                    ToastUtil.show(ModifyPwdActivity.this, "密码修改成功!");
                    ConfigManager.instance().setUserPwd(etNewPwd.getText().toString());
                    finish();
                    break;


                case REQUEST_FAIL:
                    ToastUtil.show(ModifyPwdActivity.this, msg.obj.toString());
                    break;


            }
        }
    };


    @Override
    protected void initData()
    {

    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_modify_pwd);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        tvTitle.setText("修改密码");
        setStatusBarTextDeep(false);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == ivBack)
        {
            finish();
        }
        else if (v == btnSubmit)
        {
            String oldPwd = etOldPwd.getText().toString();
            String newPwd = etNewPwd.getText().toString();
            String newPwd1 = etNewPwd1.getText().toString();

            if (StringUtils.stringIsEmpty(oldPwd) || !oldPwd.equals(ConfigManager.instance().getUserPwd()))
            {
                ToastUtil.show(this, "请输入正确的旧密码");
                return;
            }

            if (StringUtils.stringIsEmpty(newPwd))
            {
                ToastUtil.show(this, "请输入新密码");
                return;
            }


            if (newPwd.length() < 8)
            {
                ToastUtil.show(this, "请输入8-16位新密码");
                return;
            }


            if (!newPwd.equals(newPwd1))
            {
                ToastUtil.show(this, "两次新密码输入不一致");
                return;
            }
            Map<String, String> valuePairs = new HashMap<>();
            valuePairs.put("uid", ConfigManager.instance().getUserID());
            valuePairs.put("pwd", newPwd);
            valuePairs.put("role", "2");
            valuePairs.put("token", ConfigManager.instance().getToken());

            DataRequest.instance().request(ModifyPwdActivity.this, Urls.getUpdatePwdUrl(), this, HttpRequest.POST, USER_LOGIN, valuePairs,
                    new LoginHandler());

        }
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        if (USER_LOGIN.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_LOGIN_SUCCESS, obj));
            }

            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }
}
