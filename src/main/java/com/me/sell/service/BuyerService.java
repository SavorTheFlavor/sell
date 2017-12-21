package com.me.sell.service;

import com.me.sell.dto.OrderDTO;

/**
 * Created by Administrator on 2017/12/13.
 */
public interface BuyerService {
    OrderDTO findOneOrder(String openid, String orderId);
    OrderDTO cancelOrder(String openid, String orderId);
}
