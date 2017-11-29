package com.me.dao;

import com.me.bean.ProductCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017/11/29.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Test
    public void findOne(){
        ProductCategory productCategory = productCategoryRepository.findOne(1);
        System.err.println(productCategory);
    }

    @Test
    //@Transactional 在test这里加表示测试完就rollback..
    public void update(){
        ProductCategory productCategory = productCategoryRepository.findOne(1);
        productCategory.setCategoryType(222);
        productCategoryRepository.save(productCategory);
    }

    @Test
    public void test222(){
        List<Integer> ids = Arrays.asList(1,222);
        List productCategories = productCategoryRepository.findByCategoryTypeIn(ids);
        showList(productCategories);
    }

    private void showList(List<Object> list){
        for (Object item: list) {
            System.out.println(item);
        }
    }
}