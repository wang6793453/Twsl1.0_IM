package com.twlrg.twsl.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kevin.crop.UCrop;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.twlrg.twsl.MyApplication;
import com.twlrg.twsl.R;
import com.twlrg.twsl.activity.BaseHandler;
import com.twlrg.twsl.activity.CropActivity;
import com.twlrg.twsl.activity.LoginActivity;
import com.twlrg.twsl.activity.MainActivity;
import com.twlrg.twsl.activity.ModifyPwdActivity;
import com.twlrg.twsl.http.DataRequest;
import com.twlrg.twsl.http.HttpRequest;
import com.twlrg.twsl.http.IRequestListener;
import com.twlrg.twsl.im.TencentCloud;
import com.twlrg.twsl.json.ResultHandler;
import com.twlrg.twsl.listener.MyItemClickListener;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者：王先云 on 2018/4/12 16:50
 * 邮箱：wangxianyun1@163.com
 * 描述：个人中心
 */
public class UserCenterFragment extends BaseFragment implements View.OnClickListener, IRequestListener
{

    @BindView(R.id.tv_edit)
    TextView        tvEdit;
    @BindView(R.id.tv_cancel)
    TextView        tvCancel;
    @BindView(R.id.iv_user_head)
    CircleImageView ivUserHead;
    @BindView(R.id.et_nickName)
    EditText        etNickName;
    @BindView(R.id.tv_account)
    TextView        tvAccount;
    @BindView(R.id.et_userName)
    EditText        etUserName;
    @BindView(R.id.tv_userSex)
    TextView        tvUserSex;
    @BindView(R.id.tv_userPhone)
    TextView        tvUserPhone;
    @BindView(R.id.et_userEmail)
    EditText        etUserEmail;
    @BindView(R.id.tv_modify_pwd)
    TextView        tvModifyPwd;
    @BindView(R.id.tv_version)
    TextView        tvVersion;
    @BindView(R.id.btn_logout)
    Button          btnLogout;
    @BindView(R.id.topView)
    View            topView;
    private View rootView = null;
    private Unbinder unbinder;

    private int mEditStatus;
    private int sexType;

    private static final int REQUEST_SUCCESS    = 0x01;
    public static final  int REQUEST_FAIL       = 0x02;
    private static final int UPLOAD_PIC_SUCCESS = 0x03;
    private static final int INIT_ONRESUME      = 0x04;


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

    private Context mContext;

    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(getActivity())
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {


                case REQUEST_SUCCESS:

                    ConfigManager.instance().setUserEmail(etUserEmail.getText().toString());
                    ConfigManager.instance().setUserSex(sexType);
                    ConfigManager.instance().setUserName(etUserName.getText().toString());
                    ConfigManager.instance().setUserNickName(etNickName.getText().toString());
                    showEditStatus(false);
                    ToastUtil.show(mContext, "保存成功");

                    break;


                case REQUEST_FAIL:
                    ToastUtil.show(mContext, msg.obj.toString());
                    break;

                case UPLOAD_PIC_SUCCESS:
                    ToastUtil.show(MyApplication.getInstance().getBaseContext(), "保存成功");

                    ResultHandler mResultHandler = (ResultHandler) msg.obj;

                    String data = mResultHandler.getData();
                    if (!StringUtils.stringIsEmpty(data))
                    {
                        //ImageLoader.getInstance().displayImage(Urls.getImgUrl(data), ivUserHead);
                        ConfigManager.instance().setUserPic(data);
                    }
                    break;

                case INIT_ONRESUME:

                    if (((MainActivity) getActivity()).getTabIndex() == 3)
                    {

                        ((MainActivity) getActivity()).changeTabStatusColor(3);
                        if (!MyApplication.getInstance().isLogin())
                        {
                            LoginActivity.start(getActivity(), true);
                        }
                        else
                        {

                            ImageLoader.getInstance().displayImage(Urls.getImgUrl(ConfigManager.instance().getUserPic()), ivUserHead);
                            etNickName.setText(ConfigManager.instance().getUserNickName());
                            tvAccount.setText(ConfigManager.instance().getMobile());
                            etUserName.setText(ConfigManager.instance().getUserName());

                            int sex = ConfigManager.instance().getUserSex();

                            if (sex == 0)
                            {
                                tvUserSex.setText("保密");
                            }
                            else if (sex == 1)
                            {
                                tvUserSex.setText("男");
                            }
                            else
                            {
                                tvUserSex.setText("女");
                            }

                            tvUserPhone.setText(ConfigManager.instance().getMobile());
                            etUserEmail.setText(ConfigManager.instance().getUserEmail());
                            tvVersion.setText("版本：V" + APPUtils.getVersionName(getActivity()));
                        }
                        showEditStatus(false);
                    }
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (rootView == null)
        {
            rootView = inflater.inflate(R.layout.fragment_user_center, null);
            unbinder = ButterKnife.bind(this, rootView);
            initData();
            initViews();
            initViewData();
            initEvent();
        }
        // 缓存的rootView需要判断是否已经被加过parent
        // 如果有parent需要从parent删除，否则会发生这个rootView已经有parent的错误
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null)
        {
            parent.removeView(rootView);
        }

        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mHandler.sendEmptyMessageDelayed(INIT_ONRESUME, 200);
    }

    @Override
    protected void initData()
    {

    }

    @Override
    protected void initViews()
    {

    }

    @Override
    protected void initEvent()
    {
        tvEdit.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvModifyPwd.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        tvUserSex.setOnClickListener(this);
        ivUserHead.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        mContext = getActivity();
        showEditStatus(false);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(getActivity())));
        mSelectPicturePopupWindow = new SelectPicturePopupWindow(getActivity());
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

        mDestinationUri = Uri.fromFile(new File(getActivity().getCacheDir(), "cropImage.jpeg"));
        mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "photo.jpeg";
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (null != unbinder)
        {
            unbinder.unbind();
            unbinder = null;
        }
    }


    private void showEditStatus(boolean isEdit)
    {
        if (isEdit)
        {
            tvCancel.setVisibility(View.VISIBLE);
            tvEdit.setText("保存");
            mEditStatus = 1;
            etNickName.setEnabled(true);
            etUserName.setEnabled(true);
            etUserEmail.setEnabled(true);
            tvUserSex.setEnabled(true);
        }
        else
        {
            tvCancel.setVisibility(View.GONE);
            tvEdit.setText("编辑");
            mEditStatus = 0;
            etNickName.setEnabled(false);
            etUserName.setEnabled(false);
            etUserEmail.setEnabled(false);
            tvUserSex.setEnabled(false);
        }
    }


    @Override
    public void onClick(View v)
    {

        if (v == tvCancel)
        {
            showEditStatus(false);
        }
        else if (v == tvEdit)
        {
            if (mEditStatus == 0)
            {
                showEditStatus(true);
            }
            else
            {
                //TODO 执行保存操作  保存成功后 调用  showEditStatus(false);

                String nickname = etNickName.getText().toString();
                String name = etUserName.getText().toString();
                String email = etUserEmail.getText().toString();


                if (StringUtils.stringIsEmpty(nickname))
                {
                    ToastUtil.show(getActivity(), "请输入昵称");
                    return;
                }
                if (StringUtils.stringIsEmpty(name))
                {
                    ToastUtil.show(getActivity(), "请输入姓名");
                    return;
                }

                if (!StringUtils.checkEmail(email))
                {
                    ToastUtil.show(getActivity(), "请输入正确的邮箱");
                    return;
                }
                ((MainActivity) getActivity()).showProgressDialog();
                Map<String, String> valuePairs = new HashMap<>();
                valuePairs.put("uid", ConfigManager.instance().getUserID());
                valuePairs.put("token", ConfigManager.instance().getToken());
                valuePairs.put("role", "1");
                valuePairs.put("nickname", nickname);
                valuePairs.put("sex", sexType + "");
                valuePairs.put("email", email);
                valuePairs.put("name", name);
                DataRequest.instance().request(getActivity(), Urls.getUpdateUserInfoUrl(), this, HttpRequest.POST, UPDATE_USER_INFO, valuePairs,
                        new ResultHandler());

            }
        }
        else if (v == btnLogout)
        {
            APPUtils.logout(getActivity());
            TencentCloud.logout();
            LoginActivity.start(getActivity(), true);

        }
        else if (v == tvModifyPwd)
        {
            startActivity(new Intent(getActivity(), ModifyPwdActivity.class));
        }
        else if (v == tvUserSex)
        {
            String[] sexArr = getActivity().getResources().getStringArray(R.array.sexType);
            DialogUtils.showCategoryDialog(getActivity(), Arrays.asList(sexArr), new MyItemClickListener()
            {
                @Override
                public void onItemClick(View view, int position)
                {
                    sexType = position;

                    if (sexType == 0)
                    {
                        tvUserSex.setText("保密");
                    }
                    else if (sexType == 1)
                    {
                        tvUserSex.setText("男");
                    }
                    else
                    {
                        tvUserSex.setText("女");
                    }
                }
            });
        }
        else if (v == ivUserHead)
        {
            mSelectPicturePopupWindow.showPopupWindow(getActivity());
        }

    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        if (UPDATE_USER_INFO.equals(action))
        {
            ((MainActivity) getActivity()).hideProgressDialog();
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
    }


    private void takePhoto()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ((MainActivity) getActivity()).requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
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
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ((MainActivity) getActivity()).requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
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
                .start(getActivity());
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
            Cursor cursor = getActivity().getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
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
            //            try
            //            {
            //               bitmap = MediaStore.Images.Media.getBitmap(((MainActivity) getActivity()).getContentResolver(), resultUri);
            //            } catch (FileNotFoundException e)
            //            {
            //                e.printStackTrace();
            //            } catch (IOException e)
            //            {
            //                e.printStackTrace();
            //            }
            //TODO 这个地方处理图片上传操作
            try
            {

                File mFile = new File(new URI(resultUri.toString()));
                Map<String, String> valuePairs = new HashMap<>();
                valuePairs.put("uid", ConfigManager.instance().getUserID());
                valuePairs.put("token", ConfigManager.instance().getToken());
                valuePairs.put("role", "1");
                valuePairs.put("submit", "Submit");
                DataRequest.instance().request(getActivity(), Urls.getUploadPicUrl(), this, HttpRequest.UPLOAD, UPLOAD_USER_PIC, valuePairs, mFile,
                        new ResultHandler());
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            ToastUtil.show(getActivity(), "无法剪切选择图片");
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
            ToastUtil.show(getActivity(), cropError.getMessage());
        }
        else
        {
            ToastUtil.show(getActivity(), "无法剪切选择图片");
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
