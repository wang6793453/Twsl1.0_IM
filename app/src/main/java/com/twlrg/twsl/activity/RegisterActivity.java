package com.twlrg.twsl.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.http.DataRequest;
import com.twlrg.twsl.http.HttpRequest;
import com.twlrg.twsl.http.IRequestListener;
import com.twlrg.twsl.json.RegisterHandler;
import com.twlrg.twsl.json.ResultHandler;
import com.twlrg.twsl.utils.ConstantUtil;
import com.twlrg.twsl.utils.StringUtils;
import com.twlrg.twsl.utils.ToastUtil;
import com.twlrg.twsl.utils.Urls;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSHelper;
import tencent.tls.platform.TLSStrAccRegListener;
import tencent.tls.platform.TLSUserInfo;

/**
 * 作者：王先云 on 2018/4/13 10:28
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class RegisterActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_phone)
    EditText  etPhone;
    @BindView(R.id.et_code)
    EditText  etCode;
    @BindView(R.id.tv_get_code)
    TextView  tvGetCode;
    @BindView(R.id.et_pwd)
    EditText  etPwd;
    @BindView(R.id.et_pwd1)
    EditText  etPwd1;
    @BindView(R.id.btn_register)
    Button    btnRegister;
    @BindView(R.id.et_nickname)
    EditText  etNickname;
    @BindView(R.id.et_position)
    EditText  etPosition;
    @BindView(R.id.tv_role_type1)
    TextView  tvRoleType1;
    @BindView(R.id.tv_role_type2)
    TextView  tvRoleType2;
    @BindView(R.id.tv_role_type3)
    TextView  tvRoleType3;
    @BindView(R.id.tv_role_type4)
    TextView  tvRoleType4;
    private boolean isRoleType1, isRoleType2, isRoleType3, isRoleType4;

    private List<Boolean> isRoleTypeList = new ArrayList<>();

    private static final int    REQUEST_REGISTER_SUCCESS = 0x01;
    public static final  int    REQUEST_FAIL             = 0x02;
    private static final int    GET_CODE_SUCCESS         = 0x03;
    private static final String USER_REGISTER            = "user_register";
    private static final String GET_CODE                 = "GET_CODE";


    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {


                case REQUEST_REGISTER_SUCCESS:
                    ToastUtil.show(RegisterActivity.this, "注册成功!");
                    RegisterHandler mRegisterHandler = (RegisterHandler) msg.obj;

                    String uid = mRegisterHandler.getUid();

                    if (!StringUtils.stringIsEmpty(uid))
                    {
                        TLSHelper instance = TLSHelper.getInstance();
                        instance.TLSStrAccReg("slbl_server_" + uid, "slbl123456", new TLSStrAccRegListener()
                        {
                            @Override
                            public void OnStrAccRegSuccess(TLSUserInfo tlsUserInfo)
                            {
                                finish();
                                //LogUtil.d(TAG, "OnStrAccRegSuccess:" + tlsUserInfo.identifier + "");
                            }

                            @Override
                            public void OnStrAccRegFail(TLSErrInfo tlsErrInfo)
                            {
                                finish();
                                //LogUtil.d(TAG, "OnStrAccRegFail:" + tlsErrInfo.Msg + " " + tlsErrInfo.ExtraMsg);

                            }

                            @Override
                            public void OnStrAccRegTimeout(TLSErrInfo tlsErrInfo)
                            {
                                finish();
                                //LogUtil.d(TAG, "OnStrAccRegTimeout:" + tlsErrInfo.Msg + " " + tlsErrInfo.ExtraMsg);
                            }
                        });
                    }

                    break;


                case REQUEST_FAIL:
                    ToastUtil.show(RegisterActivity.this, msg.obj.toString());
                    break;

                case GET_CODE_SUCCESS:
                    ToastUtil.show(RegisterActivity.this, "验证码已发送");
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

        setContentView(R.layout.activity_register);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        tvGetCode.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        tvRoleType1.setOnClickListener(this);
        tvRoleType2.setOnClickListener(this);
        tvRoleType3.setOnClickListener(this);
        tvRoleType4.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {

    }


    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == ivBack)
        {
            finish();
        }
        else if (v == tvGetCode)
        {
            String phone = etPhone.getText().toString();

            if (StringUtils.stringIsEmpty(phone) || phone.length() < 11)
            {
                ToastUtil.show(this, "请输入正确的手机号");
                return;
            }

            Map<String, String> valuePairs = new HashMap<>();
            valuePairs.put("mobile", phone);
            valuePairs.put("role", "2");
            DataRequest.instance().request(RegisterActivity.this, Urls.getVerifycodeUrl(), this, HttpRequest.POST, GET_CODE, valuePairs,
                    new ResultHandler());
        }
        else if (v == btnRegister)
        {
            String phone = etPhone.getText().toString();
            String code = etCode.getText().toString();
            String pwd = etPwd.getText().toString();
            String pwd1 = etPwd1.getText().toString();
            String nickname = etNickname.getText().toString();
            String position = etPosition.getText().toString();
            isRoleTypeList.clear();
            isRoleTypeList.add(isRoleType1);
            isRoleTypeList.add(isRoleType2);
            isRoleTypeList.add(isRoleType3);
            isRoleTypeList.add(isRoleType4);


            if (StringUtils.stringIsEmpty(nickname))
            {
                ToastUtil.show(this, "请输入姓名");
                return;
            }

            if (StringUtils.stringIsEmpty(position))
            {
                ToastUtil.show(this, "请输入职务");
                return;
            }

            if (StringUtils.stringIsEmpty(phone) || phone.length() < 11)
            {
                ToastUtil.show(this, "请输入正确的手机号");
                return;
            }
            if (StringUtils.stringIsEmpty(code))
            {
                ToastUtil.show(this, "请输入验证码");
                return;
            }
            if (StringUtils.stringIsEmpty(pwd) || pwd.length() < 8)
            {
                ToastUtil.show(this, "请输入8-16密码");
                return;
            }

            if (!pwd.equals(pwd1))
            {
                ToastUtil.show(this, "两次密码输入不一致");
                return;
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < isRoleTypeList.size(); i++)
            {
                if (isRoleTypeList.get(i))
                {
                    sb.append(i + 1);
                    sb.append(",");
                }
            }

            String role_type = sb.toString();

            if (StringUtils.stringIsEmpty(role_type))
            {
                ToastUtil.show(this, "请选择业务范围");
                return;
            }

            Map<String, String> valuePairs = new HashMap<>();
            valuePairs.put("nickname", nickname);
            valuePairs.put("position", position);
            valuePairs.put("email", "");
            valuePairs.put("mobile", phone);
            valuePairs.put("pwd", pwd);
            valuePairs.put("role", "2");
            valuePairs.put("sex", "0");
            valuePairs.put("role_type", role_type.substring(0, role_type.length() - 1));

            valuePairs.put("verifycode", code);
            DataRequest.instance().request(RegisterActivity.this, Urls.getRegisterUrl(), this, HttpRequest.POST, USER_REGISTER, valuePairs,
                    new RegisterHandler());


        }
        else if (v == tvRoleType1)
        {
            if (isRoleType1)
            {
                isRoleType1 = false;
                tvRoleType1.setSelected(false);
            }
            else
            {
                isRoleType1 = true;
                tvRoleType1.setSelected(true);
            }
        }
        else if (v == tvRoleType2)
        {
            if (isRoleType2)
            {
                isRoleType2 = false;
                tvRoleType2.setSelected(false);
            }
            else
            {
                isRoleType2 = true;
                tvRoleType2.setSelected(true);
            }
        }
        else if (v == tvRoleType3)
        {
            if (isRoleType3)
            {
                isRoleType3 = false;
                tvRoleType3.setSelected(false);
            }
            else
            {
                isRoleType3 = true;
                tvRoleType3.setSelected(true);
            }
        }
        else if (v == tvRoleType4)
        {
            if (isRoleType4)
            {
                isRoleType4 = false;
                tvRoleType4.setSelected(false);
            }
            else
            {
                isRoleType4 = true;
                tvRoleType4.setSelected(true);
            }
        }
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        if (USER_REGISTER.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_REGISTER_SUCCESS, obj));
            }

            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }

        if (GET_CODE.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_CODE_SUCCESS, obj));
            }

            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }


    }

}
