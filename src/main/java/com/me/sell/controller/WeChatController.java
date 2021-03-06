package com.me.sell.controller;

import com.me.sell.config.ProjectUrlConfig;
import com.me.sell.enums.ResultEnum;
import com.me.sell.exception.SellException;
import com.me.sell.util.CheckUtil;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2017/12/21.
 */
@Controller
@RequestMapping("/wechat")
public class WeChatController {

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    @Autowired
    private WxMpService wxOpenService;

    private Logger logger = LoggerFactory.getLogger(WeChatController.class);

//    @Autowired
    private WxMpService wxMpService;

    //获取code
    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl){//returnUrl作为state参数传递过去，在获得授权后重定向到这个url上
       //wxMpService... first, config
        // and then use it...
        //自动拼装出了
        //https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
        String callBackUrl = "http://mytunnel4dev.free.ngrok.cc/sell/wechat/userInfo";
        String rediectUrl = wxMpService.oauth2buildAuthorizationUrl(callBackUrl, WxConsts.OAUTH2_SCOPE_USER_INFO, URLEncoder.encode(returnUrl));
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
            throw new SellException(ResultEnum.WX_MP_ERROR);
        }
        String openid = wxMpOAuth2AccessToken.getAccessToken();
        System.out.println("openid:"+openid);
        return "redirect:"+returnUrl+"?openid="+openid;
    }

    @GetMapping("/signature")
    public void checkSignature(String signature, String timestamp, String nonce, String echostr, HttpServletResponse resp){
        PrintWriter out = null;
        try {
            out = resp.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.print(echostr);
        out.close();
//        if(CheckUtil.checkSignature(signature, timestamp, nonce)){
//            out.print(echostr);
//        }
    }

    //微信扫描登录
    @GetMapping("/qrAuthorize")
    public String qrAuthorize(@RequestParam("returnUrl") String returnUrl){
        String url = projectUrlConfig.getWechatOpenAuthorize() + "/sell/wechat/qrUserInfo";
        String redirectUrl = wxOpenService.buildQrConnectUrl(url, WxConsts.QRCONNECT_SCOPE_SNSAPI_LOGIN, URLEncoder.encode(returnUrl));
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/qrUserInfo")
    public String qrUserInfo(@RequestParam("code") String code,
                             @RequestParam("state") String returnUrl) {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        try {
            wxMpOAuth2AccessToken = wxOpenService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            throw new SellException(ResultEnum.WECHAT_MP_ERROR, e.getError().getErrorMsg());
        }
        String openId = wxMpOAuth2AccessToken.getOpenId();

        return "redirect:" + returnUrl + "?openid=" + openId;
    }

}
