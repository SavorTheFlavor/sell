package com.me.service.impl;

import com.me.sell.bean.ProductInfo;
import com.me.sell.service.impl.ProductServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Administrator on 2017/12/2.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceImplTest {

    @Autowired
    private ProductServiceImpl productService;

    @Test
    public void findOne() throws Exception {
        ProductInfo productInfo = productService.findOne("1");
        System.out.print(productInfo);
    }

    @Test
    public void findAll() throws Exception {
        //第0页开始，每页两条
        PageRequest pageRequest = new PageRequest(0,2);
        Page<ProductInfo> productInfoPage = productService.findAll(pageRequest);
        System.out.println(productInfoPage.getTotalPages());
        System.out.println(productInfoPage.getContent());
        System.out.println(productInfoPage.getNumberOfElements());
    }

}