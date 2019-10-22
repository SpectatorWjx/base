package com.wang.base.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class LoginController {

    @RequestMapping("/")
    @ResponseBody
    public String root() {
        return "未登录，请先登录";
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

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

}
