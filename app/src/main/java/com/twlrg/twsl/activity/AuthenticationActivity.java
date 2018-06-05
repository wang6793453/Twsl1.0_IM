package com.twlrg.twsl.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.kevin.crop.UCrop;
import com.twlrg.twsl.R;
import com.twlrg.twsl.http.DataRequest;
import com.twlrg.twsl.http.HttpRequest;
import com.twlrg.twsl.http.IRequestListener;
import com.twlrg.twsl.json.ResultHandler;
import com.twlrg.twsl.utils.ConstantUtil;
import com.twlrg.twsl.utils.ToastUtil;
import com.twlrg.twsl.utils.Urls;
import com.twlrg.twsl.widget.SelectPicturePopupWindow;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/6/5 16:03
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class AuthenticationActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_pic)
    TextView  tvPic;
    @BindView(R.id.iv_pic)
    ImageView ivPic;
    @BindView(R.id.btn_submit)
    Button    btnSubmit;

    private File   mPicFile;
    private String uid;


    private static final int         REQUEST_FAIL       = 0x01;
    private static final int         UPLOAD_PIC_SUCCESS = 0x05;
    @SuppressLint("HandlerLeak")
    private final        BaseHandler mHandler           = new BaseHandler(AuthenticationActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {


                case REQUEST_FAIL:
                    ToastUtil.show(AuthenticationActivity.this, msg.obj.toString());

                    break;


                case UPLOAD_PIC_SUCCESS:
                    ToastUtil.show(AuthenticationActivity.this, "上传成功");
                    break;
            }
        }
    };


    @Override
    protected void initData()
    {
        uid = getIntent().getStringExtra("uid");
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_authentication);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        ivPic.setOnClickListener(this);
        tvPic.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        tvPic.post(new Runnable()
        {
            @Override
            public void run()
            {
                int width = tvPic.getWidth();
                ivPic.setLayoutParams(new FrameLayout.LayoutParams(width, width * 3 / 4));
                tvPic.setLayoutParams(new FrameLayout.LayoutParams(width, width * 3 / 4));


            }
        });


        mSelectPicturePopupWindow = new SelectPicturePopupWindow(AuthenticationActivity.this);
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


    @Override
    public void onClick(View v)
    {
        super.onClick(v);

        if (v == ivBack)
        {
            finish();
        }
        else if (v == ivPic || v == tvPic)
        {
            mSelectPicturePopupWindow.showPopupWindow(this);
        }
        else if (v == btnSubmit)
        {
            if (null != mPicFile)
            {
                Map<String, String> valuePairs = new HashMap<>();
                valuePairs.put("uid", uid);
                valuePairs.put("md_img", "md_img");
                valuePairs.put("submit", "Submit");
                DataRequest.instance().request(AuthenticationActivity.this, Urls.getBusinessCardUrl(), this, HttpRequest.UPLOAD, UPLOAD_USER_PIC, valuePairs,
                        mPicFile,
                        new ResultHandler());
            }
            else
            {
                ToastUtil.show(this, "请选择要上传的名片");
            }
        }
    }


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


    private void takePhoto()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(AuthenticationActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
                && ActivityCompat.checkSelfPermission(AuthenticationActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
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
                .start(AuthenticationActivity.this);
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
            Cursor cursor = AuthenticationActivity.this.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
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

    private Bitmap getBitmapFromUri(Uri uri)
    {
        try
        {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e)
        {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
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
                mPicFile = new File(new URI(resultUri.toString()));

                Bitmap mBitmap = getBitmapFromUri(resultUri);

                if (null != mBitmap)
                {
                    ivPic.setImageBitmap(mBitmap);
                }

            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            ToastUtil.show(AuthenticationActivity.this, "无法剪切选择图片");
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
            ToastUtil.show(AuthenticationActivity.this, cropError.getMessage());
        }
        else
        {
            ToastUtil.show(AuthenticationActivity.this, "无法剪切选择图片");
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

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        if (UPLOAD_USER_PIC.equals(action))
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
}
