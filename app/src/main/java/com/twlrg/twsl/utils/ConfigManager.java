package com.twlrg.twsl.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.twlrg.twsl.im.TencentCloud;

/**
 * @author
 */
public class ConfigManager
{
    /**
     * instance
     */
    private static ConfigManager     sConfigManager     = null;
    /**
     * mCtx
     */
    private        Context           mCtx               = null;
    /**
     * SharedPreferences
     */
    private        SharedPreferences mSharedPreferences = null;
    /**
     * config
     */
    private static String            CONFIG_NAME        = "config";

    private static final String IS_FRIST_LOGING = "IS_FRIST_LOGING";
    private static final String USER_PWD        = "user_pwd";//密码
    private static final String USER_NAME       = "user_name";// 用户名
    private static final String USER_ID         = "user_id";
    private static final String IDENTIFIER      = "identifier";
    private static final String PREF_UUID       = "miei";
    private static final String UNIQUE_CODE     = "unique_code";
    private static final String USER_MOBILE     = "mobile";
    private static final String IS_SAVE_PWD     = "is_save_pwd";
    private static final String GUEST_ID        = "guest_id";
    private static final String USER_NICK_NAME  = "user_nick_name";
    private static final String USER_EMAIL      = "user_email";
    private static final String CHEK_IN_DATE    = "chek_in_date";
    private static final String CHEK_OUT_DATE   = "chek_out_date";
    private static final String TOKEN           = "TOKEN";
    private static final String USER_PIC        = "useer_pic";
    private static final String USER_SEX        = "user_sex";

    private static final String CITY_VALUE = "city_value";
    private static final String MERCHANT_ID = "merchant_id";


    /**
     * 返回实例
     *
     * @return
     */
    public static ConfigManager instance()
    {
        if (sConfigManager == null)
        {
            sConfigManager = new ConfigManager();
        }

        return sConfigManager;
    }

    /**
     * init shared Preferences
     *
     * @param context
     */
    public void init(Context context)
    {
        mCtx = context;
        mSharedPreferences = context.getSharedPreferences(CONFIG_NAME, 0);

    }


    public String getUserPwd()
    {
        return mSharedPreferences.getString(USER_PWD, "");
    }

    public void setUserPwd(String pwd)
    {
        mSharedPreferences.edit().putString(USER_PWD, pwd).commit();
        return;
    }


    // 用户名
    public String getUserName()
    {
        return mSharedPreferences.getString(USER_NAME, "");
    }

    public void setUserName(String userName)
    {
        mSharedPreferences.edit().putString(USER_NAME, userName).commit();
    }


    public String getUserID()
    {
        return mSharedPreferences.getString(USER_ID, "");
    }

    public void setUserId(String userId)
    {
        mSharedPreferences.edit().putString(IDENTIFIER, TencentCloud.UID_PREFIX + userId).putString(USER_ID, userId).apply();
    }

    public String getIdentifier()
    {
        return mSharedPreferences.getString(IDENTIFIER, "");
    }

    public void setUUID(String uuid)
    {
        mSharedPreferences.edit().putString(PREF_UUID, uuid).commit();
    }

    public String getUUID()
    {
        return mSharedPreferences.getString(PREF_UUID, null);
    }


    public String getUniqueCode()
    {
        return mSharedPreferences.getString(UNIQUE_CODE, "");
    }

    public void setUniqueCode(String uniqueCode)
    {
        mSharedPreferences.edit().putString(UNIQUE_CODE, uniqueCode).commit();
    }

    public String getMobile()
    {
        return mSharedPreferences.getString(USER_MOBILE, "");
    }

    public void setMobile(String mobile)
    {
        mSharedPreferences.edit().putString(USER_MOBILE, mobile).commit();
    }


    public boolean getIsSavePwd()
    {
        return mSharedPreferences.getBoolean(IS_SAVE_PWD, false);
    }

    public void setIsSavePwd(boolean status)
    {
        mSharedPreferences.edit().putBoolean(IS_SAVE_PWD, status).commit();
    }


    public void setGUESTID(String id)
    {

        mSharedPreferences.edit().putString(GUEST_ID, id).commit();
    }

    public String getGUESTID()
    {
        //return StringUtils.getRandomString(5);
        return mSharedPreferences.getString(GUEST_ID, "");
    }


    public void setUserPic(String isHolder)
    {

        mSharedPreferences.edit().putString(USER_PIC, isHolder).commit();
    }

    public String getUserPic()
    {
        return mSharedPreferences.getString(USER_PIC, "");
    }


    public void setUserNickName(String nickName)
    {
        mSharedPreferences.edit().putString(USER_NICK_NAME, nickName).commit();
    }

    public String getUserNickName()
    {
        return mSharedPreferences.getString(USER_NICK_NAME, "");
    }

    public void setToken(String registerId)
    {
        mSharedPreferences.edit().putString(TOKEN, registerId).commit();
    }

    public String getToken()
    {
        return mSharedPreferences.getString(TOKEN, "");
    }

    public boolean getIsFristLogin()
    {
        return mSharedPreferences.getBoolean(IS_FRIST_LOGING, true);
    }

    public void setIsFristLogin(boolean status)
    {
        mSharedPreferences.edit().putBoolean(IS_FRIST_LOGING, status).commit();
    }

    public int getUserSex()
    {
        return mSharedPreferences.getInt(USER_SEX, 0);
    }

    public void setUserSex(int sex)
    {
        mSharedPreferences.edit().putInt(USER_SEX, sex).commit();
    }


    public void setUserEmail(String email)
    {
        mSharedPreferences.edit().putString(USER_EMAIL, email).commit();
    }

    public String getUserEmail()
    {
        return mSharedPreferences.getString(USER_EMAIL, "");
    }


    public void setChekInDate(String chek_in_date)
    {
        mSharedPreferences.edit().putString(CHEK_IN_DATE, chek_in_date).commit();
    }

    public String getChekInDate()
    {
        return mSharedPreferences.getString(CHEK_IN_DATE, "");
    }


    public void setChekOutDate(String chek_out_date)
    {
        mSharedPreferences.edit().putString(CHEK_OUT_DATE, chek_out_date).commit();
    }

    public String getChekOutDate()
    {
        return mSharedPreferences.getString(CHEK_OUT_DATE, "");
    }


    public void setCityValue(String city_value)
    {
        mSharedPreferences.edit().putString(CITY_VALUE, city_value).commit();
    }

    public String getCityValue()
    {
        return mSharedPreferences.getString(CITY_VALUE, "2158");
    }

    public void setMerchantId(String merchantId)
    {
        mSharedPreferences.edit().putString(MERCHANT_ID, merchantId).commit();
    }

    public String getMerchantId()
    {
        return mSharedPreferences.getString(MERCHANT_ID, "");
    }
}

