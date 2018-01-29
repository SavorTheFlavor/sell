package com.me.sell.util;

import com.me.sell.enums.CodeEnum;

/**
 * Created by Administrator on 2018/1/23.
 */
public class EnumUtil {
    public static <T extends CodeEnum> T getEnumByCode(Integer code, Class<T> enumClass){
        for (T item: enumClass.getEnumConstants()) {
            if(item.getCode().equals(code)){
                return item;
            }
        }
        return null;
    }
}
