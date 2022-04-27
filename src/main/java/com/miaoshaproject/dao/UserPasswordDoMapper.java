package com.miaoshaproject.dao;

import com.miaoshaproject.dataobject.UserPasswordDo;

public interface UserPasswordDoMapper {
    int insert(UserPasswordDo row);

    int insertSelective(UserPasswordDo row);

    UserPasswordDo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserPasswordDo row);

    int updateByPrimaryKey(UserPasswordDo row);

    UserPasswordDo selectByUserId(Integer userId);
}