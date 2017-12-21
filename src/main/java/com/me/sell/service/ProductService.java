package com.me.sell.service;

import com.me.sell.bean.ProductInfo;
import com.me.sell.dto.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Administrator on 2017/12/2.
 */
public interface ProductService {
    ProductInfo findOne(String productId);
    List<ProductInfo> findUpAll();//find all 'up' products
    Page<ProductInfo> findAll(Pageable page);
    ProductInfo save(ProductInfo productInfo);

    //减库存
    void decreaseStock(List<Cart> productCart);
    //加库存
    void increaseStock(List<Cart> productCart);
}
