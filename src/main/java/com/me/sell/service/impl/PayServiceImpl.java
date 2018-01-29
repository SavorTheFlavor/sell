package com.me.sell.service.impl;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundRequest;
import com.lly835.bestpay.model.RefundResponse;
import com.lly835.bestpay.service.BestPayService;
import com.me.sell.dto.OrderDTO;
import com.me.sell.enums.ResultEnum;
import com.me.sell.exception.SellException;
import com.me.sell.service.OrderService;
import com.me.sell.service.PayService;
import com.me.sell.util.MathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/1/8.
 */
@Service
public class PayServiceImpl implements PayService {

    private static final String ORDER_NAME = "Wechat Restaurant";

    private Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private BestPayService bestPayService;

    @Override
    public void create(OrderDTO orderDTO) {
        PayRequest payRequest = new PayRequest();
        payRequest.setOpenid(orderDTO.getBuyerOpenid());
        payRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        payRequest.setOrderId(orderDTO.getOrderId());
        payRequest.setOrderName(ORDER_NAME);
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        //PayResponse payResponse = bestPayService.pay(payRequest);
    }

    @Override
    public PayResponse notify(String notifyData) {
        //验证签名      (bestPaySDK已做)
        //支付状态，是否成功... (bestPaySDK已做)
        //支付金额
        //支付人
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);

        OrderDTO orderDTO = orderService.findOne(payResponse.getOrderId());
        if(orderDTO == null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        //注意，浮点数存储时会有偏差，只要接近某一精度就算是相等了
        if(MathUtil.equals(orderDTO.getOrderAmount().doubleValue(),payResponse.getOrderAmount())){
            throw new SellException(ResultEnum.WXPAY_ORDER_AMOUNT_VERIFY_ERROR);
        }
        // change the pay status of the order .....
        orderService.pay(orderDTO);
        return payResponse;
    }

    @Override
    public RefundResponse refund(OrderDTO orderDTO) {
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setOrderId(orderDTO.getOrderId());
        refundRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        refundRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        logger.info("[微信退款] refundRequest={}",refundRequest);
        RefundResponse refundResponse = bestPayService.refund(refundRequest);
        logger.info("[微信退款] refundResponse={}",refundResponse);
        return refundResponse;
    }
}
