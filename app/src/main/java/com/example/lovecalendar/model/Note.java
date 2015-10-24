package com.example.lovecalendar.model;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.Table;

import java.sql.Time;
import java.util.Date;

/**
 * Created by 轾 on 2015/10/23.
 */
@Table("lovetable")
public class Note extends Soul {
    @NotNull @Column("title")
    private String title;
    @NotNull @Column("content")
    private String content;
    @NotNull @Column("year")
    private int year;
    @NotNull @Column("month")
    private int month;
    @NotNull @Column("day")
    private int day;
    @Column("hour")
    private int hour;
    @Column("minute")
    private int minute;
    @NotNull @Column("isOpen")
    private int isOpen;   //打开闹钟？
    @Column("isFinish")
    private int isFisish;   //完成？
    @NotNull @Column("type")
    private int type;   //笔记类型？

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }

    public int getIsFisish() {
        return isFisish;
    }

    public void setIsFisish(int isFisish) {
        this.isFisish = isFisish;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", hour=" + hour +
                ", minute=" + minute +
                ", isOpen=" + isOpen +
                ", isFisish=" + isFisish +
                ", type=" + type +
                '}';
    }
}
