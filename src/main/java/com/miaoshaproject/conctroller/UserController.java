package com.miaoshaproject.conctroller;

import com.alibaba.druid.util.StringUtils;
import com.miaoshaproject.conctroller.viewobject.UserVo;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.impl.UserServiceImpl;
import com.miaoshaproject.service.model.UserModel;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @Classname UserController
 * @Description TODO
 * @Date 2022/4/27 22:09
 * @Created by Yijie
 */

@RestController
@RequestMapping("/user")
@CrossOrigin(originPatterns = "*", allowCredentials="true",allowedHeaders = "*")
public class UserController extends BaseController{

    private UserServiceImpl userService;
    private HttpServletRequest httpServletRequest;

    @Autowired
    private void Autowired(
            UserServiceImpl userService,
            HttpServletRequest httpServletRequest){
        this.userService = userService;
        this.httpServletRequest = httpServletRequest;
    }

    /**
     * 用户登录
     * @param telphone
     * @param password
     * @return
     */
    @PostMapping("/login")
    public CommonReturnType login(@RequestParam("telphone") String telphone,
                                  @RequestParam("password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //入参校验
        if(StringUtils.isEmpty(telphone) || StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //用户登陆服务
        UserModel userModel = userService.validateLogin(telphone, this.EncodeByMd5(password));
        //将登录凭证加入到用户登陆成功的session内
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN", true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER", userModel);
        return CommonReturnType.create(null);
    }

    /**
     * 用户注册
     * @param telphone
     * @param name
     * @param age
     * @param gender
     * @param password
     * @param otpCode
     * @return
     * @throws BusinessException
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/register")
    public CommonReturnType register(@RequestParam("telphone") String telphone,
                                     @RequestParam("name") String name,
                                     @RequestParam("age") Integer age,
                                     @RequestParam("gender") Integer gender,
                                     @RequestParam("password") String password,
                                     @RequestParam("otpCode") String otpCode) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证手机号和对应的otpCode相符合
        String inSessionotpCode = (String)this.httpServletRequest.getSession().getAttribute("otpCode");
        if(!StringUtils.equals(otpCode, inSessionotpCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "短信验证码不正确");
        }
        //用户注册流程
        UserModel userModel = new UserModel();
        userModel.setTelphone(telphone);
        userModel.setName(name);
        userModel.setGender(gender);
        userModel.setAge(age);
        userModel.setRegisterMode("by phone");
        userModel.setEncrpPassword( EncodeByMd5(password));

        userService.register(userModel);
        return CommonReturnType.create(null);
    }

    private String EncodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        //加密字符串
        String newStr = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return newStr;
    }

    @PostMapping("/getotp")
    public CommonReturnType getOtp(@RequestParam("telphone") String telphone){
        Random random = new Random();
        int randomInt = random.nextInt(99999);

        randomInt += 100000;
        String otpCode = String.valueOf(randomInt);
        httpServletRequest.getSession().setAttribute("otpCode", otpCode);
        return CommonReturnType.create(otpCode);
    }
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