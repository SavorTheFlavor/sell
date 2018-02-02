package com.me.sell;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan(basePackages = "com.me.sell.dao.mapper")
@EnableCaching  //使用redis缓存
public class SellApplication {

	public static void main(String[] args) {
		SpringApplication.run(SellApplication.class, args);
	}
}
