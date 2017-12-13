package com.me.controller;

import com.me.converter.OrderForm2OrderDTOConverter;
import com.me.dto.OrderDTO;
import com.me.enums.ResultEnum;
import com.me.exception.SellException;
import com.me.form.OrderForm;
import com.me.service.BuyerService;
import com.me.service.OrderService;
import com.me.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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

    @Autowired
    private BuyerService buyerService;

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

    @GetMapping("/list")
    public ResultVO<List<OrderDTO>> list(@RequestParam("openid") String openid,
                                         @RequestParam(value = "page",defaultValue = "0") Integer page,
                                         @RequestParam(value = "size",defaultValue = "5") Integer size){
        if(StringUtils.isEmpty(openid)){
            logger.error("[order list] openid is null!");
            throw new SellException(ResultEnum.PARAMETER_ERROR);
        }

        PageRequest pageRequest = new PageRequest(page,size);
        Page<OrderDTO> orderDTOPage = orderService.findList(openid,pageRequest);
        return ResultVO.success(orderDTOPage.getContent());
    }

    @RequestMapping("/detail")
    public ResultVO<OrderDTO> detail(@RequestParam("openid") String openid,
                                     @RequestParam("orderId") String orderId){
        OrderDTO orderDTO = buyerService.findOneOrder(openid,orderId);
        return ResultVO.success(orderDTO);
    }

    @RequestMapping("/cancel")
    public ResultVO cancel(@RequestParam("openid") String openid,
                           @RequestParam("orderId") String orderId){
        OrderDTO orderDTO = buyerService.cancelOrder(openid, orderId);
        return ResultVO.success();
    }



}
