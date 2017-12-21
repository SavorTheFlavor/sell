package com.me.sell.service;

import com.me.sell.bean.ProductCategory;

import java.util.List;

/**
 * Created by Administrator on 2017/12/2.
 */
public interface CategoryService {
    ProductCategory findOne(Integer categoryId);

    List<ProductCategory> findAll();

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypes);;

    ProductCategory save(ProductCategory productCategory);
}
