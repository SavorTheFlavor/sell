package com.me.sell.service;

import com.me.sell.dto.OrderDTO;

/**
 * 向买家推送微信模板消息
 */
public interface PushMessageService {
    void orderStatus(OrderDTO orderDTO);
}
