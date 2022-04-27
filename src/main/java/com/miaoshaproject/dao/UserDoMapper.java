package com.miaoshaproject.dao;

import com.miaoshaproject.dataobject.UserDo;

public interface UserDoMapper {
    int insert(UserDo row);

    int insertSelective(UserDo row);

    UserDo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserDo row);

    int updateByPrimaryKey(UserDo row);
}