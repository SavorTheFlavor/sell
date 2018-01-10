package com.me.sell.service;

import com.me.sell.dto.OrderDTO;

/**
 * Created by Administrator on 2018/1/8.
 */
public interface PayService {
    void create(OrderDTO orderDTO);
}
