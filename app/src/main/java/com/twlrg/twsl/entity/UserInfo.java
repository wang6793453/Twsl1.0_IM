package com.twlrg.twsl.entity;

import org.json.JSONObject;

/**
 * 作者：王先云 on 2018/5/3 21:45
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class UserInfo
{
    private String id;//495
    private String merchant_id;//private String 9948
    private String token;//private String 5ebee199ae09ae8819623b9914f32c8f
    private String name;//private String 樊锋钢
    private String role_type;//private String 1
    private String wallet;//0
    private String deposit;//private String 0
    private String bank_account;//0
    private String portrait;//private String \/Uploads\/portrait\/1521444161_20180319152240.jpeg
    private String business_card;//\/Uploads\/card\/1521429396_20180319111636.jpeg
    private String position;//private String 啦啦啦
    private String nickname;//private String 樊锋钢
    private String pwd;//941fc5ab3349b09f5969398cddb2085f
    private String vip;//1
    private String shares;//0
    private String reg_date;//1521429258
    private String verify_status;//0
    private String mobile;//13421303923
    private String status;//1
    private String hotel;//维也纳横岗翠湖山庄店


    public UserInfo(JSONObject object)
    {
        this.id = object.optString("id");
        this.merchant_id = object.optString("merchant_id");
        this.token = object.optString("token");
        this.name = object.optString("name");
        this.role_type = object.optString("role_type");
        this.wallet = object.optString("wallet");
        this.deposit = object.optString("deposit");
        this.portrait = object.optString("portrait");
        this.nickname = object.optString("nickname");
        this.position = object.optString("position");
        this.verify_status = object.optString("verify_status");
        this.mobile = object.optString("mobile");
        this.hotel = object.optString("hotel");
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getMerchant_id()
    {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id)
    {
        this.merchant_id = merchant_id;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getRole_type()
    {
        return role_type;
    }

    public void setRole_type(String role_type)
    {
        this.role_type = role_type;
    }

    public String getWallet()
    {
        return wallet;
    }

    public void setWallet(String wallet)
    {
        this.wallet = wallet;
    }

    public String getDeposit()
    {
        return deposit;
    }

    public void setDeposit(String deposit)
    {
        this.deposit = deposit;
    }

    public String getBank_account()
    {
        return bank_account;
    }

    public void setBank_account(String bank_account)
    {
        this.bank_account = bank_account;
    }

    public String getPortrait()
    {
        return portrait;
    }

    public void setPortrait(String portrait)
    {
        this.portrait = portrait;
    }

    public String getBusiness_card()
    {
        return business_card;
    }

    public void setBusiness_card(String business_card)
    {
        this.business_card = business_card;
    }

    public String getPosition()
    {
        return position;
    }

    public void setPosition(String position)
    {
        this.position = position;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public String getPwd()
    {
        return pwd;
    }

    public void setPwd(String pwd)
    {
        this.pwd = pwd;
    }

    public String getVip()
    {
        return vip;
    }

    public void setVip(String vip)
    {
        this.vip = vip;
    }

    public String getShares()
    {
        return shares;
    }

    public void setShares(String shares)
    {
        this.shares = shares;
    }

    public String getReg_date()
    {
        return reg_date;
    }

    public void setReg_date(String reg_date)
    {
        this.reg_date = reg_date;
    }

    public String getVerify_status()
    {
        return verify_status;
    }

    public void setVerify_status(String verify_status)
    {
        this.verify_status = verify_status;
    }

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getHotel()
    {
        return hotel;
    }

    public void setHotel(String hotel)
    {
        this.hotel = hotel;
    }
}
