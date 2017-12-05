package com.me.service;

import com.me.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Administrator on 2017/12/4.
 */
public interface OrderService {
    OrderDTO create(OrderDTO orderDTO);
    OrderDTO findOne(String orderId);
    /** 根据openid分页查询买家的订单 */
    Page<OrderDTO> findList(String buyerOpenid, Pageable pageable);
    OrderDTO cancel(OrderDTO orderDTO);
    /** 完成订单交易*/
    OrderDTO finish(OrderDTO orderDTO);
    OrderDTO pay(OrderDTO orderDTO);
}