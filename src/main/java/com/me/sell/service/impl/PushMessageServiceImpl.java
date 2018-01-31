package com.me.sell.service.impl;

import com.me.sell.config.WechatAccountConfig;
import com.me.sell.dto.OrderDTO;
import com.me.sell.service.PushMessageService;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2018/1/31.
 */
@Service
@Transactional
public class PushMessageServiceImpl implements PushMessageService {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WechatAccountConfig wechatAccountConfig;

    @Override
    public void orderStatus(OrderDTO orderDTO) {

        WxMpTemplateMessage wxMpTemplateMessage = new WxMpTemplateMessage();

        wxMpTemplateMessage.setTemplateId(wechatAccountConfig.getTemplateId().get("orderStatus"));
        wxMpTemplateMessage.setToUser(orderDTO.getBuyerOpenid());

        /* {{first.DATA}}
         *  单号: {{kw2.DATA}}
         *  课金:{{kw3.DATA}}
         *  ......
         */
        List<WxMpTemplateData> data = Arrays.asList(
          new WxMpTemplateData("first","hey!"),
          new WxMpTemplateData("kw1","微信点餐"),
          new WxMpTemplateData("kw2",orderDTO.getOrderId()),
          new WxMpTemplateData("kw3","$"+orderDTO.getOrderAmount()),
          new WxMpTemplateData("kw4","hey!")
        );

        wxMpTemplateMessage.setData(data);
        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(wxMpTemplateMessage);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

    }
}
