package com.me.service.impl;

import com.me.sell.bean.OrderDetail;
import com.me.sell.dto.OrderDTO;
import com.me.sell.enums.PayStatusEnum;
import com.me.sell.service.impl.OrderServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/5.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl orderService;

    private final String BUYER_OPENID = "asdasda";

    @Test
    public void create() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName("aptitude");
        orderDTO.setBuyerAddress("asad");
        orderDTO.setBuyerPhone("1231");
        orderDTO.setBuyerOpenid(BUYER_OPENID);

        //购物车
        List<OrderDetail> orderDetailList = new ArrayList<>();
        OrderDetail o1 = new OrderDetail();
        o1.setProductId("222");
        o1.setProductQuantity(1);

        OrderDetail o2 = new OrderDetail();
        o2.setProductId("1");
        o2.setProductQuantity(11);

        orderDetailList.add(o1);
        orderDetailList.add(o2);

        orderDTO.setOrderDetailList(orderDetailList);

        OrderDTO result = orderService.create(orderDTO);
        Assert.assertNotNull(result);
    }

    @Test
    public void findOne() throws Exception {
        String orderId = "1512451933166410444";
        OrderDTO orderDTO = orderService.findOne(orderId);
        System.out.println();
    }

    @Test
    public void findList() throws Exception {
        PageRequest pageRequest = new PageRequest(0,2);
        Page<OrderDTO> orderDTOPages = orderService.findList(BUYER_OPENID,pageRequest);
        System.out.println(orderDTOPages.getTotalElements());
    }

    @Test
    public void cancel() throws Exception {
        OrderDTO orderDTO = orderService.findOne("1512452132647961690");
        orderDTO = orderService.cancel(orderDTO);
    }

    @Test
    public void finish() throws Exception {
        OrderDTO orderDTO = orderService.findOne("1512452132647961690");
        orderDTO = orderService.finish(orderDTO);
    }

    @Test
    public void pay() throws Exception {
        OrderDTO orderDTO = orderService.findOne("1512452132647961690");
        orderDTO = orderService.pay(orderDTO);
        Assert.assertEquals(orderDTO.getPayStatus(),PayStatusEnum.SUCCESS.getCode());
    }

}