package com.gst.mybaseapp.database.been;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

import org.greenrobot.greendao.DaoException;

/**
 * https://www.jianshu.com/p/53083f782ea2
 * <p>
 * author: GuoSongtao on 2019/4/9 17:42
 * email: 157010607@qq.com
 */
@Entity(nameInDb = "loginbusername")
public class LoginBuserName {
    @Id(autoincrement = false) //注意添上@Id 不设置id属性 将无法插入
    private Long id;
    @Property(nameInDb = "bUsername")
    private String bUsername;
    private String password;

    @Generated(hash = 353141790)
    public LoginBuserName(Long id, String bUsername, String password) {
        this.id = id;
        this.bUsername = bUsername;
        this.password = password;
    }

    @Generated(hash = 422478397)
    public LoginBuserName() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBUsername() {
        return this.bUsername;
    }

    public void setBUsername(String bUsername) {
        this.bUsername = bUsername;
    }

}
