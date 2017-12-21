package com.me.sell.controller;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;

/**
 * Created by Administrator on 2017/12/21.
 */
@Controller
@RequestMapping("/wechat")
public class WeChatController {

    private Logger logger = LoggerFactory.getLogger(WeChatController.class);

    @Autowired
    private WxMpService wxMpService;

    //获取code
    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl){//returnUrl作为state参数传递过去，在获得授权后重定向到这个url上
       //wxMpService... first, config
        // and then use it...
        //自动拼装出了
        //https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
        String rediectUrl = wxMpService.oauth2buildAuthorizationUrl(returnUrl, WxConsts.OAuth2Scope.SNSAPI_BASE, URLEncoder.encode(returnUrl));
        return "redirect:" + rediectUrl;
    }

    //上面的returnUrl 定位到这里
    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code,
                         @RequestParam("state") String returnUrl){
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = null;
        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        String openid = wxMpOAuth2AccessToken.getAccessToken();
        System.out.println("openid:"+openid);
        return "rediect:"+returnUrl+"?openid="+openid;
    }

}
