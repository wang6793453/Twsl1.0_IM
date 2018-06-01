package com.twlrg.twsl.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twlrg.twsl.MyApplication;
import com.twlrg.twsl.R;
import com.twlrg.twsl.http.DataRequest;
import com.twlrg.twsl.http.HttpRequest;
import com.twlrg.twsl.http.IRequestListener;
import com.twlrg.twsl.json.ResultHandler;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.ConfigManager;
import com.twlrg.twsl.utils.ConstantUtil;
import com.twlrg.twsl.utils.StringUtils;
import com.twlrg.twsl.utils.ToastUtil;
import com.twlrg.twsl.utils.Urls;
import com.twlrg.twsl.widget.AutoFitTextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/4/23 20:02
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class AddCommentActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.topView)
    View            topView;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView tvTitle;
    @BindView(R.id.tv_submit)
    TextView        tvSubmit;
    @BindView(R.id.et_content)
    EditText        etContent;

    private String merchant_id;

    private static final int    REQUEST_LOGIN_SUCCESS = 0x01;
    public static final  int    REQUEST_FAIL          = 0x02;
    private static final String ADD_COMMENT           = "add_comment";

    private BaseHandler mHandler = new BaseHandler(this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {


                case REQUEST_LOGIN_SUCCESS:
                    ToastUtil.show(AddCommentActivity.this, "发布成功!");
                    finish();
                    break;


                case REQUEST_FAIL:
                    ToastUtil.show(AddCommentActivity.this, msg.obj.toString());
                    break;


            }
        }
    };

    @Override
    protected void initData()
    {
        merchant_id = getIntent().getStringExtra("MERCHANT_ID");
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_add_comment);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        setStatusBarTextDeep(false);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));
        tvTitle.setText("评论");
        tvSubmit.setText("发布");
        tvSubmit.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == ivBack)
        {
            finish();
        }
        else if (v == tvSubmit)
        {
            if (MyApplication.getInstance().isLogin())
            {
                String content = etContent.getText().toString();

                if (StringUtils.stringIsEmpty(content))
                {
                    ToastUtil.show(AddCommentActivity.this, "请输入您的评论");
                    return;
                }
                Map<String, String> valuePairs = new HashMap<>();
                valuePairs.put("merchant_id", merchant_id);
                valuePairs.put("content", content);
                valuePairs.put("user_id", ConfigManager.instance().getUserID());
                valuePairs.put("create_time", StringUtils.getTimestamp());

                DataRequest.instance().request(AddCommentActivity.this, Urls.getAddCommentUrl(), this, HttpRequest.POST, ADD_COMMENT, valuePairs,
                        new ResultHandler());
            }
            else
            {
                LoginActivity.start(AddCommentActivity.this,true);
            }

        }
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        if (ADD_COMMENT.equals(action))
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
