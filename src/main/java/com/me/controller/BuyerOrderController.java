package com.me.controller;

import com.me.converter.OrderForm2OrderDTOConverter;
import com.me.dto.OrderDTO;
import com.me.enums.ResultEnum;
import com.me.exception.SellException;
import com.me.form.OrderForm;
import com.me.service.OrderService;
import com.me.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/11.
 */
@RestController
@RequestMapping("/buyer/order")
public class BuyerOrderController {

    private Logger logger = LoggerFactory.getLogger(BuyerOrderController.class);

    @Autowired
    private OrderService orderService;

    /**
     * create order
     */
    @PostMapping("/create")
    public ResultVO<Map<String,String>> create(@Valid OrderForm orderForm,
                                               BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            logger.error("[create order] parameters error, orderForm={}",orderForm);
            throw new SellException(ResultEnum.PARAMETER_ERROR,bindingResult.getFieldError().getDefaultMessage());
        }

        OrderDTO orderDTO = OrderForm2OrderDTOConverter.convert(orderForm);
        if(CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
            logger.error("[creat order] cart is empty!");
            throw new SellException(ResultEnum.CART_EMPTY);
        }

        OrderDTO creatResult = orderService.create(orderDTO);
        Map<String,String> map = new HashMap<>();
        map.put("orderId",creatResult.getOrderId());
        return  ResultVO.success(map);
    }
}
