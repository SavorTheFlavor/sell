package com.me.sell.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/12/22.
 */
//spring会自动从配置文件里查找这些属性并注入
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatAccountConfig {
    private String mpAppId;
    private String mpAppSecret;

    public String getMpAppId() {
        return mpAppId;
    }

    public void setMpAppId(String mpAppId) {
        this.mpAppId = mpAppId;
    }

    public String getMpAppSecret() {
        return mpAppSecret;
    }

    public void setMpAppSecret(String mpAppSecret) {
        this.mpAppSecret = mpAppSecret;
    }
}
