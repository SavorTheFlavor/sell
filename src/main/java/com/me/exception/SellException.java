package com.me.exception;

import com.me.enums.ResultEnum;

/**
 * Created by Administrator on 2017/12/5.
 */
public class SellException extends RuntimeException {
    private Integer code;

    public SellException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }
}
