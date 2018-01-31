package com.me.sell.handler;

import com.me.sell.config.ProjectUrlConfig;
import com.me.sell.exception.SellerAuthorizeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Administrator on 2018/1/30.
 */
@ControllerAdvice
public class SellExceptionHandler {

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    /**
     * 当发生登录异常时，让其跳转到登录界面
     * @return
     */
    @ExceptionHandler(value = SellerAuthorizeException.class)
    public ModelAndView handleAuthorizeException(){
        return new ModelAndView("redirect:"
        .concat(projectUrlConfig.getSell())
        .concat("/sell/wechat/qrAuthorize")
        .concat("?returnUrl=")
        .concat(projectUrlConfig.getSell())
        .concat("/sell/seller/login"));

    }

}
