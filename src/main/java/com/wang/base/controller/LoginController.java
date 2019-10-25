package com.wang.base.controller;

import com.wang.base.common.result.Result;
import com.wang.base.service.msg.MsgSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class LoginController {

    @Autowired
    MsgSendService msgSendService;

    @RequestMapping("/")
    @ResponseBody
    public String root() {
        return "游客状态";
    }

    @RequestMapping("/login-error")
    @ResponseBody
    public String loginError() {
        return "登录失败";
    }

    @RequestMapping("/loginSuccess")
    @ResponseBody
    public String index() {
                return "登陆成功";
    }

    @GetMapping("/smsCode")
    @ResponseBody
    public Result getCode(String phone){
        return msgSendService.sendMsg(phone);
    }

}
