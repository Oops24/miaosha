package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.UserModel;

/**
 * @Classname UserService
 * @Description TODO
 * @Date 2022/4/27 22:15
 * @Created by Yijie
 */
public interface UserService {
    UserModel getUserById(Integer id);
    void register(UserModel userModel) throws BusinessException;
    UserModel validateLogin(String tlephone, String password) throws BusinessException;
}
