package com.twlrg.twsl.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.tencent.imsdk.TIMConnListener;
import com.tencent.imsdk.TIMManager;
import com.twlrg.twsl.MyApplication;
import com.twlrg.twsl.R;
import com.twlrg.twsl.fragment.HomeFragment;
import com.twlrg.twsl.fragment.MessageFragment;
import com.twlrg.twsl.fragment.OrderFragment;
import com.twlrg.twsl.fragment.OrderListFragment;
import com.twlrg.twsl.fragment.UserCenterFragment;
import com.twlrg.twsl.fragment.UserCenterFragment1;
import com.twlrg.twsl.service.LocationService;
import com.twlrg.twsl.utils.ConfigManager;
import com.twlrg.twsl.utils.DialogUtils;
import com.twlrg.twsl.utils.LogUtil;
import com.twlrg.twsl.utils.StringUtils;
import com.twlrg.twsl.utils.ToastUtil;
import com.twlrg.twsl.utils.VersionManager;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;

public class MainActivity extends BaseActivity
{

    @BindView(android.R.id.tabhost)
    FragmentTabHost fragmentTabHost;

    private                String texts[]                                   = {"首页", "消息", "订单", "我的"};
    private                int    imageButton[]                             = {
            R.drawable.ic_home_selector, R.drawable.ic_message_selector,
            R.drawable.ic_order_selector, R.drawable.ic_user_center_selector};
    protected static final int    READ_PHONE_STATE_PERMISSIONS_REQUEST_CODE = 9002;

    private Class fragmentArray[] = {HomeFragment.class, MessageFragment.class, OrderListFragment.class, UserCenterFragment1.class};

    private final String USER_LOGOUT = "USER_LOGOUT";

    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    new VersionManager(MainActivity.this).init();
                    break;
            }
        }
    };

    private MyBroadCastReceiver mMyBroadCastReceiver;

    @Override
    protected void initData()
    {
        mMyBroadCastReceiver = new MyBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(USER_LOGOUT);
        registerReceiver(mMyBroadCastReceiver, intentFilter);
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_main);
        setTranslucentStatus();
        TIMManager.getInstance().getUserConfig().setConnectionListener(new TIMConnListener()
        {
            @Override
            public void onConnected()
            {
                Log.e("TAG", "onConnected、");
            }

            @Override
            public void onDisconnected(int i, String s)
            {
                Log.e("TAG", "onDisconnected、");
                ConfigManager.instance().setUserId("");
            }

            @Override
            public void onWifiNeedAuth(String s)
            {
                Log.e("TAG", "onWifiNeedAuth1111111111111111111111111111111111111111111111111");
            }
        });

    }

    @Override
    protected void initEvent()
    {
        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener()
        {
            @Override
            public void onTabChanged(String tabId)
            {
                LogUtil.e("TAG", "tabId--->" + tabId);

                if (!"首页".equals(tabId))
                {
                    if (!MyApplication.getInstance().isLogin())
                    {
                        fragmentTabHost.setCurrentTab(0);
                        LoginActivity.start(MainActivity.this, true);
                    }
                }
            }
        });
    }

    @Override
    protected void initViewData()
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                )


        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_PHONE_STATE))
            {
                ToastUtil.show(MainActivity.this, "您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    READ_PHONE_STATE_PERMISSIONS_REQUEST_CODE);
        }
        else
        {
            initMain();
        }
    }


    private void initMain()
    {
        fragmentTabHost.setup(this, getSupportFragmentManager(),
                R.id.main_layout);

        for (int i = 0; i < texts.length; i++)
        {
            TabHost.TabSpec spec = fragmentTabHost.newTabSpec(texts[i]).setIndicator(getView(i));

            fragmentTabHost.addTab(spec, fragmentArray[i], null);

            //设置背景(必须在addTab之后，由于需要子节点（底部菜单按钮）否则会出现空指针异常)
            // fragmentTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.main_tab_selector);
        }
        fragmentTabHost.getTabWidget().setDividerDrawable(R.color.transparent);
        mHandler.sendEmptyMessageDelayed(1, 3 * 1000);
    }

    private View getView(int i)
    {
        //取得布局实例
        View view = View.inflate(MainActivity.this, R.layout.tabcontent, null);
        //取得布局对象
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        TextView textView = (TextView) view.findViewById(R.id.text);

        //设置图标
        imageView.setImageResource(imageButton[i]);
        //设置标题
        textView.setText(texts[i]);
        return view;
    }


    public void changeTabStatusColor(int index)
    {
        if (index == 0)
        {
            setStatusBarTextDeep(false);
        }
        else
        {
            setStatusBarTextDeep(false);
        }
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (null != mMyBroadCastReceiver)
        {
            unregisterReceiver(mMyBroadCastReceiver);
        }

    }


    public int getTabIndex()
    {
        if (null == fragmentTabHost)
        {
            return 0;
        }
        return fragmentTabHost.getCurrentTab();
    }

    class MyBroadCastReceiver extends BroadcastReceiver
    {
        private static final String TAG = "TestBroadCastReceiver";

        @Override
        public void onReceive(Context context, Intent intent)
        {

            if (USER_LOGOUT.contentEquals(intent.getAction()))
            {
                fragmentTabHost.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        fragmentTabHost.setCurrentTab(0);
                        changeTabStatusColor(0);
                    }
                }, 100);

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

            DialogUtils.showToastDialog2Button(MainActivity.this, "是否退出商旅部落", new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    finish();
                }
            });

            return false;
        }
        else
        {
            return super.onKeyDown(keyCode, event);
        }

    }


    public void startLocation()
    {
        //        if (Build.VERSION.SDK_INT >= 23)
        //        {
        //            showContacts();
        //        }
        //        else
        //        {
        openGPSSettings();
        //        }
    }


    private LocationService locationService;
    private int count = 0;

    private void initLocation()
    {
        locationService = new LocationService(this);
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0)
        {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        }
        else if (type == 1)
        {
            locationService.setLocationOption(locationService.getOption());
        }
        locationService.start();// 定位SDK
    }


    private boolean isonReceiveLocation;
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener()
    {

        @Override
        public void onReceiveLocation(BDLocation location)
        {
            LogUtil.e("TAG", "1111111111111111111111111");

            if (null != location && location.getLocType() != BDLocation.TypeServerError && !isonReceiveLocation)
            {
                isonReceiveLocation = true;
                LogUtil.e("TAG", "22222222222222222222222");
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());
                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");// 经度
                sb.append(location.getLongitude());
                sb.append("\nradius : ");// 半径
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");// 国家码
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");// 国家名称
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");// 城市编码
                sb.append(location.getCityCode());
                sb.append("\ncity : ");// 城市
                sb.append(location.getCity());
                sb.append("\nDistrict : ");// 区
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");// 街道
                sb.append(location.getStreet());
                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());
                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
                sb.append(location.getUserIndoorState());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());// 方向
                sb.append("\nlocationdescribe: ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                sb.append("\nPoi: ");// POI信息


                double lat = location.getLatitude();
                double lng = location.getLongitude();
                String currentCity = location.getCity();


                if (location.getPoiList() != null && !location.getPoiList().isEmpty())
                {
                    for (int i = 0; i < location.getPoiList().size(); i++)
                    {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation)
                {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 速度 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());// 卫星数目
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 海拔高度 单位：米
                    sb.append("\ngps status : ");
                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                }
                else if (location.getLocType() == BDLocation.TypeNetWorkLocation)
                {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude())
                    {// *****如果有海拔高度*****
                        sb.append("\nheight : ");
                        sb.append(location.getAltitude());// 单位：米
                    }
                    sb.append("\noperationers : ");// 运营商信息
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                }
                else if (location.getLocType() == BDLocation.TypeOffLineLocation)
                {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                }
                else if (location.getLocType() == BDLocation.TypeServerError)
                {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                }
                else if (location.getLocType() == BDLocation.TypeNetWorkException)
                {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                }
                else if (location.getLocType() == BDLocation.TypeCriteriaException)
                {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }


                LogUtil.e("TAG", sb.toString());

                //定位结束
                locationService.unregisterListener(mListener); //注销掉监听
                locationService.stop(); //停止定位服务

                if(StringUtils.stringIsEmpty(currentCity))
                {
                    currentCity="深圳市";
                }

                if(lat==4.9E-324)
                {
                   lng = 114.2496;
                   lat = 22.737045;
                }
                sendBroadcast(new Intent("TWSL_LOCATION_RESULT")
                        .putExtra("LAT", lat)
                        .putExtra("LNG", lng)
                        .putExtra("CURRENT_CITY", currentCity)

                );
            }
            else
            {

                locationService.unregisterListener(mListener); //注销掉监听
                locationService.stop(); //停止定位服务
                sendBroadcast(new Intent("TWSL_LOCATION_RESULT")
                        .putExtra("LAT", 0)
                        .putExtra("LNG", 0)
                        .putExtra("CURRENT_CITY", "深圳市")

                );
            }
        }

    };


    private static final int BAIDU_READ_PHONE_STATE = 100;

    public void showContacts()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ToastUtil.show(this, "没有权限,请手动开启定位权限");
            openGPSSettings();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission
                    .ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, BAIDU_READ_PHONE_STATE);
        }
        else
        {
            openGPSSettings();
        }
    }


    //Android6.0申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    openGPSSettings();
                }
                else
                {
                    // 没有获取到权限，做特殊处理
                    ToastUtil.show(MainActivity.this, "没有权限,请手动开启定位权限");
                    openGPSSettings();
                }
                break;
            case READ_PHONE_STATE_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED
                        && grantResults[3] == PackageManager.PERMISSION_GRANTED
                        && grantResults[4] == PackageManager.PERMISSION_GRANTED
                        )
                {
                    initMain();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private int GPS_REQUEST_CODE = 10;
    //private Dialog mToastDialog;

    /**
     * 检测GPS是否打开
     *
     * @return
     */
    private boolean checkGPSIsOpen()
    {
        boolean isOpen;
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
        return isOpen;
    }


    public boolean isLocationEnabled()
    {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            try
            {
                locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e)
            {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        }
        else
        {
            locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    /**
     * 跳转GPS设置
     */
    private void openGPSSettings()
    {
        mHandler.post(new Runnable() {
            @Override
            public void run()
            {
                if (isLocationEnabled())
                {

                    if (count == 0)
                    {
                        count++;
                        initLocation();

                    }
                }
                else
                {
                    //            if (mToastDialog != null)
                    //            {
                    //                mToastDialog.show();
                    //            }
                    //            else
                    //            {
                    Dialog   mToastDialog = DialogUtils.showToastDialog2Button(MainActivity.this, "请打开定位功能", new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, GPS_REQUEST_CODE);
                        }
                    }, new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            openGPSSettings();
                        }
                    });
                    mToastDialog.show();
                    //            }

                }
            }
        });

    }


    @Override
    protected void onResume()
    {
        super.onResume();
//        if (null != mToastDialog)
//        {
//            mToastDialog.dismiss();
//        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPS_REQUEST_CODE)
        {
            openGPSSettings();
        }

    }



}
