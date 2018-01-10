package com.me.sell.util;

/**
 * Created by Administrator on 2018/1/10.
 */
public class MathUtil {
    private final static double PRECISION = 0.01;

    //浮点数在存储时可能会有偏差，像0.1之类的数...
    public static boolean equals(Double amount1, Double amount2){
        double deviation = Math.abs(amount1 - amount2);
        if(deviation < PRECISION){
            return true;
        }
        return  false;
    }

}
