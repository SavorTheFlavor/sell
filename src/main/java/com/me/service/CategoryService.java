package com.me.service;

import com.me.bean.ProductCategory;

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
