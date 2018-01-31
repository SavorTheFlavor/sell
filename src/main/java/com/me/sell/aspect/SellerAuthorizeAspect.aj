package com.me.sell.aspect;

import com.me.sell.constant.CookieConstant;
import com.me.sell.constant.RedisConstant;
import com.me.sell.exception.SellerAuthorizeException;
import com.me.sell.util.CookieUtil;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2018/1/30.
 */
@Aspect
@Component
public class SellerAuthorizeAspect {

    @Autowired
    private RedisTemplate redisTemplate;

    @Pointcut("execution(public * com.me.sell.controller.Seller*.*(..))"
    +"&& !execution(public * com.me.sell.controller.SellerUserController.*(..))")
    public void verify(){}

    @Before("verify()")
    public void doVerify(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //查询cookie
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if(cookie == null){
            throw new SellerAuthorizeException();
        }

        //去redis里查
        String key = String.format(RedisConstant.TOKEN_PREFIX,cookie.getValue()); //key: token_asdlkasdmalknf...  value: openid
        String openid = redisTemplate.opsForValue().get(key).toString();
        if(StringUtils.isEmpty(openid)){
            throw new SellerAuthorizeException();
        }
    }
}
