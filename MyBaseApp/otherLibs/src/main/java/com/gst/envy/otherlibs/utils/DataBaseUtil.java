package com.gst.envy.otherlibs.utils;

import android.app.Application;
import android.util.Log;


import com.gst.envy.otherlibs.been.MyObjectBox;
import com.gst.envy.otherlibs.been.Note;
import com.gst.envy.otherlibs.been.Note_;

import java.util.Date;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.BoxStoreBuilder;

/**
 * 数据库操作 会用到IOC和类的类型，操作十分复杂，所以无法像图片一样 封装数据库工具类 建议用接口实现需要存储数据的存储
 * author: GuoSongtao on 2019/4/8 16:16
 * email: 157010607@qq.com
 */
public class DataBaseUtil {
    private static BoxStore mBoxStore;

    public static void initDatabase(Application application) {
        //createDebugWithoutModel 操作数据类时 异常：XXX is not a known entity
//        mBoxStore = BoxStoreBuilder.createDebugWithoutModel().androidContext(application).build();
        mBoxStore = MyObjectBox.builder().androidContext(application).build();
    }

    public static BoxStore getBoxStore() {
        return mBoxStore;
    }


    public static void modifyUsers(List<Note> allNotes) {
        Box<Note> box = mBoxStore.boxFor(Note.class);
        //但这种做法可能会需要较多的时间、花费更多的性能，正确做法
        for (Note user : allNotes) {
//            modify(user); // modifies properties of given user
            user.setText("我是更新后的内容");
        }
        box.put(allNotes);
    }

    /**
     * 关于 objectBox 操作
     * @Entity：
     * 这个对象需要持久化。
     * @Id：
     * 这个对象的主键,默认情况下，id是会被objectbox管理的，也就是自增id，如果你想手动管理id需要在注解的时候加上@Id(assignable = true)即可。当你在自己管理id的时候如果超过long的最大值，objectbox 会报错.id的值不能为负数。当id等于0时objectbox会认为这是一个新的实体对象,因此会新增到数据库表中
     * @Index：
     * 这个对象中的索引。对经常大量进行查询的字段创建索引，会提高你的查询性能。
     * @Transient:
     * 如果你有某个字段不想被持久化，可以使用此注解,那么该字段将不会保存到数据库
     * @NameInDb：
     * 有的时候数据库中的字段跟你的对象字段不匹配的时候，可以使用此注解。
     * @ToOne:
     * 做一对一的关联注解，例如示例中表示一张学生表（Student）关联一张班级表（Class）,此外还有一对多，多对多的关联，例如Class的示例：
     *
     * @Entity
     * public class Class{
     *     @Id
     *     long id;
     *
     *     @Backlink(to = "classToOne")
     *     public ToMany<Student> studentEntitys;
     * }
     *
     * @ToMany:
     * 做一对多的关联注解，如示例中表示一张班级表(Class)关联多张学生表(Student)
     * @Backlink:
     * 表示反向关联
     */
    public static void testNoteEntity() {

        Box<Note> box = mBoxStore.boxFor(Note.class);
        Log.i("testUserEntity", "0,box.count()="+box.count());

        Note been1 = new Note(1,"title1","text1",new Date());
        Note been2 = new Note(2,"title2","text2",new Date());
        Note been3 = new Note(3,"title3","text3",new Date());
        //新增和修改，put 的参数可以是 list
        box.put(been1);
        box.put(been2);
        box.put(been3);
        Log.i("testNoteEntity", "1,box.count()="+box.count());
        //查询，名字为 T 开头或者 age为 10 的数据
        List<Note> item = box.query()
                .startsWith(Note_.title, "title")
                .or().equal(Note_.text, "text1").build().find();
        Log.i("testNoteEntity", "2,item.size()="+item.size());
        Log.i("testNoteEntity", "2,box.count()="+box.count());

        //删除 id 为 2 的数据
        box.remove(2);
        Log.i("testNoteEntity", "3,box.count()="+box.count());
        //查询时，用到了生成类 UserEntity_通常是实体类加一个下划线。使用 builder.equal() 进行设置匹配，调用 startWith() 设置查询条件，find() 可以用于分页。
    }
}
