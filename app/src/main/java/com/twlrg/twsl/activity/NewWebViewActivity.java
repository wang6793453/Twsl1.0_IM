package com.twlrg.twsl.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.ShareInfo;
import com.twlrg.twsl.listener.MyOnClickListener;
import com.twlrg.twsl.utils.DialogUtils;
import com.twlrg.twsl.utils.LogUtil;
import com.twlrg.twsl.utils.ToastUtil;
import com.twlrg.twsl.utils.WXShare;
import com.twlrg.twsl.widget.AutoFitTextView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：王先云 on 2018/9/1 16:14
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class NewWebViewActivity extends BaseActivity
{
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView tvTitle;
    @BindView(R.id.tv_submit)
    TextView        tvSubmit;
    @BindView(R.id.bridge_webView)
    BridgeWebView   mBridgeWebView;
    public static final String EXTRA_URL   = "extra_url";
    public static final String EXTRA_TITLE = "extra_title";
    public static final String IS_SETTITLE = "isSetTitle";

    private String callBackData;

    @Override
    protected void initData()
    {
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_new_webview);
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mBridgeWebView.canGoBack())
                {
                    tvSubmit.setVisibility(View.GONE);
                    mBridgeWebView.goBack();
                }
                else
                {
                    finish();
                }
            }
        });
        tvSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!TextUtils.isEmpty(callBackData))
                {
                    try
                    {
                        final ShareInfo shareInfo = new ShareInfo(new JSONObject(callBackData));


                        DialogUtils.showShareDialog(NewWebViewActivity.this, new MyOnClickListener.OnSubmitListener()
                        {
                            @Override
                            public void onSubmit(String content)
                            {
                                shareInfo.setShareStyle(Integer.parseInt(content));
                                wxShare.shareWebpage(NewWebViewActivity.this, shareInfo);
                            }
                        });


                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    @Override
    protected void onDestroy()
    {
        wxShare.unregister();
        super.onDestroy();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        wxShare.register();
    }

    private WXShare wxShare;

    @Override
    protected void initViewData()
    {
        tvSubmit.setVisibility(View.GONE);

        mBridgeWebView.getSettings().setJavaScriptEnabled(true);
        mBridgeWebView.getSettings().setSupportZoom(true);
        mBridgeWebView.getSettings().setBuiltInZoomControls(false);
        // mBridgeWebView.getSettings().setDefaultFontSize(18);
        String mUrl = getIntent().getStringExtra(EXTRA_URL);
        mBridgeWebView.setWebViewClient(new MyWebViewClient(mBridgeWebView));

        mBridgeWebView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onReceivedTitle(WebView view, String title)
            {
                super.onReceivedTitle(view, title);
                //    Log.d("ANDROID_LAB", "TITLE=" + title);
                tvTitle.setText(title);
            }
        });
        mBridgeWebView.setDefaultHandler(new BridgeHandler()
        {
            @Override
            public void handler(String data, CallBackFunction function)
            {
                // Log.e(TAG, "DefaultHandler接收全部来自web的数据："+data);
                function.onCallBack("DefaultHandler收到Web发来的数据，回传数据给你");
            }
        });

        mBridgeWebView.registerHandler("shareInfoHandle", new BridgeHandler()
        {

            @Override
            public void handler(String data, CallBackFunction function)
            {
                //  Log.e(TAG, "指定Handler接收来自web的数据：" + data);
                function.onCallBack("shareInfoHandle");
            }
        });

        wxShare = new WXShare(this);
        wxShare.setListener(new WXShare.OnResponseListener()
        {
            @Override
            public void onSuccess()
            {
                // 分享成功
                // ToastUtil.show(NewWebViewActivity.this, "分享成功");
            }

            @Override
            public void onCancel()
            {
                // 分享取消
            }

            @Override
            public void onFail(String message)
            {
                // 分享失败
            }
        });


        mBridgeWebView.loadUrl(mUrl);
    }

    class MyWebViewClient extends BridgeWebViewClient
    {

        public MyWebViewClient(BridgeWebView webView)
        {

            super(webView);

        }

        @Override

        public void onPageFinished(WebView view, String url)
        {

            super.onPageFinished(view, url);


            mBridgeWebView.callHandler("shareInfoHandle", "AAA", new CallBackFunction()
            {

                @Override

                public void onCallBack(String data)
                {

                    LogUtil.e("TAG", "来自web的回传数据：" + data);
                    callBackData = data;
                    if (!TextUtils.isEmpty(data))
                    {
                        tvSubmit.setVisibility(View.VISIBLE);
                        tvSubmit.setText("分享");

                    }
                }

            });

        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {

            if (mBridgeWebView.canGoBack())
            {
                tvSubmit.setVisibility(View.GONE);
                mBridgeWebView.goBack();
            }
            else
            {
                return super.onKeyDown(keyCode, event);
            }

            return false;
        }
        else
        {
            return super.onKeyDown(keyCode, event);
        }

    }
}
