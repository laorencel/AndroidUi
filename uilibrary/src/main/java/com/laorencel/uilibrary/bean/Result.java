package com.laorencel.uilibrary.bean;

import java.io.Serializable;

/**
 * 请求返回数据（通用），实现序列化Serializable接口
 * @param <T>
 */
public class Result<T> implements Serializable {
    public static final int SUCCESS = 1;
    public static final int FAILURE = 2;
    private int code;
    private String message;
    private T data;

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = Result.SUCCESS;
        result.data = data;
        return result;
    }

    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.code = Result.SUCCESS;
        result.message = message;
        result.data = data;
        return result;
    }

    public static <T> Result<T> failure(String message) {
        Result<T> result = new Result<>();
        result.code = Result.FAILURE;
        result.message = message;
        return result;
    }

    public static <T> Result<T> failure(String message, T data) {
        Result<T> result = new Result<>();
        result.code = Result.FAILURE;
        result.message = message;
        result.data = data;
        return result;
    }

    public Result() {

    }

    public Result(int code) {
        this.code = code;
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
