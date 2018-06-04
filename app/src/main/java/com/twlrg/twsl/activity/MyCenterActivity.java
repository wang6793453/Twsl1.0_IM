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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kevin.crop.UCrop;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.twlrg.twsl.MyApplication;
import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.UserInfo;
import com.twlrg.twsl.http.DataRequest;
import com.twlrg.twsl.http.HttpRequest;
import com.twlrg.twsl.http.IRequestListener;
import com.twlrg.twsl.im.TencentCloud;
import com.twlrg.twsl.json.ResultHandler;
import com.twlrg.twsl.json.UserInfoHandler;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.ConfigManager;
import com.twlrg.twsl.utils.ConstantUtil;
import com.twlrg.twsl.utils.DialogUtils;
import com.twlrg.twsl.utils.StringUtils;
import com.twlrg.twsl.utils.ToastUtil;
import com.twlrg.twsl.utils.Urls;
import com.twlrg.twsl.widget.CircleImageView;
import com.twlrg.twsl.widget.SelectPicturePopupWindow;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/5/23 15:17
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class MyCenterActivity extends BaseActivity implements IRequestListener
{


    @BindView(R.id.topView)
    View            topView;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_edit)
    TextView        tvEdit;
    @BindView(R.id.iv_user_head)
    CircleImageView ivUserHead;
    @BindView(R.id.tv_change_head)
    TextView        tvChangeHead;
    @BindView(R.id.tv_hotel)
    TextView        tvHotel;
    @BindView(R.id.tv_userName)
    TextView        tvUserName;
    @BindView(R.id.et_position)
    EditText        etPosition;
    @BindView(R.id.tv_userPhone)
    TextView        tvUserPhone;
    @BindView(R.id.tv_modify_pwd)
    TextView        tvModifyPwd;
    @BindView(R.id.tv_role_type1)
    TextView        tvRoleType1;
    @BindView(R.id.tv_role_type2)
    TextView        tvRoleType2;
    @BindView(R.id.tv_role_type3)
    TextView        tvRoleType3;
    @BindView(R.id.tv_role_type4)
    TextView        tvRoleType4;
    @BindView(R.id.btn_logout)
    Button          btnLogout;
    private int mEditStatus;


    private List<TextView> mTextViewList = new ArrayList<>();


    private static final int REQUEST_SUCCESS          = 0x01;
    public static final  int REQUEST_FAIL             = 0x02;
    private static final int UPLOAD_PIC_SUCCESS       = 0x03;
    private static final int INIT_ONRESUME            = 0x04;
    private static final int UPDATE_USER_INFO_SUCCESS = 0x05;

    private static final String GET_USER_INFO    = "get_user_info";
    private static final String UPLOAD_USER_PIC  = "upload_user_pic";
    private static final String UPDATE_USER_INFO = "update_user_info";
    private SelectPicturePopupWindow mSelectPicturePopupWindow;
    private                Bitmap bitmap                                  = null;
    protected static final int    REQUEST_STORAGE_READ_ACCESS_PERMISSION  = 101;
    protected static final int    REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;
    private static final   int    GALLERY_REQUEST_CODE                    = 9001;    // 相册选图标记
    private static final   int    CAMERA_REQUEST_CODE                     = 9002;    // 相机拍照标记

    // 拍照临时图片
    private String mTempPhotoPath;
    // 剪切后图像文件
    private Uri    mDestinationUri;

    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(MyCenterActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {


                case REQUEST_SUCCESS:
                    UserInfoHandler mUserInfoHandler = (UserInfoHandler) msg.obj;
                    UserInfo mUserInfo = mUserInfoHandler.getUserInfo();


                    if (null != mUserInfo)
                    {
                        tvHotel.setText(mUserInfo.getHotel());
                        tvUserName.setText(mUserInfo.getName());
                        etPosition.setText(mUserInfo.getPosition());
                        tvUserPhone.setText(mUserInfo.getMobile());
                        ImageLoader.getInstance().displayImage(Urls.getImgUrl(mUserInfo.getPortrait()), ivUserHead);

                        String role_type = mUserInfo.getRole_type();

                        if (!StringUtils.stringIsEmpty(role_type))
                        {
                            String[] type = role_type.split(",");


                            for (int i = 0; i < type.length; i++)
                            {
                                if (StringUtils.isNumber(type[i]))
                                {
                                    setRoleType(Integer.parseInt(type[i]));
                                }

                            }
                        }


                    }

                    break;


                case REQUEST_FAIL:
                    ToastUtil.show(MyCenterActivity.this, msg.obj.toString());
                    break;

                case UPLOAD_PIC_SUCCESS:
                    ToastUtil.show(MyCenterActivity.this, "保存成功");
                    loadData();
                    break;

                case UPDATE_USER_INFO_SUCCESS:
                    ToastUtil.show(MyCenterActivity.this, "保存成功");
                    showEditStatus(false);
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
        setContentView(R.layout.activity_user_center);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        tvEdit.setOnClickListener(this);
        tvModifyPwd.setOnClickListener(this);
        tvChangeHead.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        ivUserHead.setOnClickListener(this);
        tvRoleType1.setOnClickListener(this);
        tvRoleType2.setOnClickListener(this);
        tvRoleType3.setOnClickListener(this);
        tvRoleType4.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        showEditStatus(false);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(MyCenterActivity.this)));
        setStatusBarTextDeep(false);

        mSelectPicturePopupWindow = new SelectPicturePopupWindow(MyCenterActivity.this);
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


        mTextViewList.add(tvRoleType1);
        mTextViewList.add(tvRoleType2);
        mTextViewList.add(tvRoleType3);
        mTextViewList.add(tvRoleType4);

        tvRoleType1.setEnabled(false);
        tvRoleType2.setEnabled(false);
        tvRoleType3.setEnabled(false);
        tvRoleType4.setEnabled(false);
        loadData();

    }


    private void setRoleType(int p)
    {
        for (int i = 0; i < mTextViewList.size(); i++)
        {
            if (i == p - 1)
            {
                mTextViewList.get(i).setSelected(true);
            }
        }
    }


    private void changeRoleTypeSelected(int p)
    {
        if (mTextViewList.get(p).isSelected())
        {
            mTextViewList.get(p).setSelected(false);
        }
        else
        {
            mTextViewList.get(p).setSelected(true);
        }
    }

    private void loadData()
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("uid", ConfigManager.instance().getUserID());
        valuePairs.put("token", ConfigManager.instance().getToken());
        DataRequest.instance().request(MyCenterActivity.this, Urls.getUserInfoUrl(), this, HttpRequest.POST, GET_USER_INFO, valuePairs,
                new UserInfoHandler());
    }

    private void showEditStatus(boolean isEdit)
    {
        if (isEdit)
        {
            tvEdit.setText("保存");
            etPosition.setEnabled(true);
            tvRoleType1.setEnabled(true);
            tvRoleType2.setEnabled(true);
            tvRoleType3.setEnabled(true);
            tvRoleType4.setEnabled(true);
            mEditStatus = 1;
        }
        else
        {
            etPosition.setEnabled(false);
            tvRoleType1.setEnabled(false);
            tvRoleType2.setEnabled(false);
            tvRoleType3.setEnabled(false);
            tvRoleType4.setEnabled(false);
            tvEdit.setText("编辑");
            mEditStatus = 0;
        }
    }


    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == ivBack)
        {
            finish();
        }
        else if (v == tvEdit)
        {
            if (mEditStatus == 0)
            {
                showEditStatus(true);
            }
            else
            {
                String mPosition = etPosition.getText().toString();

                if (StringUtils.stringIsEmpty(mPosition))
                {
                    ToastUtil.show(this, "职位不能为空");
                    return;
                }


                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < mTextViewList.size(); i++)
                {

                    if (mTextViewList.get(i).isSelected())
                    {
                        sb.append(i + 1);
                        sb.append(",");
                    }

                }

                if (StringUtils.stringIsEmpty(sb.toString()))
                {
                    ToastUtil.show(this, "请选择业务范围");
                    return;
                }

                //TODO 执行保存操作  保存成功后 调用  showEditStatus(false);
                showProgressDialog();
                Map<String, String> valuePairs = new HashMap<>();
                valuePairs.put("uid", ConfigManager.instance().getUserID());
                valuePairs.put("token", ConfigManager.instance().getToken());
                valuePairs.put("role", "2");
                valuePairs.put("position", mPosition);
                valuePairs.put("role_type", sb.toString().substring(0, sb.toString().length() - 1));
                DataRequest.instance().request(MyCenterActivity.this, Urls.getUpdateUserInfoUrl(), this, HttpRequest.POST, UPDATE_USER_INFO, valuePairs,
                        new ResultHandler());

            }
        }
        else if (v == btnLogout)
        {

            DialogUtils.showToastDialog2Button(MyCenterActivity.this, "是否确认退出登录", new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    APPUtils.logout(MyCenterActivity.this);
                    TencentCloud.logout();
                    LoginActivity.start(MyCenterActivity.this, true);
                }
            });


        }
        else if (v == tvModifyPwd)
        {
            startActivity(new Intent(MyCenterActivity.this, ModifyPwdActivity.class));
        }
        else if (v == ivUserHead || v == tvChangeHead)
        {
            mSelectPicturePopupWindow.showPopupWindow(MyCenterActivity.this);
        }
        else if (v == tvRoleType1)
        {
            changeRoleTypeSelected(0);
        }
        else if (v == tvRoleType2)
        {
            changeRoleTypeSelected(1);
        }
        else if (v == tvRoleType3)
        {
            changeRoleTypeSelected(2);
        }
        else if (v == tvRoleType4)
        {
            changeRoleTypeSelected(3);
        }
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        if (UPDATE_USER_INFO.equals(action))
        {
            hideProgressDialog();
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(UPDATE_USER_INFO_SUCCESS, obj));
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
        else if (GET_USER_INFO.equals(action))
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
    }


    private void takePhoto()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(MyCenterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
                && ActivityCompat.checkSelfPermission(MyCenterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
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
                .withAspectRatio(1, 1)
                .withMaxResultSize(200, 200)
                .withTargetActivity(CropActivity.class)
                .start(MyCenterActivity.this);
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
            Cursor cursor = MyCenterActivity.this.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
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
                valuePairs.put("submit", "Submit");
                DataRequest.instance().request(MyCenterActivity.this, Urls.getUploadPicUrl(), this, HttpRequest.UPLOAD, UPLOAD_USER_PIC, valuePairs, mFile,
                        new ResultHandler());
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            ToastUtil.show(MyCenterActivity.this, "无法剪切选择图片");
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
            ToastUtil.show(MyCenterActivity.this, cropError.getMessage());
        }
        else
        {
            ToastUtil.show(MyCenterActivity.this, "无法剪切选择图片");
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
