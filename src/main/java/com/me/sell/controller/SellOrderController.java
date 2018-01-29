package com.me.sell.controller;

import com.me.sell.dto.OrderDTO;
import com.me.sell.enums.OrderStatusEnum;
import com.me.sell.exception.SellException;
import com.me.sell.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * 卖家端后台订单管理的controller
 */
@Controller
@RequestMapping("/seller/order")
public class SellOrderController {

    @Autowired
    private OrderService orderService;

    private Logger logger = LoggerFactory.getLogger(SellOrderController.class);

    /**
     *
     * @param page page index, from 0 to ....
     * @param size the size of a page
     * @param map
     * @return
     */
    @RequestMapping("/list")
    public ModelAndView list(@RequestParam(value = "page", defaultValue = "1" )Integer page,
                            @RequestParam(value = "size", defaultValue = "8")Integer size,
                            Map<String, Object> map){
        PageRequest pageRequest = new PageRequest(page-1 , size);
        Page<OrderDTO> orderDTOPage = orderService.findList(pageRequest);
        map.put("orderDTOPage",orderDTOPage);
        map.put("currentPage", page);
        map.put("size",size);
        return new ModelAndView("order/list",map);
    }

    @RequestMapping("/cancel")
    public ModelAndView cancel(@RequestParam("orderId") String orderId,Map<String,Object> map){
        try {
            OrderDTO orderDTO = orderService.findOne(orderId);
            orderService.cancel(orderDTO);
        }catch (SellException e){
            logger.info("订单取消失败！");
            map.put("msg", e.getMessage());
            map.put("url","/sell/seller/order/list");
            return new ModelAndView("common/error",map);
        }
        logger.info("订单取消成功！");
        map.put("msg","订单取消成功！");
        map.put("url","/sell/seller/order/list");
        return new ModelAndView("common/success",map);
    }

}
