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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kevin.crop.UCrop;
import com.twlrg.twsl.R;
import com.twlrg.twsl.adapter.HotelImgAdapter;
import com.twlrg.twsl.entity.HotelImgInfo;
import com.twlrg.twsl.http.DataRequest;
import com.twlrg.twsl.http.HttpRequest;
import com.twlrg.twsl.http.IRequestListener;
import com.twlrg.twsl.json.HotelImgListHandler;
import com.twlrg.twsl.json.HotelInfoHandler;
import com.twlrg.twsl.json.ResultHandler;
import com.twlrg.twsl.listener.MyItemClickListener;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.ConfigManager;
import com.twlrg.twsl.utils.ConstantUtil;
import com.twlrg.twsl.utils.DialogUtils;
import com.twlrg.twsl.utils.ToastUtil;
import com.twlrg.twsl.utils.Urls;
import com.twlrg.twsl.widget.AutoFitTextView;
import com.twlrg.twsl.widget.EmptyDecoration;
import com.twlrg.twsl.widget.SelectPicturePopupWindow;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/5/30 14:48
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class HotelImgActivity extends BaseActivity implements IRequestListener
{


    @BindView(R.id.topView)
    View            topView;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView    recyclerView;


    private HotelImgAdapter mHotelImgAdapter;
    private              List<HotelImgInfo> hotelImgInfoList = new ArrayList<>();
    private static final String             GET_HOTEL_INFO   = "get_hotel_info";
    private static final String             DEL_HOTEL_IMG    = "DEL_HOTEL_IMG";

    private static final int         REQUEST_SUCCESS       = 0x01;
    public static final  int         REQUEST_FAIL          = 0x02;
    private static final int         UPLOAD_PIC_SUCCESS    = 0x03;
    private static final int         DEL_HOTEL_IMG_SUCCESS = 0x05;
    @SuppressLint("HandlerLeak")
    private              BaseHandler mHandler              = new BaseHandler(this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:
                    HotelImgListHandler mHotelImgListHandler = (HotelImgListHandler) msg.obj;
                    hotelImgInfoList.clear();
                    hotelImgInfoList.addAll(mHotelImgListHandler.getHotelImgInfoList());
                    mHotelImgAdapter.notifyDataSetChanged();
                    break;


                case REQUEST_FAIL:
                    ToastUtil.show(HotelImgActivity.this, msg.obj.toString());
                    break;
                case UPLOAD_PIC_SUCCESS:
                    ToastUtil.show(HotelImgActivity.this, "上传成功");
                    loadData();
                    break;
                case DEL_HOTEL_IMG_SUCCESS:
                    ToastUtil.show(HotelImgActivity.this, "删除成功");
                    loadData();
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
        setContentView(R.layout.activity_hotel_img);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    @Override
    protected void initViewData()
    {
        setStatusBarTextDeep(true);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));
        tvTitle.setText("酒店图片");

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new EmptyDecoration(this, ""));

        mHotelImgAdapter = new HotelImgAdapter(hotelImgInfoList, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, final int position)
            {
                DialogUtils.showToastDialog2Button(HotelImgActivity.this, "是否删除该图片", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        delImg(hotelImgInfoList.get(position - 1).getId());
                    }
                });
            }
        }, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                mSelectPicturePopupWindow.showPopupWindow(HotelImgActivity.this);
            }
        });
        recyclerView.setAdapter(mHotelImgAdapter);


        mSelectPicturePopupWindow = new SelectPicturePopupWindow(HotelImgActivity.this);
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
        loadData();

    }


    private void loadData()
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("merchant_id", ConfigManager.instance().getMerchantId());
        valuePairs.put("city_value", ConfigManager.instance().getCityValue());
        DataRequest.instance().request(this, Urls.getHotelImgUrl(), this, HttpRequest.POST, GET_HOTEL_INFO, valuePairs,
                new HotelImgListHandler());
    }

    private void delImg(String id)
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("id", id);
        valuePairs.put("merchant_id", ConfigManager.instance().getMerchantId());
        valuePairs.put("city_value", ConfigManager.instance().getCityValue());
        DataRequest.instance().request(this, Urls.getDelHotelImgUrl(), this, HttpRequest.POST, DEL_HOTEL_IMG, valuePairs,
                new ResultHandler());
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog();
        if (GET_HOTEL_INFO.equals(action))
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
        else if (DEL_HOTEL_IMG.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(DEL_HOTEL_IMG_SUCCESS, obj));
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
                && ActivityCompat.checkSelfPermission(HotelImgActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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


    private              String pic             = "pic";
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

    private void pickFromGallery()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(HotelImgActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
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
                .start(HotelImgActivity.this);
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
            Cursor cursor = HotelImgActivity.this.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
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
                valuePairs.put("type", "wg");
                valuePairs.put("pic", pic);
                valuePairs.put("id", ConfigManager.instance().getMerchantId());
                valuePairs.put("merchant_id", ConfigManager.instance().getMerchantId());
                valuePairs.put("city_value", ConfigManager.instance().getCityValue());
                valuePairs.put("submit", "Submit");
                DataRequest.instance().request(HotelImgActivity.this, Urls.getUploadImgUrl(), this, HttpRequest.UPLOAD, UPLOAD_USER_PIC, valuePairs, mFile,
                        new ResultHandler());
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            ToastUtil.show(HotelImgActivity.this, "无法剪切选择图片");
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
            ToastUtil.show(HotelImgActivity.this, cropError.getMessage());
        }
        else
        {
            ToastUtil.show(HotelImgActivity.this, "无法剪切选择图片");
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
