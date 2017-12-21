package com.me.sell.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by Administrator on 2017/12/11.
 * about the form data and its validating...
 */
@Data
public class OrderForm {
    /**
     * buyer name.....
     */
    @NotEmpty(message = "name required!")
    private String name;
    /**
     * buyer address
     */
    @NotEmpty(message = "address required!")
    private String address;
    /**
     * buyer openid
     */
    @NotEmpty(message = "openid is indispensable!")
    private String openid;
    /**
     * buyer phone
     */
    @NotEmpty(message = "phone required!")
    private String phone;

    /**
     * buyer cart
     */
    @NotEmpty(message = "cart required!")
    private String items;

}
