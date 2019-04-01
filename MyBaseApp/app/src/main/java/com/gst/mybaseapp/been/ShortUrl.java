package com.gst.mybaseapp.been;

/**
 * {"urls":[
 *     {"result":true,"url_short":"http://t.cn/EfE8AmE","url_long":"https://user-gold-cdn.xitu.io/2017/11/13/15fb45a27df11fea?w=320","object_type":"","type":0,"object_id":""}
 *     ]}
 */
public class ShortUrl {
    private String url_short;
    private String url_long;
    private String object_type;
    private String object_id;

    public ShortUrl() {
    }

    public ShortUrl(String url_short, String url_long, String object_type, String object_id) {
        this.url_short = url_short;
        this.url_long = url_long;
        this.object_type = object_type;
        this.object_id = object_id;
    }

    public String getUrl_short() {
        return url_short;
    }

    public void setUrl_short(String url_short) {
        this.url_short = url_short;
    }

    public String getUrl_long() {
        return url_long;
    }

    public void setUrl_long(String url_long) {
        this.url_long = url_long;
    }

    public String getObject_type() {
        return object_type;
    }

    public void setObject_type(String object_type) {
        this.object_type = object_type;
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }
}
