package com.me.dto;

import lombok.Data;

/**
 * 购物车
 */
@Data
public class Cart {
    private String productId;
    private Integer productQuantity;

    public Cart(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
