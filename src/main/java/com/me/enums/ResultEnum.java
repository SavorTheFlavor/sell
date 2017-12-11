package com.me.enums;

import lombok.Getter;

/**
 * Created by Administrator on 2017/12/5.
 */
@Getter
public enum ResultEnum {

    PARAMETER_ERROR(-1,"parameters error!"),
    PRODUCT_NOT_EXIST(0,"商品不存在！"),
    PRODUCT_STOCK_NOT_ENOUGH(1,"库存不足！"),
    ORDER_NOT_EXIST(2,"订单不存在！" ),
    ORDER_STATUS_ERROR(3,"order status error!"),
    ORDER_UPDATE_FAILED(4,"update order status failed!"),
    ORDER_DETAIL_EMPTY(5,"there is no product in the order!"),
    ORDER_PAY_STATUS_ERROR(6,"It is not a new order that waits to be paid!"),
    CART_EMPTY(7,"get nothing to buy!"),
    ;

    private Integer code;
    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
