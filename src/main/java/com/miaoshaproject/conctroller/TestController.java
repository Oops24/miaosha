package com.miaoshaproject.conctroller;

import com.miaoshaproject.dao.UserDoMapper;
import com.miaoshaproject.dataobject.UserDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("test")
public class TestController extends BaseController{
    @Autowired
    private UserDoMapper userDoMapper;

    @GetMapping("/select/{id}")
    public String Test(@PathVariable("id") int id){
        String result = "";
        UserDo user = userDoMapper.selectByPrimaryKey( id );
        if (user == null) {
            result = "用户不存在";
        }else {
            result = user.toString();
        }
        return result;
    }
}
