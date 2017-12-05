package com.me.util;

import java.util.Random;

/**
 * Created by Administrator on 2017/12/5.
 */
public class KeyUtil {

    private static Random r = new Random();

    /**
     *
     * 生成唯一的key
     * 时间 + 随机数
     * @return
     */
    public static synchronized String generateUniqueKey(){
        return System.currentTimeMillis() + "" + (r.nextInt(900000) + 100000);
    }

}
