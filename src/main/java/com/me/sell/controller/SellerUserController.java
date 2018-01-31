package com.me.sell.controller;


import com.me.sell.bean.SellerInfo;
import com.me.sell.config.ProjectUrlConfig;
import com.me.sell.constant.CookieConstant;
import com.me.sell.constant.RedisConstant;
import com.me.sell.enums.ResultEnum;
import com.me.sell.service.SellerService;

import com.me.sell.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 卖家用户
 * Created by 廖师兄
 * 2017-07-30 15:28
 */
@Controller
@RequestMapping("/seller")
public class SellerUserController {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;


    @GetMapping("/login")
    public ModelAndView login(@RequestParam("openid") String openid,
                              HttpServletResponse response,
                              Map<String, Object> map) {

        //1. openid去和数据库里的数据匹配
        SellerInfo sellerInfo = sellerService.findSellerInfoByOpenid(openid);
        if(sellerInfo == null){
            map.put("msg", ResultEnum.LOGIN_FAIL.getMessage());
            map.put("url","/sell/seller/order/list");
            return new ModelAndView("common/error",map);
        }

        String token = UUID.randomUUID().toString();
        String key = String.format(RedisConstant.TOKEN_PREFIX, token);
        Integer expire = RedisConstant.EXPIRE;
        //把token缓存到redis中
        redisTemplate.opsForValue().set(key,openid,expire,TimeUnit.SECONDS);

        //把token设置到cookie里返回给客户端
        CookieUtil.set(response,CookieConstant.TOKEN,openid,expire);

        return new ModelAndView("redirect:"+projectUrlConfig.getSell()+"/sell/seller/order/list",map);
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Map<String, Object> map) {
        //1. 从cookie里查询
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if (cookie != null) {
            //2. 清除redis
            String key = String.format(RedisConstant.TOKEN_PREFIX, cookie.getValue());
            redisTemplate.opsForValue().getOperations().delete(key);

            //3. 清除cookie
            CookieUtil.set(response,CookieConstant.TOKEN,null,0);
        }

        map.put("msg", ResultEnum.LOGOUT_SUCCESS.getMessage());
        map.put("url", "/sell/seller/order/list");
        return new ModelAndView("common/success", map);
    }
}
