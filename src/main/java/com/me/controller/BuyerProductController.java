package com.me.controller;

import com.me.vo.ProductInfoVO;
import com.me.vo.ProductsVO;
import com.me.vo.ResultVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/12/2.
 */
@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @GetMapping("/list")
    public ResultVO list(){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("fail!");

        ProductsVO productListVO = new ProductsVO();
        ProductInfoVO productInfoVO = new ProductInfoVO();

        productInfoVO.setProductName("meat");
        productInfoVO.setProductIcon("http://xxx.pp.c/kk.jpg");

        productListVO.setProductInfoVOList(Arrays.asList(productInfoVO));
        resultVO.setData(Arrays.asList(productListVO));
        return resultVO;
    }
}
