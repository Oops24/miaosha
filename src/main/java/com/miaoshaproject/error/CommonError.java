package com.miaoshaproject.error;

/**
 * @Classname CommonError
 * @Description TODO
 * @Date 2022/4/28 0:37
 * @Created by Yijie
 */
public interface CommonError {
     int getErrCode();
     String getErrMsg();
     CommonError setErrMsg(String errMsg);
}
