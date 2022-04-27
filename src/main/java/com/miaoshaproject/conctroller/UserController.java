package com.miaoshaproject.conctroller;

import com.miaoshaproject.conctroller.viewobject.UserVo;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.impl.UserServiceImpl;
import com.miaoshaproject.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname UserController
 * @Description TODO
 * @Date 2022/4/27 22:09
 * @Created by Yijie
 */

@RestController
@RequestMapping("/user")
public class UserController extends BaseController{

    @Autowired
    private UserServiceImpl userService;

    /**
     * 根据id查询
     * @param id
     * @return
     * @throws BusinessException
     */
    @GetMapping("/getSingle/{id}")
    public CommonReturnType getUser(@PathVariable("id") Integer id) throws BusinessException {
        UserModel userModel = userService.getUserById(id);
        UserVo userVo = convertFromUserModel(userModel);
        //返回通用对象
        return CommonReturnType.create(userVo);
    }
    /**
     * model转换
     * @param userModel
     * @return
     * @throws BusinessException
     */
    private UserVo convertFromUserModel(UserModel userModel) throws BusinessException {
        if(userModel == null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userModel, userVo);
        return userVo;
    }
}