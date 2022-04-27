package com.miaoshaproject.error;

/**
 * @Classname EmBusinessError
 * @Description TODO
 * @Date 2022/4/28 0:40
 * @Created by Yijie
 */
public enum EmBusinessError implements CommonError {
    //通用错误类型00001
    PARAMETER_VALIDATION_ERROR(10001, "参数不合法"),
    //未知错误
    UNKNOW_EXCEPTION(10002, "未知错误"),
    //10000开头为用户信息相关错误定义
    USER_NOT_EXIST(20001, "用户不存在");


    EmBusinessError(int errCode, String errMsg){
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    private int errCode;
    private String errMsg;

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
