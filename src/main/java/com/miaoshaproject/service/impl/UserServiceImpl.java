package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.UserDoMapper;
import com.miaoshaproject.dao.UserPasswordDoMapper;
import com.miaoshaproject.dataobject.UserDo;
import com.miaoshaproject.dataobject.UserPasswordDo;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if(userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        if(userModel.getAge() == null
                || userModel.getAge() ==null
                || StringUtils.isEmpty(userModel.getName())
                || StringUtils.isEmpty(userModel.getTelphone())){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        UserDo userDo = convertUserDoFromUserModer(userModel);

        try {
            userDoMapper.insertSelective(userDo);
        }catch (DuplicateKeyException ex){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"该手机号已重复注册");
        }

        userModel.setId(userDo.getId());

        UserPasswordDo userPasswordDo = convertPasswordFromUserModer(userModel);
        userPasswordDoMapper.insertSelective(userPasswordDo);
    }

    @Override
    public UserModel validateLogin(String telphone, String encreptPassword) throws BusinessException {
        UserDo userDo = userDoMapper.selectByTelPhone(telphone);

        if(userDo ==null){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        UserPasswordDo userPasswordDo = userPasswordDoMapper.selectByUserId(userDo.getId());
        UserModel userModel = convertFromUserDo(userDo, userPasswordDo);

        //对比用户信息内加密的密码是否和传输进来的密码一致
        if(!StringUtils.equals(encreptPassword, userModel.getEncrpPassword())){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        return userModel;
    }

    private UserPasswordDo convertPasswordFromUserModer(UserModel userModel) {
        UserPasswordDo userPasswordDo = new UserPasswordDo();
        if (userModel == null)
            return null;
        userPasswordDo.setEncrptPassword(userModel.getEncrpPassword());
        userPasswordDo.setUserId(userModel.getId());
        return userPasswordDo;
    }

    private UserDo convertUserDoFromUserModer(UserModel userModel) {
        UserDo userDo = new UserDo();
        if (userModel == null)
            return null;
        BeanUtils.copyProperties(userModel, userDo);
        return userDo;
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
