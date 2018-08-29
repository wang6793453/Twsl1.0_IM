package com.twlrg.twsl.entity;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 作者：王先云 on 2018/4/13 11:21
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class OrderInfo implements Serializable
{
    private String order_id;
    private String merchant;//某某酒店1
    private String title;//豪华大床房
    private String buynum;//1,
    private String total_fee;//500,
    private String name;//樊先生
    private String check_in;//2017-05-12
    private String check_out;//2017-05-13
    private String days;//1,"
    private String create_time;//2017-05-12 16:05
    private String ordcode;//120520173923164211,
    private String payment_trade_status;//0    //0为未支付，1为已支付
    private int    is_used;//0 //0待确认，1已留房，2客满  3.取消确认中 4.已取消 5.酒店拒绝取消
    private String id;//222,
    private String microweb_id;//0,
    private String merchant_id;//4,
    private String type;//1,
    private String product_id;//4,
    private String price;//0,
    private String uid;//487,
    private String sale_uid;//0,
    private String mobile;//138****1032,
    private String sex;//0,
    private String is_invoice;//0,
    private String invoice_type;//0,
    private String courier_fees;//0,
    private String shipping_address;//
    private String recipient;//
    private String recipient_mobile;//0,
    private String company_name;//
    private String company_address;//
    private String company_mobile;//
    private String bank_name;//
    private String bank_account;//
    private String taxpayer_id;//
    private String payment_type;//0,
    private String payment_trade_no;//
    private String buyer_email;//
    private String notify_time;//"
    private String occupant;
    private String invoice;
    private String remark;
    private String cancel_policy;
    private String date;
    private String status;
    private String price_type;
    //酒店销售人员信息
    private String salesperson;
    private String portrait;
    private String position;
    private String nickname;
    private String sale_mobile;
    private  boolean priceModify;
    private String  is_pay;
    private String  merchant_name;
    private String  show_mobile;
    private String  bill;
    public OrderInfo(){}
    public OrderInfo(JSONObject obj)
    {
        this.bill = obj.optString("bill");
        this.status = obj.optString("status");
        this.price_type = obj.optString("price_type");
        this.salesperson = obj.optString("salesperson");
        this.portrait = obj.optString("portrait");
        this.position = obj.optString("position");
        this.nickname = obj.optString("nickname");
        this.sale_mobile = obj.optString("sale_mobile");
        this.date = obj.optString("date");
        this.invoice = obj.optString("invoice");
        this.remark = obj.optString("remark");
        this.show_mobile = obj.optString("show_mobile");


        this.occupant = obj.optString("occupant");
        this.order_id = obj.optString("order_id");
        this.merchant = obj.optString("merchant");
        this.title = obj.optString("title");
        this.buynum = obj.optString("buynum");
        this.total_fee = obj.optString("total_fee");
        this.name = obj.optString("name");
        this.check_in = obj.optString("check_in");
        this.check_out = obj.optString("check_out");
        this.days = obj.optString("days");
        this.create_time = obj.optString("create_time");
        this.ordcode = obj.optString("ordcode");
        this.payment_trade_status = obj.optString("payment_trade_status");
        this.is_used = obj.optInt("is_used");
        this.id = obj.optString("id");
        this.microweb_id = obj.optString("microweb_id");
        this.merchant_id = obj.optString("merchant_id");
        this.type = obj.optString("type");
        this.product_id = obj.optString("product_id");
        this.price = obj.optString("price");
        this.uid = obj.optString("uid");
        this.sale_uid = obj.optString("sale_uid");
        this.mobile = obj.optString("mobile");
        this.sex = obj.optString("sex");
        this.is_invoice = obj.optString("is_invoice");
        this.invoice_type = obj.optString("invoice_type");
        this.courier_fees = obj.optString("courier_fees");
        this.shipping_address = obj.optString("shipping_address");
        this.recipient = obj.optString("recipient");
        this.recipient_mobile = obj.optString("recipient_mobile");
        this.company_name = obj.optString("company_name");
        this.company_address = obj.optString("company_address");
        this.company_mobile = obj.optString("company_mobile");
        this.bank_name = obj.optString("bank_name");
        this.bank_account = obj.optString("bank_account");
        this.taxpayer_id = obj.optString("taxpayer_id");
        this.payment_type = obj.optString("payment_type");
        this.payment_trade_no = obj.optString("payment_trade_no");
        this.buyer_email = obj.optString("buyer_email");
        this.notify_time = obj.optString("notify_time");
        this.cancel_policy = obj.optString("cancel_policy");
        this.merchant_name=obj.optString("merchant_name");
        this.is_pay=obj.optString("is_pay");
    }

    public String getIs_pay()
    {
        return is_pay;
    }

    public void setIs_pay(String is_pay)
    {
        this.is_pay = is_pay;
    }

    public String getMerchant_name()
    {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name)
    {
        this.merchant_name = merchant_name;
    }

    public String getShow_mobile()
    {
        return show_mobile;
    }

    public void setShow_mobile(String show_mobile)
    {
        this.show_mobile = show_mobile;
    }

    public String getCancel_policy()
    {
        return cancel_policy;
    }

    public void setCancel_policy(String cancel_policy)
    {
        this.cancel_policy = cancel_policy;
    }

    public String getMerchant()
    {
        return merchant;
    }

    public void setMerchant(String merchant)
    {
        this.merchant = merchant;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getBuynum()
    {
        return buynum;
    }

    public void setBuynum(String buynum)
    {
        this.buynum = buynum;
    }

    public String getTotal_fee()
    {
        return total_fee;
    }

    public void setTotal_fee(String total_fee)
    {
        this.total_fee = total_fee;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCheck_in()
    {
        return check_in;
    }

    public void setCheck_in(String check_in)
    {
        this.check_in = check_in;
    }

    public String getCheck_out()
    {
        return check_out;
    }

    public void setCheck_out(String check_out)
    {
        this.check_out = check_out;
    }

    public String getDays()
    {
        return days;
    }

    public void setDays(String days)
    {
        this.days = days;
    }

    public String getCreate_time()
    {
        return create_time;
    }

    public void setCreate_time(String create_time)
    {
        this.create_time = create_time;
    }

    public String getOrdcode()
    {
        return ordcode;
    }

    public void setOrdcode(String ordcode)
    {
        this.ordcode = ordcode;
    }

    public String getPayment_trade_status()
    {
        return payment_trade_status;
    }

    public void setPayment_trade_status(String payment_trade_status)
    {
        this.payment_trade_status = payment_trade_status;
    }

    public int getIs_used()
    {
        return is_used;
    }

    public void setIs_used(int is_used)
    {
        this.is_used = is_used;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getMicroweb_id()
    {
        return microweb_id;
    }

    public void setMicroweb_id(String microweb_id)
    {
        this.microweb_id = microweb_id;
    }

    public String getMerchant_id()
    {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id)
    {
        this.merchant_id = merchant_id;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getProduct_id()
    {
        return product_id;
    }

    public void setProduct_id(String product_id)
    {
        this.product_id = product_id;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getSale_uid()
    {
        return sale_uid;
    }

    public void setSale_uid(String sale_uid)
    {
        this.sale_uid = sale_uid;
    }

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getIs_invoice()
    {
        return is_invoice;
    }

    public void setIs_invoice(String is_invoice)
    {
        this.is_invoice = is_invoice;
    }

    public String getInvoice_type()
    {
        return invoice_type;
    }

    public void setInvoice_type(String invoice_type)
    {
        this.invoice_type = invoice_type;
    }

    public String getCourier_fees()
    {
        return courier_fees;
    }

    public void setCourier_fees(String courier_fees)
    {
        this.courier_fees = courier_fees;
    }

    public String getShipping_address()
    {
        return shipping_address;
    }

    public void setShipping_address(String shipping_address)
    {
        this.shipping_address = shipping_address;
    }

    public String getRecipient()
    {
        return recipient;
    }

    public void setRecipient(String recipient)
    {
        this.recipient = recipient;
    }

    public String getRecipient_mobile()
    {
        return recipient_mobile;
    }

    public void setRecipient_mobile(String recipient_mobile)
    {
        this.recipient_mobile = recipient_mobile;
    }

    public String getCompany_name()
    {
        return company_name;
    }

    public void setCompany_name(String company_name)
    {
        this.company_name = company_name;
    }

    public String getCompany_address()
    {
        return company_address;
    }

    public void setCompany_address(String company_address)
    {
        this.company_address = company_address;
    }

    public String getCompany_mobile()
    {
        return company_mobile;
    }

    public void setCompany_mobile(String company_mobile)
    {
        this.company_mobile = company_mobile;
    }

    public String getBank_name()
    {
        return bank_name;
    }

    public void setBank_name(String bank_name)
    {
        this.bank_name = bank_name;
    }

    public String getBank_account()
    {
        return bank_account;
    }

    public void setBank_account(String bank_account)
    {
        this.bank_account = bank_account;
    }

    public String getTaxpayer_id()
    {
        return taxpayer_id;
    }

    public void setTaxpayer_id(String taxpayer_id)
    {
        this.taxpayer_id = taxpayer_id;
    }

    public String getPayment_type()
    {
        return payment_type;
    }

    public void setPayment_type(String payment_type)
    {
        this.payment_type = payment_type;
    }

    public String getPayment_trade_no()
    {
        return payment_trade_no;
    }

    public void setPayment_trade_no(String payment_trade_no)
    {
        this.payment_trade_no = payment_trade_no;
    }

    public String getBuyer_email()
    {
        return buyer_email;
    }

    public void setBuyer_email(String buyer_email)
    {
        this.buyer_email = buyer_email;
    }

    public String getNotify_time()
    {
        return notify_time;
    }

    public void setNotify_time(String notify_time)
    {
        this.notify_time = notify_time;
    }

    public String getOrder_id()
    {
        return order_id;
    }

    public void setOrder_id(String order_id)
    {
        this.order_id = order_id;
    }

    public String getPrice_type()
    {
        return price_type;
    }

    public void setPrice_type(String price_type)
    {
        this.price_type = price_type;
    }

    public String getSalesperson()
    {
        return salesperson;
    }

    public void setSalesperson(String salesperson)
    {
        this.salesperson = salesperson;
    }

    public String getPortrait()
    {
        return portrait;
    }

    public void setPortrait(String portrait)
    {
        this.portrait = portrait;
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

    public String getSale_mobile()
    {
        return sale_mobile;
    }

    public void setSale_mobile(String sale_mobile)
    {
        this.sale_mobile = sale_mobile;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getOccupant()
    {
        return occupant;
    }

    public void setOccupant(String occupant)
    {
        this.occupant = occupant;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getInvoice()
    {
        return invoice;
    }

    public void setInvoice(String invoice)
    {
        this.invoice = invoice;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public boolean isPriceModify()
    {
        return priceModify;
    }

    public void setPriceModify(boolean priceModify)
    {
        this.priceModify = priceModify;
    }

    public String getBill()
    {
        return bill;
    }

    public void setBill(String bill)
    {
        this.bill = bill;
    }
}
