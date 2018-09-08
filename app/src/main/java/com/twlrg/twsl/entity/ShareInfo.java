package com.twlrg.twsl.entity;

import org.json.JSONObject;

/**
 * 作者：王先云 on 2018/9/1 17:53
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class ShareInfo
{
    private String title;//维也纳横岗翠湖山庄店",
    private String img;//\/Uploads\/wg\/9948\/1533035495_cropImage.jpeg",
    private String content;//超级优惠大派送！",
    private String url;//http:\/\/www.shanglvbuluo.com\/microsite\/index\/hoteldetail\/id\/60\/merchant_id\/9948\/uid\/888.html"
    private int shareStyle;

    public ShareInfo(JSONObject obj)
    {
        this.title = obj.optString("title");
        this.img=obj.optString("img");
        this.content=obj.optString("content");
        this.url=obj.optString("url");
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getImg()
    {
        return img;
    }

    public void setImg(String img)
    {
        this.img = img;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public int getShareStyle()
    {
        return shareStyle;
    }

    public void setShareStyle(int shareStyle)
    {
        this.shareStyle = shareStyle;
    }
}
