package com.me.sell.enums;

import lombok.Getter;

/**
 * Created by Administrator on 2017/12/4.
 */
@Getter
public enum OrderStatusEnum {
    NEW(0,"新订单"),
    FINISHED(1,"完成交易"),
    CANCEL(2,"取消交易")
    ;
    private Integer code;
    private String msg;
    OrderStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
