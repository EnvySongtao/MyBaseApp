package com.gst.mybaseapp.database.been;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * 该类是读取已存在数据库的操作
 *
 * @Entity注解 ：
 * schema：如果你有多个架构，你可以告诉GreenDao当前属于哪个架构。
 * active：标记一个实体处于活跃状态，活动实体有更新、删除和刷新方法。
 * nameInDb：在数据中使用的别名，默认使用的是实体的类名。
 * indexes：标记如果DAO应该创建数据库表(默认为true)，如果您有多个实体映射到一个表，或者表的创建是在greenDAO之外进行的，那么将其设置为false。
 * createInDb：标记创建数据库表。
 * generateGettersSetters：如果缺少，是否应生成属性的getter和setter方法。
 * <p>
 * 基础属性注解（@Id，@Property，@NotNull，@Transient）
 * @Id注解选择 ：
 * long / Long属性作为实体ID。在数据库方面，它是主键。参数autoincrement = true 表示自增，id不给赋值或者为赋值为null即可（这里需要注意，如果要实现自增，id必须是Long,为long不行！)。
 * @Property ：
 * 允许您定义属性映射到的非默认列名。如果不存在，GreenDAO将以SQL-ish方式使用字段名称（大写，下划线而不是camel情况，例如 name将成为 NAME）。注意：您当前只能使用内联常量来指定列名。
 * @NotNull ：
 * 设置数据库表当前列不能为空 。
 * @Transient ：
 * 添加次标记之后不会生成数据库表的列。标记要从持久性中排除的属性。将它们用于临时状态等。或者，您也可以使用Java中的transient关键字。
 * <p>
 * 索引注解
 * @Index ：
 * 使用@Index作为一个属性来创建一个索引，通过name设置索引别名，也可以通过unique给索引添加约束。
 * @Unique ：
 * 向索引添加UNIQUE约束，强制所有值都是唯一的。
 * <p>
 * 关系注解
 * @ToOne ：
 * 定义与另一个实体（一个实体对象）的关系,（如，一个市级城市对应一个省级城市）
 * @ToMany ：
 * 定义与多个实体对象的关系,（如，一个省级城市有多个市级城市）。
 * <p>
 * 其他
 * @Generated ：
 * 当你想保留多个构造方法(constructor)时 必须加上注解@Generated
 *
 * https://www.jianshu.com/p/53083f782ea2
 * author: GuoSongtao on 2019/4/9 10:26
 * email: 157010607@qq.com
 */
@Entity(nameInDb = "YunRichCity2")
public class City {
    String PROVID;
    String PROVNAME;
    @Id
    @Property(nameInDb = "AREA")
    String AREAID;
    String AREANAME;

    @Generated(hash = 750791287)
    public City() {
    }

    @Generated(hash = 629905072)
    public City(String PROVID, String PROVNAME, String AREAID, String AREANAME) {
        this.PROVID = PROVID;
        this.PROVNAME = PROVNAME;
        this.AREAID = AREAID;
        this.AREANAME = AREANAME;
    }

    public String getPROVID() {
        return PROVID;
    }

    public void setPROVID(String PROVID) {
        this.PROVID = PROVID;
    }

    public String getPROVNAME() {
        return PROVNAME;
    }

    public void setPROVNAME(String PROVNAME) {
        this.PROVNAME = PROVNAME;
    }

    public String getAREAID() {
        return AREAID;
    }

    public void setAREAID(String AREAID) {
        this.AREAID = AREAID;
    }

    public String getAREANAME() {
        return AREANAME;
    }

    public void setAREANAME(String AREANAME) {
        this.AREANAME = AREANAME;
    }

    @Override
    public String toString() {
        return "City{" +
                "PROVID='" + PROVID + '\'' +
                ", PROVNAME='" + PROVNAME + '\'' +
                ", AREAID='" + AREAID + '\'' +
                ", AREANAME='" + AREANAME + '\'' +
                '}';
    }
}
