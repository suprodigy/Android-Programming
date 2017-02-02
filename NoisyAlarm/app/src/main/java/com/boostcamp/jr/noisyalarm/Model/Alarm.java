package com.boostcamp.jr.noisyalarm.Model;

/**
 * Created by jr on 2017-01-24.
 */

public class Alarm {
    private int id;
    private int hour;
    private int min;
    private int dayOfWeek;
    private boolean isSet;
    private String content;

    public int getId() {
        return id;
    }

    public Alarm setId(int id) {
        this.id = id;
        return this;
    }

    public int getHour() {
        return hour;
    }

    public Alarm setHour(int hour) {
        this.hour = hour;
        return this;
    }

    public int getMin() {
        return min;
    }

    public Alarm setMin(int min) {
        this.min = min;
        return this;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public Alarm setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        return this;
    }

    public boolean isSet() {
        return isSet;
    }

    public Alarm setSet(boolean set) {
        isSet = set;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Alarm setContent(String content) {
        this.content = content;
        return this;
    }

}
