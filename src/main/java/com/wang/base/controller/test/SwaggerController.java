package com.wang.base.controller.test;

import com.wang.base.model.RoleEntity;
import com.wang.base.model.UserEntity;
import io.swagger.annotations.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/***
 * @ClassName: UserController
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/15 10:51
 */
@Controller
@RequestMapping("/test")
@Api(value = "SwaggerController" , tags = "测试swagger")
public class SwaggerController {

    @ApiOperation(value = "测试swagger1方法")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "Id", required = true),
            @ApiImplicitParam(name = "userName", value = "用户名", required = true)
    })
    @GetMapping("swagger1")
    @ResponseBody
    public String swagger1(Integer id, String userName) {
        return "与不添加参数注解效果相同";
    }

    @ApiOperation(value = "测试swagger2方法")
    @GetMapping("swagger2")
    @ResponseBody
    public String swagger2( @ApiParam(value = "ID", required = true)Integer id,
                             @ApiParam(value = "用户名", required = true)String userName) {
        return "@ApiParam单个参数body形式";
    }


    @ApiOperation(value = "测试swagger3方法")
    @GetMapping("swagger3")
    @ResponseBody
    public String swagger3(@ApiParam(value = "用户List")@ModelAttribute@RequestBody List<RoleEntity> roles) {
        return "实体参数@ApiParam@ModelAttribute@RequestBody";
    }

    @ApiOperation(value = "测试访问权限方法")
    @GetMapping("swagger4")
    @ResponseBody
    public String swagger4(Integer id, String userName) {
        return "参数不使用注解,默认获取类型,传入方式为query";
    }

    @ApiOperation(value = "测试操作权限方法")
    @ResponseBody
    @PutMapping("swagger5")
    public String swagger5(@ModelAttribute @RequestBody UserEntity user) {
        return "实体参数@ApiParam@ModelAttribute@RequestBody";
    }

}
