package com.me.sell.exception;

import com.me.sell.enums.ResultEnum;
import lombok.Getter;

/**
 * Created by Administrator on 2017/12/5.
 */
@Getter
public class SellException extends RuntimeException {
    private Integer code;

    public SellException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public SellException(ResultEnum resultEnum, String message) {
        super(message);
        this.code = resultEnum.getCode();
    }
}
