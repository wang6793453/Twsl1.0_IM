package com.twlrg.twsl.entity;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：王先云 on 2018/4/17 14:34
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class HotelInfo implements Serializable
{
    private String id;//4   //酒店ID
    private String title;//"某某酒店1  //酒店名称
    private String jl;//12472.47   //当前位置距离酒店距离
    private int price;//200    //无早最低价格
    private String position_label;//"某某地铁/某某商圈   //位置标签
    private String service_label;//"前台热情/会议酒店" //特色服务标签
    private String  reviews_label;
    private String lat;
    private String lng;
    private String hotel_img;
    private double star;


    private String province;// 2131   //省份
    private String city;// 2158   //城市
    private String region;// 2164   //区域
    private String address;//  //街道地址
    private String type;// 1   //商户类型
    private String phone;//0755-89899888
    private String fax;//0755-89890999
    private String brand;//  //酒店品牌
    private String rooms;// 418   //酒店住房总间数
    private String opening_time;//  //开业时间
    private String decoration_time;//  //最近装修时间
    private String summary;//  //酒店简介
    private String in_out;//  //入店说明
    private String children_policy;// 儿童政策
    private String meal_plans;//膳食安排
    private String pet_policy;//宠物政策
    private String hotel_facilities;//6   7   8   9   11   12   13   14   15   16   17   18   19  //酒店设施
    private String activity_facilities;//21   23   24  活动设施
    private String room_facilities;//25   26   27   28   29   31   42   43  //客房设施
    private String conference_facilities;//44   45   46   47   48   49   65  //会场设施
    private String dining_facilities;//66   67   68  //餐厅设施
    private String restaurant;//  //附近美食餐饮
    private String shopping;//  //附近购物商圈
    private String entertainment;//  //附近娱乐场所
    private String metro_station;//  //附近地铁站
    private String scenic_spot;//  //附近景点
    private String status;// 1
    private String sort;// 0
    private String isdelete;// 0
    private String count;// 2 //评论总数
    private String   cancel_policy;
    private String      invoice;

    private String  province_name;//广东省",
    private String  city_name;//深圳市",
    private String  region_name;//龙岗区"

    private List<HotelImgInfo>  hotelImgInfoList;

    private boolean isSelected;

    public HotelInfo(JSONObject obj)
    {

        this.province_name = obj.optString("province_name");
        this.city_name = obj.optString("city_name");
        this.region_name = obj.optString("region_name");

        this.cancel_policy = obj.optString("cancel_policy");
        this.invoice = obj.optString("invoice");

        this.id = obj.optString("id");
        this.title = obj.optString("title");
        this.jl = obj.optString("jl");
        this.price = obj.optInt("price", 0);
        this.position_label = obj.optString("position_label");
        this.service_label = obj.optString("service_label");
        this.lng = obj.optString("lng");
        this.lat = obj.optString("lat");
        this.hotel_img = obj.optString("hotel_img");
        this.star = obj.optDouble("star", 5);


        this.reviews_label = obj.optString("reviews_label");
        this.count = obj.optString("count");
        this.isdelete = obj.optString("isdelete");
        this.sort = obj.optString("sort");
        this.status = obj.optString("status");
        this.scenic_spot = obj.optString("scenic_spot");
        this.metro_station = obj.optString("metro_station");
        this.entertainment = obj.optString("entertainment");
        this.shopping = obj.optString("shopping");
        this.restaurant = obj.optString("restaurant");
        this.dining_facilities = obj.optString("dining_facilities");
        this.conference_facilities = obj.optString("conference_facilities");
        this.room_facilities = obj.optString("room_facilities");
        this.activity_facilities = obj.optString("activity_facilities");
        this.hotel_facilities = obj.optString("hotel_facilities");
        this.pet_policy = obj.optString("pet_policy");
        this.meal_plans = obj.optString("meal_plans");
        this.children_policy = obj.optString("children_policy");
        this.in_out = obj.optString("in_out");
        this.summary = obj.optString("summary");
        this.decoration_time = obj.optString("decoration_time");
        this.opening_time = obj.optString("opening_time");
        this.rooms = obj.optString("rooms");
        this.brand = obj.optString("brand");
        this.fax = obj.optString("fax");
        this.phone = obj.optString("phone");
        this.type = obj.optString("type");
        this.address = obj.optString("address");
        this.region = obj.optString("region");
        this.city = obj.optString("city");
        this.province = obj.optString("province");
    }


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getJl()
    {
        return jl;
    }

    public void setJl(String jl)
    {
        this.jl = jl;
    }


    public String getPosition_label()
    {
        return position_label;
    }

    public void setPosition_label(String position_label)
    {
        this.position_label = position_label;
    }

    public String getService_label()
    {
        return service_label;
    }

    public void setService_label(String service_label)
    {
        this.service_label = service_label;
    }

    public int getPrice()
    {
        return price;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }

    public String getLat()
    {
        return lat;
    }

    public void setLat(String lat)
    {
        this.lat = lat;
    }

    public String getLng()
    {
        return lng;
    }

    public void setLng(String lng)
    {
        this.lng = lng;
    }

    public String getHotel_img()
    {
        return hotel_img;
    }

    public void setHotel_img(String hotel_img)
    {
        this.hotel_img = hotel_img;
    }

    public double getStar()
    {
        return star;
    }

    public void setStar(double star)
    {
        this.star = star;
    }

    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getRegion()
    {
        return region;
    }

    public void setRegion(String region)
    {
        this.region = region;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getFax()
    {
        return fax;
    }

    public void setFax(String fax)
    {
        this.fax = fax;
    }

    public String getBrand()
    {
        return brand;
    }

    public void setBrand(String brand)
    {
        this.brand = brand;
    }

    public String getRooms()
    {
        return rooms;
    }

    public void setRooms(String rooms)
    {
        this.rooms = rooms;
    }

    public String getOpening_time()
    {
        return opening_time;
    }

    public void setOpening_time(String opening_time)
    {
        this.opening_time = opening_time;
    }

    public String getDecoration_time()
    {
        return decoration_time;
    }

    public void setDecoration_time(String decoration_time)
    {
        this.decoration_time = decoration_time;
    }

    public String getSummary()
    {
        return summary;
    }

    public void setSummary(String summary)
    {
        this.summary = summary;
    }

    public String getIn_out()
    {
        return in_out;
    }

    public void setIn_out(String in_out)
    {
        this.in_out = in_out;
    }

    public String getChildren_policy()
    {
        return children_policy;
    }

    public void setChildren_policy(String children_policy)
    {
        this.children_policy = children_policy;
    }

    public String getMeal_plans()
    {
        return meal_plans;
    }

    public void setMeal_plans(String meal_plans)
    {
        this.meal_plans = meal_plans;
    }

    public String getPet_policy()
    {
        return pet_policy;
    }

    public void setPet_policy(String pet_policy)
    {
        this.pet_policy = pet_policy;
    }

    public String getHotel_facilities()
    {
        return hotel_facilities;
    }

    public void setHotel_facilities(String hotel_facilities)
    {
        this.hotel_facilities = hotel_facilities;
    }

    public String getActivity_facilities()
    {
        return activity_facilities;
    }

    public void setActivity_facilities(String activity_facilities)
    {
        this.activity_facilities = activity_facilities;
    }

    public String getRoom_facilities()
    {
        return room_facilities;
    }

    public void setRoom_facilities(String room_facilities)
    {
        this.room_facilities = room_facilities;
    }

    public String getConference_facilities()
    {
        return conference_facilities;
    }

    public void setConference_facilities(String conference_facilities)
    {
        this.conference_facilities = conference_facilities;
    }

    public String getDining_facilities()
    {
        return dining_facilities;
    }

    public void setDining_facilities(String dining_facilities)
    {
        this.dining_facilities = dining_facilities;
    }

    public String getRestaurant()
    {
        return restaurant;
    }

    public void setRestaurant(String restaurant)
    {
        this.restaurant = restaurant;
    }

    public String getShopping()
    {
        return shopping;
    }

    public void setShopping(String shopping)
    {
        this.shopping = shopping;
    }

    public String getEntertainment()
    {
        return entertainment;
    }

    public void setEntertainment(String entertainment)
    {
        this.entertainment = entertainment;
    }

    public String getMetro_station()
    {
        return metro_station;
    }

    public void setMetro_station(String metro_station)
    {
        this.metro_station = metro_station;
    }

    public String getScenic_spot()
    {
        return scenic_spot;
    }

    public void setScenic_spot(String scenic_spot)
    {
        this.scenic_spot = scenic_spot;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getSort()
    {
        return sort;
    }

    public void setSort(String sort)
    {
        this.sort = sort;
    }

    public String getIsdelete()
    {
        return isdelete;
    }

    public void setIsdelete(String isdelete)
    {
        this.isdelete = isdelete;
    }

    public String getCount()
    {
        return count;
    }

    public void setCount(String count)
    {
        this.count = count;
    }

    public String getReviews_label()
    {
        return reviews_label;
    }

    public void setReviews_label(String reviews_label)
    {
        this.reviews_label = reviews_label;
    }


    public String getCancel_policy()
    {
        return cancel_policy;
    }

    public void setCancel_policy(String cancel_policy)
    {
        this.cancel_policy = cancel_policy;
    }

    public String getInvoice()
    {
        return invoice;
    }

    public void setInvoice(String invoice)
    {
        this.invoice = invoice;
    }

    public String getProvince_name()
    {
        return province_name;
    }

    public void setProvince_name(String province_name)
    {
        this.province_name = province_name;
    }

    public String getCity_name()
    {
        return city_name;
    }

    public void setCity_name(String city_name)
    {
        this.city_name = city_name;
    }

    public String getRegion_name()
    {
        return region_name;
    }

    public void setRegion_name(String region_name)
    {
        this.region_name = region_name;
    }

    public List<HotelImgInfo> getHotelImgInfoList()
    {
        return hotelImgInfoList;
    }

    public void setHotelImgInfoList(List<HotelImgInfo> hotelImgInfoList)
    {
        this.hotelImgInfoList = hotelImgInfoList;
    }

    public boolean isSelected()
    {
        return isSelected;
    }

    public void setSelected(boolean selected)
    {
        isSelected = selected;
    }
}
