package com.me.service.impl;

import com.me.controller.BuyerOrderController;
import com.me.dto.OrderDTO;
import com.me.enums.ResultEnum;
import com.me.exception.SellException;
import com.me.service.BuyerService;
import com.me.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/12/13.
 */
@Service
public class BuyerServiceImpl implements BuyerService{

    @Autowired
    private OrderService orderService;

    private Logger logger = LoggerFactory.getLogger(BuyerOrderController.class);

    @Override
    public OrderDTO findOneOrder(String openid, String orderId) {
        OrderDTO orderDTO = orderService.findOne(orderId);
        if(orderDTO == null){
            return null;
        }
        if(!orderDTO.getBuyerOpenid().equals(openid)){
            logger.error("[find one order] the order is the owner!");
            throw new SellException(ResultEnum.ORDER_OWNER_ERROR);
        }
        return orderDTO;
    }

    @Override
    public OrderDTO cancelOrder(String openid, String orderId) {
        OrderDTO orderDTO = findOneOrder(openid, orderId);
        if(orderDTO == null){
            logger.error("[cancel one order] the order is not exist!");
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        orderService.cancel(orderDTO);
        return orderDTO;
    }
}
