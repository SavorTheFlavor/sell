package com.me.sell.controller;

import com.me.sell.service.CategoryService;
import com.me.sell.service.ProductService;
import com.me.sell.vo.ProductInfoVO;
import com.me.sell.vo.ProductsVO;
import com.me.sell.vo.ResultVO;
import com.me.sell.bean.ProductCategory;
import com.me.sell.bean.ProductInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2017/12/2.
 */
@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    //key默认为方法参数值
    @Cacheable(cacheNames = "product", key = "1234") //cacheNames：一个cache的name，key:在这个cache中的key值(value就是这个方法的返回值
    //@Cacheable(key = "#productId",condition = "#productId.length() > 3",unless="#result.getCode() != 0")
    public ResultVO list(){

        //查询所有上架商品
        List<ProductInfo> productInfoList = productService.findUpAll();

        //获取上面查出的商品的类目列表
        //传统方法
//        for (ProductInfo productInfo : productInfoList) {
//            categoryTypeList.add(productInfo.getCategoryType());
//        }
        //流式计算
        List<Integer> categoryTypeList = productInfoList.stream()
                .map(e -> e.getCategoryType())
                .collect(Collectors.toList());

        //根据类目编号查找类目
        List<ProductCategory> productCategoryList = categoryService.findByCategoryTypeIn(categoryTypeList);

        //拼装数据
        //根据类目分类商品
        List<ProductsVO> productsVOList = new ArrayList<>();
        for(ProductCategory productCategory : productCategoryList){
            ProductsVO productsVO = new ProductsVO();
            productsVO.setCategoryName(productCategory.getCategoryName());
            productsVO.setCategoryType(productCategory.getCategoryType());
            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            productsVO.setProductInfoVOList(productInfoVOList);
            //遍历当前类目下的商品
            for (ProductInfo productInfo : productInfoList){
                if(productInfo.getCategoryType().equals(productCategory.getCategoryType())){
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo,productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }
            productsVOList.add(productsVO);
        }
        return ResultVO.success(productsVOList);
    }
}
