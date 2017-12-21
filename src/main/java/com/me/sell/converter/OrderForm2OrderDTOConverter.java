package com.me.sell.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.me.sell.bean.OrderDetail;
import com.me.sell.dto.OrderDTO;
import com.me.sell.enums.ResultEnum;
import com.me.sell.exception.SellException;
import com.me.sell.form.OrderForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Administrator on 2017/12/11.
 */
public class OrderForm2OrderDTOConverter {

    private static Logger logger = LoggerFactory.getLogger(OrderForm2OrderDTOConverter.class);

    public static OrderDTO convert(OrderForm orderForm){
        Gson gson = new Gson();
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerOpenid(orderForm.getOpenid());
        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        orderDTO.setBuyerAddress(orderForm.getAddress());
        //convert json string to json object
        List<OrderDetail> orderDetailList = null;
        try {
            orderDetailList = gson.fromJson(orderForm.getItems(),
                    new TypeToken<List<OrderDetail>>() {
                    }.getType());
        }catch (Exception e){
            logger.error("[json object convert failed!], string={}",orderForm.getItems());
            throw new SellException(ResultEnum.PARAMETER_ERROR);
        }
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }
}
