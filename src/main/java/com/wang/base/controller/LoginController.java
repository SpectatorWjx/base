package com.wang.base.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class LoginController {

    @RequestMapping("/login-error")
    @ResponseBody
    public String loginError() {
        return "登录失败";
    }


}
