package com.me.sell.bean;


import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by Administrator on 2017/11/29.
 */
@Entity
@DynamicUpdate //为null的字段不会写到sql里进行更新，没有参与到update，则updateTime就可以被自动被赋值
//@Table(name="product_category") springboot会自动转驼峰
//@Data lombok, 自动为属性生成getter, setter, toString... 估计通过注解处理器API利用反射生成java代码来代替原来的吧
public class ProductCategory {
    @Id
    @GeneratedValue
    private Integer categoryId;
    private String categoryName;
    /** 类目编号*/
    private Integer categoryType;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }


    @Override
    public String toString() {
        return "ProductCategory{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", categoryType=" + categoryType +
                '}';
    }
}
