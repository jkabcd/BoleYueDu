package com.bole.basemodel.bean;

import java.util.List;

public class EventMsg<T> {
    private T event;

    private List<?> list;

    private Integer tag;

    public EventMsg(T event) {
        this.event = event;
    }

    public EventMsg(Integer tag, T event) {
        this.tag = tag;
        this.event = event;
    }

    public EventMsg(List<?> list) {
        this.list = list;
    }

    public EventMsg(Integer tag, List<?> list) {
        this.tag = tag;
        this.list = list;
    }

    public Integer getTag() {
        return tag;
    }

    public T getData() {
        return event;
    }

    public List<?> getList() {
        return list;
    }
}
