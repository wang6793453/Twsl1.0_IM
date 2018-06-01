package com.twlrg.twsl.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.twlrg.twsl.R;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.LogUtil;
import com.twlrg.twsl.utils.ToastUtil;
import com.twlrg.twsl.widget.AutoFitTextView;
import com.twlrg.twsl.widget.SelectMapPopupWindow;


import butterknife.BindView;

/**
 * 作者：王先云 on 2018/4/25 16:30
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class LocationActivity extends BaseActivity
{


    @BindView(R.id.topView)
    View            topView;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView tvTitle;
    @BindView(R.id.bmapView)
    MapView         bmapView;
    BaiduMap mBaiduMap;
    private Marker     mMarkerA;
    private InfoWindow mInfoWindow;
    // 初始化全局 bitmap 信息，不用时及时 recycle
    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_marka);


    private String hotelName;
    private String lat, lng;


    private SelectMapPopupWindow mSelectMapPopupWindow;

    @Override
    protected void initData()
    {
        hotelName = getIntent().getStringExtra("HOTEL_NAME");
        lat = getIntent().getStringExtra("LAT");
        lng = getIntent().getStringExtra("LNG");

        LogUtil.e("TAG", lat + "、" + lng);
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_location);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(new View.OnClickListener()
        {
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
        tvTitle.setText(hotelName);
        setStatusBarTextDeep(false);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));

        mBaiduMap = bmapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
        mBaiduMap.setMapStatus(msu);
        initOverlay();

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener()
        {
            public boolean onMarkerClick(final Marker marker)
            {
                Button button = new Button(getApplicationContext());


                button.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                    }
                });


                button.setBackgroundResource(R.drawable.popup);
                InfoWindow.OnInfoWindowClickListener listener = null;
                if (marker == mMarkerA)
                {
                    button.setText(hotelName);
                    button.setTextColor(Color.BLACK);
                    listener = new InfoWindow.OnInfoWindowClickListener()
                    {
                        public void onInfoWindowClick()
                        {
                            //                            LatLng ll = marker.getPosition();
                            //                            LatLng llNew = new LatLng(ll.latitude + 0.005,
                            //                                    ll.longitude + 0.005);
                            //                            marker.setPosition(llNew);
                            //                            mBaiduMap.hideInfoWindow();

                            if (null != mSelectMapPopupWindow)
                            {
                                mSelectMapPopupWindow.showPopupWindow(LocationActivity.this);
                            }

                        }
                    };
                    LatLng ll = marker.getPosition();
                    mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, listener);
                    mBaiduMap.showInfoWindow(mInfoWindow);
                }
                return true;
            }
        });


        mSelectMapPopupWindow = new SelectMapPopupWindow(LocationActivity.this, new SelectMapPopupWindow.OnSelectedListener()
        {
            @Override
            public void OnSelected(View v, int position)
            {
                switch (position)
                {
                    case 0:
                        if (APPUtils.isAppInstall(LocationActivity.this, "com.autonavi.minimap"))
                        {
                            openGaoDeNavi();
                        }
                        else
                        {
                            ToastUtil.show(LocationActivity.this, "未安装高德地图");
                        }
                        break;
                    case 1:
                        if (APPUtils.isAppInstall(LocationActivity.this, "com.baidu.BaiduMap"))
                        {
                            openBaiduNavi();
                        }
                        else
                        {
                            ToastUtil.show(LocationActivity.this, "未安装百度地图");
                        }
                        break;
                    case 2:
                        mSelectMapPopupWindow.dismissPopupWindow();
                        break;
                }
            }
        });

    }


    /**
     * 启动高德App进行导航
     * sourceApplication 必填 第三方调用应用名称。如 amap
     * poiname           非必填 POI 名称
     * dev               必填 是否偏移(0:lat 和 lon 是已经加密后的,不需要国测加密; 1:需要国测加密)
     * style             必填 导航方式(0 速度快; 1 费用少; 2 路程短; 3 不走高速；4 躲避拥堵；5 不走高速且避免收费；6 不走高速且躲避拥堵；7 躲避收费和拥堵；8 不走高速躲避收费和拥堵))
     */
    private void openGaoDeNavi()
    {
        StringBuffer stringBuffer = new StringBuffer("androidamap://navi?sourceApplication=")
                .append("yitu8_driver").append("&lat=").append(lat)
                .append("&lon=").append(lng)
                .append("&dev=").append(1)
                .append("&style=").append(0);
        ;
        if (!TextUtils.isEmpty(hotelName))
        {
            stringBuffer.append("&poiname=").append(hotelName);
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(stringBuffer.toString()));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setPackage("com.autonavi.minimap");
        startActivity(intent);
    }


    /**
     * 打开百度地图导航客户端
     * intent = Intent.getIntent("baidumap://map/navi?location=34.264642646862,108.95108518068&type=BLK&src=thirdapp.navi.you
     * location 坐标点 location与query二者必须有一个，当有location时，忽略query
     * query    搜索key   同上
     * type 路线规划类型  BLK:躲避拥堵(自驾);TIME:最短时间(自驾);DIS:最短路程(自驾);FEE:少走高速(自驾);默认DIS
     */
    private void openBaiduNavi()
    {
        StringBuffer stringBuffer = new StringBuffer("baidumap://map/navi?location=")
                .append(lat).append(",").append(lng).append("&type=TIME");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(stringBuffer.toString()));
        intent.setPackage("com.baidu.BaiduMap");
        startActivity(intent);
    }


    public void initOverlay()
    {
        LatLng llA = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        MarkerOptions ooA = new MarkerOptions().position(llA).icon(bdA).zIndex(9).draggable(true);
        //        if (animationBox.isChecked()) {
        //            // 掉下动画
        //            ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
        //        }
        mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
        MapStatus mMapStatus = new MapStatus.Builder()
                //要移动的点
                .target(llA)
                //放大地图到20倍
                .zoom(16)
                .build();

        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);

        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }

    @Override
    protected void onDestroy()
    {
        // 退出时销毁定位
        // 关闭定位图层
        bmapView.onDestroy();
        bmapView = null;
        bdA.recycle();
        super.onDestroy();
    }

}
