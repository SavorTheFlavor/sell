package com.me.enums;

import lombok.Getter;

/**
 * Created by Administrator on 2017/12/5.
 */
@Getter
public enum ResultEnum {

    PRODUCT_NOT_EXIST(0,"商品不存在！"),
    PRODUCT_STOCK_NOT_ENOUGH(1,"库存不足！"),
    ORDER_NOT_EXIST(2,"订单不存在！" );

    private Integer code;
    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
