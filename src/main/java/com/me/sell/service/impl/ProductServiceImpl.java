package com.me.sell.service.impl;

import com.me.sell.exception.SellException;
import com.me.sell.service.ProductService;
import com.me.sell.bean.ProductInfo;
import com.me.sell.dao.ProductInfoRepository;
import com.me.sell.dto.Cart;
import com.me.sell.enums.ProductStatusEnum;
import com.me.sell.enums.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2017/12/2.
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Override
    public ProductInfo findOne(String productId) {
        return productInfoRepository.findOne(productId);
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return productInfoRepository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable page) {
        return productInfoRepository.findAll(page);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return productInfoRepository.save(productInfo);
    }

    @Override
    @Transactional
    public void decreaseStock(List<Cart> productCart) {
        for(Cart cart : productCart){
            ProductInfo productInfo = productInfoRepository.findOne(cart.getProductId());
            if(productInfo == null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            Integer stock = productInfo.getProductStock() - cart.getProductQuantity();
            if(stock < 0){
                throw new SellException(ResultEnum.PRODUCT_STOCK_NOT_ENOUGH);
            }
            productInfo.setProductStock(stock);
            productInfoRepository.save(productInfo);
        }
    }

    @Override
    public void increaseStock(List<Cart> productCart) {
        for(Cart cart : productCart){
            ProductInfo productInfo = productInfoRepository.findOne(cart.getProductId());
            if(productInfo == null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            Integer stock = productInfo.getProductStock() + cart.getProductQuantity();
            productInfo.setProductStock(stock);
            productInfoRepository.save(productInfo);
        }
    }
}
