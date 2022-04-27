package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.UserDoMapper;
import com.miaoshaproject.dao.UserPasswordDoMapper;
import com.miaoshaproject.dataobject.UserDo;
import com.miaoshaproject.dataobject.UserPasswordDo;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Classname UserServiceImpl
 * @Description TODO
 * @Date 2022/4/27 22:16
 * @Created by Yijie
 */
@Service
public class UserServiceImpl implements UserService {

    private UserDoMapper userDoMapper;
    private UserPasswordDoMapper userPasswordDoMapper;

    @Autowired
    protected void Autowired(
            UserDoMapper userDoMapper,
            UserPasswordDoMapper userPasswordDoMapper){
        this.userDoMapper = userDoMapper;
        this.userPasswordDoMapper = userPasswordDoMapper;
    }
    @Override
    public UserModel getUserById(Integer id) {
        UserDo userDo = userDoMapper.selectByPrimaryKey(id);
        UserPasswordDo userPasswordDo = userPasswordDoMapper.selectByUserId(id);
        return convertFromUserDo(userDo, userPasswordDo);
    }

    private UserModel convertFromUserDo(UserDo userDo, UserPasswordDo userPasswordDo) {
        if (userDo == null){
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDo, userModel);
        if(userPasswordDo != null){
            userModel.setEncrpPassword(userPasswordDo.getEncrptPassword());
        }
        return userModel;
    }
}
