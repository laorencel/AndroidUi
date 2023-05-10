package com.laorencel.uilibrary.util.rxbus;

public class RxEvent {
    private int code;
    private Object object;

    public RxEvent() {
    }

    public RxEvent(int code) {
        this.code = code;
    }

    public RxEvent(int code, Object object) {
        this.code = code;
        this.object = object;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "RxEvent{" +
                "code=" + code +
                ", object=" + object +
                '}';
    }
}
