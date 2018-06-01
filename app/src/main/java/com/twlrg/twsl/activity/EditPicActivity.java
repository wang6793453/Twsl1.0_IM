package com.twlrg.twsl.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kevin.crop.UCrop;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.ConferenceInfo;
import com.twlrg.twsl.entity.RoomInfo;
import com.twlrg.twsl.http.DataRequest;
import com.twlrg.twsl.http.HttpRequest;
import com.twlrg.twsl.http.IRequestListener;
import com.twlrg.twsl.json.ConferenceInfoHandler;
import com.twlrg.twsl.json.ResultHandler;
import com.twlrg.twsl.json.RoomInfoHandler;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.ConfigManager;
import com.twlrg.twsl.utils.ConstantUtil;
import com.twlrg.twsl.utils.ToastUtil;
import com.twlrg.twsl.utils.Urls;
import com.twlrg.twsl.widget.AutoFitTextView;
import com.twlrg.twsl.widget.SelectPicturePopupWindow;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/5/28 21:14
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class EditPicActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.topView)
    View            topView;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView tvTitle;
    @BindView(R.id.tv_pic1)
    TextView        tvPic1;
    @BindView(R.id.iv_pic1)
    ImageView       ivPic1;
    @BindView(R.id.ll_pic1)
    FrameLayout     llPic1;
    @BindView(R.id.tv_pic2)
    TextView        tvPic2;
    @BindView(R.id.iv_pic2)
    ImageView       ivPic2;
    @BindView(R.id.ll_pic2)
    FrameLayout     llPic2;
    @BindView(R.id.tv_pic3)
    TextView        tvPic3;
    @BindView(R.id.iv_pic3)
    ImageView       ivPic3;
    @BindView(R.id.ll_pic3)
    FrameLayout     llPic3;
    @BindView(R.id.tv_pic4)
    TextView        tvPic4;
    @BindView(R.id.iv_pic4)
    ImageView       ivPic4;
    @BindView(R.id.ll_pic4)
    FrameLayout     llPic4;
    @BindView(R.id.tv_pic5)
    TextView        tvPic5;
    @BindView(R.id.iv_pic5)
    ImageView       ivPic5;
    @BindView(R.id.ll_pic5)
    FrameLayout     llPic5;
    @BindView(R.id.tv_pic6)
    TextView        tvPic6;
    @BindView(R.id.iv_pic6)
    ImageView       ivPic6;
    @BindView(R.id.ll_pic6)
    FrameLayout     llPic6;


    private String id;
    private String type;//1:room  2:conference

    private static final String      GET_ROOMINFO                = "get_roominfo";
    private static final String      GET_CONFERENCE_INFO         = "GET_CONFERENCE_INFO";
    private static final int         REQUEST_FAIL                = 0x02;
    private static final int         GET_ROOMINFO_SUCCESS        = 0x03;
    private static final int         GET_CONFERENCE_INFO_SUCCESS = 0x04;
    private static final int         UPLOAD_PIC_SUCCESS          = 0x05;
    @SuppressLint("HandlerLeak")
    private final        BaseHandler mHandler                    = new BaseHandler(EditPicActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {


                case REQUEST_FAIL:
                    ToastUtil.show(EditPicActivity.this, msg.obj.toString());

                    break;
                case GET_ROOMINFO_SUCCESS:
                    RoomInfoHandler mRoomInfoHandler = (RoomInfoHandler) msg.obj;
                    RoomInfo mRoomInfo = mRoomInfoHandler.getRoomInfo();

                    if (null != mRoomInfo)
                    {
                        if (!TextUtils.isEmpty(mRoomInfo.getPic1()))
                            ImageLoader.getInstance().displayImage(Urls.getImgUrl(mRoomInfo.getPic1()), ivPic1);

                        if (!TextUtils.isEmpty(mRoomInfo.getPic2()))
                            ImageLoader.getInstance().displayImage(Urls.getImgUrl(mRoomInfo.getPic2()), ivPic2);

                        if (!TextUtils.isEmpty(mRoomInfo.getPic3()))
                            ImageLoader.getInstance().displayImage(Urls.getImgUrl(mRoomInfo.getPic3()), ivPic3);

                        if (!TextUtils.isEmpty(mRoomInfo.getPic4()))
                            ImageLoader.getInstance().displayImage(Urls.getImgUrl(mRoomInfo.getPic4()), ivPic4);

                        if (!TextUtils.isEmpty(mRoomInfo.getPic5()))
                            ImageLoader.getInstance().displayImage(Urls.getImgUrl(mRoomInfo.getPic5()), ivPic5);

                        if (!TextUtils.isEmpty(mRoomInfo.getPic6()))
                            ImageLoader.getInstance().displayImage(Urls.getImgUrl(mRoomInfo.getPic6()), ivPic6);
                    }

                    break;
                case GET_CONFERENCE_INFO_SUCCESS:
                    ConferenceInfoHandler mConferenceInfoHandler = (ConferenceInfoHandler) msg.obj;
                    ConferenceInfo mConferenceInfo = mConferenceInfoHandler.getConferenceInfo();

                    if (null != mConferenceInfo)
                    {
                        if (!TextUtils.isEmpty(mConferenceInfo.getPic1()))
                            ImageLoader.getInstance().displayImage(Urls.getImgUrl(mConferenceInfo.getPic1()), ivPic1);

                        if (!TextUtils.isEmpty(mConferenceInfo.getPic2()))
                            ImageLoader.getInstance().displayImage(Urls.getImgUrl(mConferenceInfo.getPic2()), ivPic2);

                        if (!TextUtils.isEmpty(mConferenceInfo.getPic3()))
                            ImageLoader.getInstance().displayImage(Urls.getImgUrl(mConferenceInfo.getPic3()), ivPic3);

                        if (!TextUtils.isEmpty(mConferenceInfo.getPic4()))
                            ImageLoader.getInstance().displayImage(Urls.getImgUrl(mConferenceInfo.getPic4()), ivPic4);

                        if (!TextUtils.isEmpty(mConferenceInfo.getPic5()))
                            ImageLoader.getInstance().displayImage(Urls.getImgUrl(mConferenceInfo.getPic5()), ivPic5);

                        if (!TextUtils.isEmpty(mConferenceInfo.getPic6()))
                            ImageLoader.getInstance().displayImage(Urls.getImgUrl(mConferenceInfo.getPic6()), ivPic6);
                    }

                    break;

                case UPLOAD_PIC_SUCCESS:
                    ToastUtil.show(EditPicActivity.this, "上传成功");
                    loadData();
                    break;
            }
        }
    };


    @Override
    protected void initData()
    {
        id = getIntent().getStringExtra("ID");
        type = getIntent().getStringExtra("TYPE");
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_pic_edit);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        tvPic1.setOnClickListener(this);
        tvPic2.setOnClickListener(this);
        tvPic3.setOnClickListener(this);
        tvPic4.setOnClickListener(this);
        tvPic5.setOnClickListener(this);
        tvPic6.setOnClickListener(this);
        ivPic1.setOnClickListener(this);
        ivPic2.setOnClickListener(this);
        ivPic3.setOnClickListener(this);
        ivPic4.setOnClickListener(this);
        ivPic5.setOnClickListener(this);
        ivPic6.setOnClickListener(this);

    }

    @Override
    protected void initViewData()
    {
        tvTitle.setText("图片管理");
        setStatusBarTextDeep(false);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));

        ivPic1.post(new Runnable()
        {
            @Override
            public void run()
            {
                int width = ivPic1.getWidth();


                ivPic1.setLayoutParams(new FrameLayout.LayoutParams(width, width * 3 / 4));
                ivPic2.setLayoutParams(new FrameLayout.LayoutParams(width, width * 3 / 4));
                ivPic3.setLayoutParams(new FrameLayout.LayoutParams(width, width * 3 / 4));
                ivPic4.setLayoutParams(new FrameLayout.LayoutParams(width, width * 3 / 4));
                ivPic5.setLayoutParams(new FrameLayout.LayoutParams(width, width * 3 / 4));
                ivPic6.setLayoutParams(new FrameLayout.LayoutParams(width, width * 3 / 4));

                tvPic1.setLayoutParams(new FrameLayout.LayoutParams(width, width * 3 / 4));
                tvPic2.setLayoutParams(new FrameLayout.LayoutParams(width, width * 3 / 4));
                tvPic3.setLayoutParams(new FrameLayout.LayoutParams(width, width * 3 / 4));
                tvPic4.setLayoutParams(new FrameLayout.LayoutParams(width, width * 3 / 4));
                tvPic5.setLayoutParams(new FrameLayout.LayoutParams(width, width * 3 / 4));
                tvPic6.setLayoutParams(new FrameLayout.LayoutParams(width, width * 3 / 4));
                loadData();
            }
        });
        mSelectPicturePopupWindow = new SelectPicturePopupWindow(EditPicActivity.this);
        mSelectPicturePopupWindow.setOnSelectedListener(new SelectPicturePopupWindow.OnSelectedListener()
        {
            @Override
            public void OnSelected(View v, int position)
            {
                switch (position)
                {
                    case 0:
                        // "拍照"按钮被点击了
                        takePhoto();
                        break;
                    case 1:
                        // "从相册选择"按钮被点击了
                        pickFromGallery();
                        break;
                    case 2:
                        // "取消"按钮被点击了
                        mSelectPicturePopupWindow.dismissPopupWindow();
                        break;
                }
            }
        });

        mDestinationUri = Uri.fromFile(new File(getCacheDir(), "cropImage.jpeg"));
        mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "photo.jpeg";

    }


    private void loadData()
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("token", ConfigManager.instance().getToken());
        valuePairs.put("uid", ConfigManager.instance().getUserID());
        valuePairs.put("city_value", ConfigManager.instance().getCityValue());
        valuePairs.put("id", id);
        if ("room".equals(type))
        {

            DataRequest.instance().request(EditPicActivity.this, Urls.getRoomInfoUrl(), this, HttpRequest.POST, GET_ROOMINFO, valuePairs,
                    new RoomInfoHandler());
        }
        else
        {

            DataRequest.instance().request(EditPicActivity.this, Urls.getConferenceInfoUrl(), this, HttpRequest.POST, GET_CONFERENCE_INFO, valuePairs,
                    new ConferenceInfoHandler());
        }
    }

    private              String pic             = "";
    private static final String UPLOAD_USER_PIC = "upload_user_pic";
    private SelectPicturePopupWindow mSelectPicturePopupWindow;
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION  = 101;
    protected static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;
    private static final   int GALLERY_REQUEST_CODE                    = 9001;    // 相册选图标记
    private static final   int CAMERA_REQUEST_CODE                     = 9002;    // 相机拍照标记

    // 拍照临时图片
    private String mTempPhotoPath;
    // 剪切后图像文件
    private Uri    mDestinationUri;

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == ivBack)
        {
            finish();
        }
        else if (v == tvPic1 || v == ivPic1)
        {
            pic = "pic1";
            mSelectPicturePopupWindow.showPopupWindow(this);
        }
        else if (v == tvPic2 || v == ivPic2)
        {
            pic = "pic2";
            mSelectPicturePopupWindow.showPopupWindow(this);
        }
        else if (v == tvPic3 || v == ivPic3)
        {
            pic = "pic3";
            mSelectPicturePopupWindow.showPopupWindow(this);
        }
        else if (v == tvPic4 || v == ivPic4)
        {
            pic = "pic4";
            mSelectPicturePopupWindow.showPopupWindow(this);
        }
        else if (v == tvPic5 || v == ivPic5)
        {
            pic = "pic5";
            mSelectPicturePopupWindow.showPopupWindow(this);
        }
        else if (v == tvPic6 || v == ivPic6)
        {
            pic = "pic6";
            mSelectPicturePopupWindow.showPopupWindow(this);
        }

    }


    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog();
        if (GET_ROOMINFO.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_ROOMINFO_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (GET_CONFERENCE_INFO.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_CONFERENCE_INFO_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (UPLOAD_USER_PIC.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(UPLOAD_PIC_SUCCESS, obj));
            }

            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }

    private void takePhoto()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(EditPicActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    getString(R.string.permission_write_storage_rationale),
                    REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
        }
        else
        {
            mSelectPicturePopupWindow.dismissPopupWindow();
            Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //下面这句指定调用相机拍照后的照片存储的路径
            takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mTempPhotoPath)));
            startActivityForResult(takeIntent, CAMERA_REQUEST_CODE);
        }
    }

    private void pickFromGallery()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(EditPicActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.permission_read_storage_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        }
        else
        {
            mSelectPicturePopupWindow.dismissPopupWindow();
            Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
            // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(pickIntent, GALLERY_REQUEST_CODE);
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startCropActivity(Uri uri)
    {
        UCrop.of(uri, mDestinationUri)
                .withAspectRatio(4, 3)
                .withMaxResultSize(800, 600)
                .withTargetActivity(CropActivity.class)
                .start(EditPicActivity.this);
    }

    //将URI文件转化为FILE文件
    public String getRealPathFromURI(Uri uri)
    {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme))
        {
            data = uri.getPath();
        }
        else if (ContentResolver.SCHEME_CONTENT.equals(scheme))
        {
            Cursor cursor = EditPicActivity.this.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor)
            {
                if (cursor.moveToFirst())
                {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1)
                    {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 处理剪切成功的返回值
     *
     * @param result
     */
    private void handleCropResult(Intent result)
    {
        //  deleteTempPhotoFile();
        final Uri resultUri = UCrop.getOutput(result);
        if (null != resultUri)
        {
            //TODO 这个地方处理图片上传操作
            try
            {

                File mFile = new File(new URI(resultUri.toString()));
                Map<String, String> valuePairs = new HashMap<>();
                valuePairs.put("uid", ConfigManager.instance().getUserID());
                valuePairs.put("token", ConfigManager.instance().getToken());
                valuePairs.put("role", "2");
                valuePairs.put("id", id);
                valuePairs.put("type", type);
                valuePairs.put("pic", pic);
                valuePairs.put("merchant_id", ConfigManager.instance().getMerchantId());
                valuePairs.put("city_value", ConfigManager.instance().getCityValue());
                valuePairs.put("submit", "Submit");
                DataRequest.instance().request(EditPicActivity.this, Urls.getUploadImgUrl(), this, HttpRequest.UPLOAD, UPLOAD_USER_PIC, valuePairs, mFile,
                        new ResultHandler());
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            ToastUtil.show(EditPicActivity.this, "无法剪切选择图片");
        }
    }


    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case REQUEST_STORAGE_READ_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    pickFromGallery();
                }
                break;
            case REQUEST_STORAGE_WRITE_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    takePhoto();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    /**
     * 删除拍照临时文件
     */
    private void deleteTempPhotoFile()
    {
        File tempFile = new File(mTempPhotoPath);
        if (tempFile.exists() && tempFile.isFile())
        {
            tempFile.delete();
        }
    }

    /**
     * 处理剪切失败的返回值
     *
     * @param result
     */
    private void handleCropError(Intent result)
    {
        //  deleteTempPhotoFile();
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null)
        {
            ToastUtil.show(EditPicActivity.this, cropError.getMessage());
        }
        else
        {
            ToastUtil.show(EditPicActivity.this, "无法剪切选择图片");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            switch (requestCode)
            {
                case CAMERA_REQUEST_CODE:   // 调用相机拍照
                    File temp = new File(mTempPhotoPath);
                    startCropActivity(Uri.fromFile(temp));
                    break;
                case GALLERY_REQUEST_CODE:  // 直接从相册获取
                    startCropActivity(data.getData());
                    break;
                case UCrop.REQUEST_CROP:    // 裁剪图片结果
                    handleCropResult(data);
                    break;
                case UCrop.RESULT_ERROR:    // 裁剪图片错误
                    handleCropError(data);
            }
        }
    }


}
