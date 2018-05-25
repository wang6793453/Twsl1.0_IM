package com.twlrg.twsl.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.adapter.CommentAdapter;
import com.twlrg.twsl.entity.CommentInfo;
import com.twlrg.twsl.http.DataRequest;
import com.twlrg.twsl.http.HttpRequest;
import com.twlrg.twsl.http.IRequestListener;
import com.twlrg.twsl.json.CommentListHandler;
import com.twlrg.twsl.json.ResultHandler;
import com.twlrg.twsl.listener.MyItemClickListener;
import com.twlrg.twsl.listener.MyOnClickListener;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.ConfigManager;
import com.twlrg.twsl.utils.ConstantUtil;
import com.twlrg.twsl.utils.DialogUtils;
import com.twlrg.twsl.utils.StringUtils;
import com.twlrg.twsl.utils.ToastUtil;
import com.twlrg.twsl.utils.Urls;
import com.twlrg.twsl.widget.AutoFitTextView;
import com.twlrg.twsl.widget.DividerDecoration;
import com.twlrg.twsl.widget.EmptyDecoration;
import com.twlrg.twsl.widget.list.refresh.PullToRefreshBase;
import com.twlrg.twsl.widget.list.refresh.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/4/23 20:46
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class CommentListActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener<RecyclerView>, IRequestListener
{
    @BindView(R.id.topView)
    View                      topView;
    @BindView(R.id.iv_back)
    ImageView                 ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView           tvTitle;
    @BindView(R.id.pullToRefreshRecyclerView)
    PullToRefreshRecyclerView mPullToRefreshRecyclerView;
    @BindView(R.id.tv_noData)
    TextView                  tvNoData;

    private RecyclerView mRecyclerView;
    private int pn = 1;
    private int mRefreshStatus;
    private List<CommentInfo> commentInfoList = new ArrayList<>();
    private CommentAdapter mCommentAdapter;

    private String merchant_id;


    private static final String GET_COMMENT_LIST = "get_comment_list";
    private static final String REPLY_COMMENT = "reply_comment";
    private static final int REQUEST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL    = 0x02;
    private static final int  REPLY_COMMENT_SUCCESS = 0x03;


    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(CommentListActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:
                    CommentListHandler mCommentListHandler = (CommentListHandler) msg.obj;
                    commentInfoList.addAll(mCommentListHandler.getCommentInfoList());
                    mCommentAdapter.notifyDataSetChanged();

                    if (commentInfoList.isEmpty())
                    {
                        tvNoData.setVisibility(View.VISIBLE);
                        mPullToRefreshRecyclerView.setVisibility(View.GONE);
                    }
                    else
                    {
                        tvNoData.setVisibility(View.GONE);
                        mPullToRefreshRecyclerView.setVisibility(View.VISIBLE);
                    }

                    break;

                case REQUEST_FAIL:
                    ToastUtil.show(CommentListActivity.this, msg.obj.toString());

                    break;
                case REPLY_COMMENT_SUCCESS:
                    ToastUtil.show(CommentListActivity.this, "操作成功");
                    commentInfoList.clear();
                    pn = 1;
                    mRefreshStatus = 0;
                    getCommentList();
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
        setContentView(R.layout.activity_comment_list);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        setStatusBarTextDeep(true);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));
        tvTitle.setText("评论");


        mPullToRefreshRecyclerView.setPullLoadEnabled(true);
        mRecyclerView = mPullToRefreshRecyclerView.getRefreshableView();
        mPullToRefreshRecyclerView.setOnRefreshListener(this);
        mPullToRefreshRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(CommentListActivity.this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new EmptyDecoration(CommentListActivity.this,""));


        mCommentAdapter = new CommentAdapter(commentInfoList, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, final int position)
            {
                DialogUtils.showReplyDialog(CommentListActivity.this, new MyOnClickListener.OnSubmitListener()
                {
                    @Override
                    public void onSubmit(String content)
                    {
                        showProgressDialog();
                        Map<String, String> valuePairs = new HashMap<>();
                        valuePairs.put("merchant_id", merchant_id);
                        valuePairs.put("user_id", ConfigManager.instance().getUserID());
                        valuePairs.put("pid", commentInfoList.get(position).getId());
                        valuePairs.put("content", content);
                        valuePairs.put("create_time", StringUtils.getCurrentTime());
                        DataRequest.instance().request(CommentListActivity.this, Urls.getCommentReplyUrl(), CommentListActivity.this, HttpRequest.POST, REPLY_COMMENT, valuePairs,
                                new ResultHandler());
                    }
                });
            }
        });
        mRecyclerView.setAdapter(mCommentAdapter);
        getCommentList();
    }


    @Override
    public void onClick(View v)
    {
        super.onClick(v);

        if (v == ivBack)
        {
            finish();
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView)
    {
        commentInfoList.clear();
        pn = 1;
        mRefreshStatus = 0;
        getCommentList();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView)
    {
        pn += 1;
        mRefreshStatus = 1;
        getCommentList();
    }

    private void getCommentList()
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("merchant_id", merchant_id);
        valuePairs.put("page", pn + "");
        DataRequest.instance().request(CommentListActivity.this, Urls.getCommentListUrl(), this, HttpRequest.POST, GET_COMMENT_LIST, valuePairs,
                new CommentListHandler());
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog();
        if (mRefreshStatus == 1)
        {
            mPullToRefreshRecyclerView.onPullUpRefreshComplete();
        }
        else
        {
            mPullToRefreshRecyclerView.onPullDownRefreshComplete();
        }

        if (GET_COMMENT_LIST.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if(REPLY_COMMENT.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(REPLY_COMMENT_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }
}
