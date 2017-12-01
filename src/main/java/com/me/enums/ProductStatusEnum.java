package com.me.enums;

/**
 * Created by Administrator on 2017/12/2.
 */
public enum ProductStatusEnum {
    UP(0,"在架子上"),
    DOWN(1,"下架")
    ;
    private Integer code;
    private  String message;

    ProductStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
