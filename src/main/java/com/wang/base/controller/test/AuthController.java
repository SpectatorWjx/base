package com.wang.base.controller.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/***
 * @ClassName: UserController
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/15 10:51
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("index")
    @ResponseBody
    public String userIndex(String userName) {
        return "拥有访问权限";
    }

    @ResponseBody
    @PutMapping("operate")
    public String userOperate() {
        return "拥有操作权限";
    }
}
