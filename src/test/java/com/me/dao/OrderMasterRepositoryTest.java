package com.me.dao;

import com.me.sell.bean.OrderMaster;
import com.me.sell.dao.OrderMasterRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/12/4.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMasterRepositoryTest {

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Test
    public void test1() throws Exception {
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setBuyerName("abc");
        orderMaster.setOrderId("111");
        orderMaster.setBuyerPhone("123456789123");
        orderMaster.setBuyerAddress("幕课网");
        orderMaster.setBuyerOpenid("12345");
        orderMaster.setOrderAmount(new BigDecimal(2.5));
        orderMaster = orderMasterRepository.save(orderMaster);
        Assert.assertNotNull(orderMaster);
    }

    @Test
    public void findByBuyerOpenid() throws Exception {
        PageRequest pageRequest = new PageRequest(0,2);
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid("12345",pageRequest);
        System.out.println(orderMasterPage.getTotalPages());
    }

}