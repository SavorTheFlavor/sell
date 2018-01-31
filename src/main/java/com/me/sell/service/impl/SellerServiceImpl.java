package com.me.sell.service.impl;

import com.me.sell.bean.SellerInfo;
import com.me.sell.dao.SellerInfoRepository;
import com.me.sell.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 2017-07-29 23:15
 */
@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerInfoRepository repository;

    @Override
    public SellerInfo findSellerInfoByOpenid(String openid) {
        return repository.findByOpenid(openid);
    }
}
