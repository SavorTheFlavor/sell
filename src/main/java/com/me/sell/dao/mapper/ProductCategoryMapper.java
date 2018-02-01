package com.me.sell.dao.mapper;

import com.me.sell.bean.ProductCategory;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/2/1.
 */
//使用mybatis注解的方式实现dao
public interface ProductCategoryMapper {

    @Insert("insert into product_category(category_name, category_type) values(#{categoryName,jdbcType=VARCHAR},#{categoryType,jdbcType=INTEGER})")
    int insertByMap(Map<String, Object> params);//使用map接收参数

    @Insert("insert into product_category(category_name, category_type) values(#{categoryName,jdbcType=VARCHAR},#{categoryType,jdbcType=INTEGER})")
    int insertByObject(ProductCategory productCategory);//使用map接收参数


    @Select("select * from product_category where category_type = #{categoryName}")
    @Results({
            @Result(column = "category_id", property = "categoryId"),
            @Result(column = "category_name", property = "categoryName"),
            @Result(column = "category_type", property = "categoryType")
    })//里面是数组.... //反射是无法获取到变量名的，若参数有两个以上时，要加注解或封装成对象或者map
    List<ProductCategory> findByCategoryName(@Param("categoryName") String categoryName);

    //.................
    ProductCategory selectByCategoryType(Integer type);
}
