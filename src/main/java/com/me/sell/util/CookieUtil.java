package com.me.sell.util;

import org.apache.http.HttpRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/30.
 */
public class CookieUtil {
    public static void set(HttpServletResponse response,
                      String key, String value, Integer expiry){
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(expiry);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static Cookie get(HttpServletRequest request, String key){
        Map<String, Cookie> map = readCookieMap(request);
        if(map.containsKey(key)){
            return map.get(key);
        }else{
            return null;
        }
    }

    private static Map<String,Cookie> readCookieMap(HttpServletRequest request) {
        Map<String, Cookie> map = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for(Cookie cookie: cookies){
                map.put(cookie.getName(),cookie);
            }
        }
        return map;
    }
}
