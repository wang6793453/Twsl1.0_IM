package com.twlrg.twsl.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.imsdk.TIMCallBack;
import com.tencent.qcloud.presentation.presenter.FriendshipManagerPresenter;
import com.tencent.qcloud.tlslibrary.service.TLSService;
import com.twlrg.twsl.R;
import com.twlrg.twsl.http.DataRequest;
import com.twlrg.twsl.http.HttpRequest;
import com.twlrg.twsl.http.IRequestListener;
import com.twlrg.twsl.im.TencentCloud;
import com.twlrg.twsl.json.LoginHandler;
import com.twlrg.twsl.utils.ConfigManager;
import com.twlrg.twsl.utils.ConstantUtil;
import com.twlrg.twsl.utils.LogUtil;
import com.twlrg.twsl.utils.StringUtils;
import com.twlrg.twsl.utils.ToastUtil;
import com.twlrg.twsl.utils.Urls;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/4/13 09:53
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class LoginActivity extends BaseActivity implements IRequestListener
{

    public static void start(Context context, boolean loginIM)
    {
        Intent starter = new Intent(context, LoginActivity.class);
        starter.putExtra("loginIM", loginIM);
        context.startActivity(starter);
    }

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_register)
    TextView  tvRegister;
    @BindView(R.id.et_phone)
    EditText  etPhone;
    @BindView(R.id.et_pwd)
    EditText  etPwd;
    @BindView(R.id.btn_login)
    Button    btnLogin;
    @BindView(R.id.tv_forget_pwd)
    TextView  tvForgetPwd;

    String mUserName, mPwd;

    private static final int REQUEST_LOGIN_SUCCESS = 0x01;
    public static final  int REQUEST_FAIL          = 0x02;
    public static final  int LOGIN_IM              = 0X03;
    public static final  int ACTIVITY_FINISH       = 0X04;

    private static final String USER_LOGIN = "user_login";

    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {


                case REQUEST_LOGIN_SUCCESS:


                    ConfigManager.instance().setUserPwd(mPwd);
                    ConfigManager.instance().setMobile(mUserName);
                    sendEmptyMessage(LOGIN_IM);
                    break;


                case REQUEST_FAIL:
                    ToastUtil.show(LoginActivity.this, msg.obj.toString());
                    break;

                case LOGIN_IM:
                    TencentCloud.LoginListener login = new TencentCloud.LoginListener()
                    {
                        @Override
                        public void onSuccess(String identifier)
                        {
                            TLSService.getInstance().setLastErrno(0);
                            modifyUserProfile();
                        }

                        @Override
                        public void onFail(String msg, int code2)
                        {
                            TLSService.getInstance().setLastErrno(-1);
                            LogUtil.d("login", "failed:" + msg + " " + code2);
                            ToastUtil.show(LoginActivity.this, "登录失败!");
                        }
                    };
                    String identifier = ConfigManager.instance().getIdentifier();
                    if (getIntent().getBooleanExtra("loginIM", false))
                    {
                        TencentCloud.IMLogin(identifier, login);
                    }
                    else
                    {
                        TencentCloud.login(identifier, login);
                    }
                    break;

                case ACTIVITY_FINISH:
                    finish();
                    break;

            }
        }
    };


    @Override
    protected void initData()
    {


    }


    private void modifyUserProfile()
    {

        String name = ConfigManager.instance().getUserNickName();
        String userPic = Urls.getImgUrl(ConfigManager.instance().getUserPic());
        FriendshipManagerPresenter.setMyInfo(name, userPic, new TIMCallBack()
        {
            @Override
            public void onError(int i, String s)
            {
                ToastUtil.show(LoginActivity.this, "登录成功!");
                finish();

            }

            @Override
            public void onSuccess()
            {
                LogUtil.e("login", "modifyUserProfile onSuccess");
                ToastUtil.show(LoginActivity.this, "登录成功!");
                finish();

            }
        });
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_login);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        tvForgetPwd.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        etPhone.setText(ConfigManager.instance().getMobile());
        etPwd.setText(ConfigManager.instance().getUserPwd());
    }


    @Override
    public void onClick(View v)
    {
        super.onClick(v);

        if (v == ivBack)
        {
            sendBroadcast(new Intent().setAction("USER_LOGOUT"));
            finish();
        }
        else if (v == tvForgetPwd)
        {
            startActivity(new Intent(LoginActivity.this, ForgetPwdActivity.class));
        }
        else if (v == tvRegister)
        {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

        }
        else if (v == btnLogin)
        {
            mUserName = etPhone.getText().toString();
            mPwd = etPwd.getText().toString();


            if (StringUtils.stringIsEmpty(mUserName) || mUserName.length() < 11)
            {
                ToastUtil.show(this, "请输入正确的手机号");
                return;
            }


            if (StringUtils.stringIsEmpty(mPwd))
            {
                ToastUtil.show(this, "请输入正确的密码");
                return;
            }

            showProgressDialog();
            Map<String, String> valuePairs = new HashMap<>();
            valuePairs.put("mobile", mUserName);
            valuePairs.put("pwd", mPwd);
            valuePairs.put("role", "2");
            DataRequest.instance().request(LoginActivity.this, Urls.getLoginUrl(), this, HttpRequest.POST, USER_LOGIN, valuePairs,
                    new LoginHandler());
        }
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog();
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


    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {

            sendBroadcast(new Intent().setAction("USER_LOGOUT"));
            finish();
            return false;
        }
        else
        {
            return super.onKeyDown(keyCode, event);
        }

    }

}
