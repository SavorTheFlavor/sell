package com.me.sell.bean;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 2017-07-23 23:02
 */
@Data
@Entity
public class SellerInfo {

    @Id
    private String sellerId;

    private String username;

    private String password;

    private String openid;
}
