package com.me.sell.service.impl;

import com.me.sell.bean.OrderDetail;
import com.me.sell.bean.OrderMaster;
import com.me.sell.converter.OrderMaster2OrderDTOConverter;
import com.me.sell.dao.OrderDetailRepository;
import com.me.sell.dao.OrderMasterRepository;
import com.me.sell.dto.Cart;
import com.me.sell.enums.ResultEnum;
import com.me.sell.exception.SellException;
import com.me.sell.service.*;
import com.me.sell.util.KeyUtil;
import com.me.sell.bean.ProductInfo;
import com.me.sell.dto.OrderDTO;
import com.me.sell.enums.OrderStatusEnum;
import com.me.sell.enums.PayStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2017/12/4.
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private PayService payService;

    @Autowired
    private PushMessageService pushMessageService;

    @Autowired
    private WebSocket webSocket;

    private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {
        //notice:商品单价一定要从数据库中取出来，前端传过来1毛钱的话就真的当作一毛钱卖吗

        String orderId = KeyUtil.generateUniqueKey(); //orderMaster
        BigDecimal orderAmount = new BigDecimal(0);

        //1. 查询商品 （quantity, price）
        for(OrderDetail orderDetail : orderDTO.getOrderDetailList()){
            ProductInfo productInfo = productService.findOne(orderDetail.getProductId());
            if(productInfo == null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            //2. 计算订单总价
            orderAmount = productInfo.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                    .add(orderAmount);

            //写订单详情入数据库
            orderDetail.setDetailId(KeyUtil.generateUniqueKey());
            orderDetail.setOrderId(orderId);
            BeanUtils.copyProperties(productInfo,orderDetail);
            orderDetailRepository.save(orderDetail);
        }
        //3. 写主订单入库
        OrderMaster orderMaster = new OrderMaster();
        //要先copy完properties再set其他的properties，不然会被覆盖
        orderDTO.setOrderId(orderId);
        orderDTO.setOrderAmount(orderAmount);
        orderDTO.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderDTO.setPayStatus(PayStatusEnum.WAIT.getCode());
        BeanUtils.copyProperties(orderDTO,orderMaster);

        orderMasterRepository.save(orderMaster);

        //4. 减库存
        List<Cart> cartList =
                orderDTO.getOrderDetailList()
                .stream()
                .map(e -> new Cart(e.getProductId(),e.getProductQuantity()))
                .collect(Collectors.toList());
        productService.decreaseStock(cartList);

        //向前端推送新订单消息
        webSocket.sendMessage("有新订单啊！");

        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId) {
        OrderMaster orderMaster = orderMasterRepository.findOne(orderId);
        if(orderMaster == null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster,orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> orderMasterPages = orderMasterRepository.findByBuyerOpenid(buyerOpenid, pageable);
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterPages.getContent());
        return new PageImpl<OrderDTO>(orderDTOList,pageable,orderMasterPages.getTotalElements());
    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
        OrderMaster orderMaster = new OrderMaster();
        //判断订单状态
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            logger.error("[cancel order] the order has finished..., orderId={},orderStatus",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO,orderMaster);

        OrderMaster updateRes = orderMasterRepository.save(orderMaster);
        if(updateRes == null){
            logger.error("[cancel order] cancel order failed!..., orderId={},orderStatus",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_UPDATE_FAILED);
        }
        //返还库存
        if(orderDTO.getOrderDetailList().isEmpty()){
            logger.error("[cancel order] nothing in the cart..., orderId={},orderStatus",orderDTO.getOrderId(),orderDTO.getOrderStatus());
        }
        List<Cart> cartList = orderDTO.getOrderDetailList().stream()
                .map(e -> new Cart(e.getProductId(),e.getProductQuantity()))
                .collect(Collectors.toList());
        productService.increaseStock(cartList);

        //if paid, refund the money
        if(orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())){
            payService.refund(orderDTO);
        }
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO finish(OrderDTO orderDTO) {
        OrderMaster orderMaster = new OrderMaster();
        //判断订单状态
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            logger.error("[finish order] the order can't be finished..., orderId={},orderStatus",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        BeanUtils.copyProperties(orderDTO,orderMaster);

        OrderMaster updateRes = orderMasterRepository.save(orderMaster);
        if(updateRes == null){
            logger.error("[finish order] cancel order failed!..., orderId={},orderStatus",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_UPDATE_FAILED);
        }

        //推送微信模板消息
        pushMessageService.orderStatus(orderDTO);

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO pay(OrderDTO orderDTO) {
        OrderMaster orderMaster = new OrderMaster();

        //判断订单状态
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            logger.error("[pay order] the order can't be paid..., orderId={},orderStatus",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //判断支付状态
        if(!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())){
            logger.error("[pay order] the pay status error..., order={}",orderDTO);
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }

        //修改支付状态
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        BeanUtils.copyProperties(orderDTO,orderMaster);

        OrderMaster updateRes = orderMasterRepository.save(orderMaster);
        if(updateRes == null){
            logger.error("[pay order] pay order failed!..., order={}",orderDTO);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAILED);
        }

        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(Pageable pageable) {
        Page<OrderMaster> orderMasters = orderMasterRepository.findAll(pageable);
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasters.getContent());
        return new PageImpl<OrderDTO>(orderDTOList,pageable,orderMasters.getTotalElements());
    }
}
