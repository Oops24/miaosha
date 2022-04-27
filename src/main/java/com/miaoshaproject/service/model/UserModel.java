package com.miaoshaproject.service.model;

import lombok.Data;

/**
 * @Classname UserModel
 * @Description TODO
 * @Date 2022/4/27 22:19
 * @Created by Yijie
 */
@Data
public class UserModel {

    private Integer id;
    private String name;
    private Byte gender;
    private Integer age;
    private String telphone;
    private String registerMode;
    private String thirdPartyId;
    private String encrpPassword;
}
