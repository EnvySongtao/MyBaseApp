package com.gst.mybaseapp.been;

/**
 * author: GuoSongtao on 2019/4/12 17:24
 * email: 157010607@qq.com
 */
public class XmlApp {
    private String id;
    private String name;
    private String version;
    private String type;

    public XmlApp() {
    }

    public XmlApp(String id, String name, String version, String type) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "XmlApp{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
