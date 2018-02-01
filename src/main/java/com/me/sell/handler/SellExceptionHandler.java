package com.me.sell.handler;

import com.me.sell.config.ProjectUrlConfig;
import com.me.sell.exception.SellException;
import com.me.sell.exception.SellerAuthorizeException;
import com.me.sell.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @ExceptionHandler(value = SellException.class)
    @ResponseBody  //返回ResultVO对象，但http状态是200
    // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) 直接给前端返回500
    public ResultVO handleSellException(SellException e){
        return ResultVO.error(e.getCode(),e.getMessage());
    }

}
