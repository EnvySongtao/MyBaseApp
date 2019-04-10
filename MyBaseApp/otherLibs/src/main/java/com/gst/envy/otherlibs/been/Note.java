package com.gst.envy.otherlibs.been;

import java.util.Date;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * author: GuoSongtao on 2019/4/9 10:26
 * email: 157010607@qq.com
 */
@Entity
public class Note {
    @Id(assignable = true)
    long id;

    String title;
    String text;
    Date date;

    public Note() {
    }

    public Note(long id, String title, String text, Date date) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
