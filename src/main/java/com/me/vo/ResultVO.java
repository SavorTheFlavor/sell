package com.me.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * http请求返回的最外层对象(Json格式)
 * VO, view object
 */
//@Data 但是ide会报错啊
public class ResultVO<T> {
    private Integer code;
    private String msg;
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
