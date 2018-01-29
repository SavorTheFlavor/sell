package com.me.sell.service;

import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundResponse;
import com.me.sell.dto.OrderDTO;

/**
 * Created by Administrator on 2018/1/8.
 */
public interface PayService {
    void create(OrderDTO orderDTO);
    PayResponse notify(String notifyData);
    RefundResponse refund(OrderDTO orderDTO);
}
