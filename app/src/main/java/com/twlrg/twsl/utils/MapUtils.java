package com.twlrg.twsl.utils;

import com.twlrg.twsl.entity.LngLat;

import java.math.BigDecimal;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 * 作者：王先云 on 2018/7/12 21:32
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class MapUtils
{
    private static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    /**
     * 坐标转换，百度地图坐标转换成腾讯地图坐标
     *
     * @param lat 百度坐标纬度
     * @param lon 百度坐标经度
     * @return 返回结果：纬度,经度
     */
    public static double[] map_bd2tx(double lat, double lon)
    {
        double tx_lat;
        double tx_lon;
        double x_pi = 3.14159265358979324;
        double x = lon - 0.0065, y = lat - 0.006;
        double z = sqrt(x * x + y * y) - 0.00002 * sin(y * x_pi);
        double theta = atan2(y, x) - 0.000003 * cos(x * x_pi);
        tx_lon = z * cos(theta);
        tx_lat = z * sin(theta);
        return new double[]{tx_lat, tx_lon};

    }

    /**
     * 对double类型数据保留小数点后多少位
     * 高德地图转码返回的就是 小数点后6位，为了统一封装一下
     *
     * @param digit 位数
     * @param in    输入
     * @return 保留小数位后的数
     */
    public static double dataDigit(int digit, double in)
    {
        return new BigDecimal(in).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();

    }

    /**
     * 将火星坐标转变成百度坐标
     *
     * @param lngLat_gd 火星坐标（高德、腾讯地图坐标等）
     * @return 百度坐标
     */

    public static LngLat bd_encrypt(LngLat lngLat_gd)
    {
        double x = lngLat_gd.getLongitude(), y = lngLat_gd.getLantitude();
        double z = sqrt(x * x + y * y) + 0.00002 * sin(y * x_pi);
        double theta = atan2(y, x) + 0.000003 * cos(x * x_pi);
        return new LngLat(dataDigit(6, z * cos(theta) + 0.0065), dataDigit(6, z * sin(theta) + 0.006));

    }

    /**
     * 将百度坐标转变成火星坐标
     *
     * @param lngLat_bd 百度坐标（百度地图坐标）
     * @return 火星坐标(高德 、 腾讯地图等)
     */
    public static LngLat bd_decrypt(LngLat lngLat_bd)
    {
        double x = lngLat_bd.getLongitude() - 0.0065, y = lngLat_bd.getLantitude() - 0.006;
        double z = sqrt(x * x + y * y) - 0.00002 * sin(y * x_pi);
        double theta = atan2(y, x) - 0.000003 * cos(x * x_pi);
        return new LngLat(dataDigit(6, z * cos(theta)), dataDigit(6, z * sin(theta)));


    }
}