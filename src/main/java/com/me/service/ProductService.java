package com.me.service;

import com.me.bean.ProductInfo;
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
}
