package com.wang.base.controller.test;

import com.wang.base.config.apiVersion.ApiVersion;
import com.wang.base.model.UserEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 * @ClassName:
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/24 10:29
 */
@RestController
@RequestMapping("/api")
public class VersionController {


    /**
    * @api {get} /api/v1/version
    * @apiVersion 1.0.0
    * @apiDescription  接口描述
    * @apiGroup  VersionController
    * @apiParam  {Integer} test1
    * @apiPermission  login
    * @apiSuccessExample Success-Response:
    *     {
    *       "status": "200",
    *       "message": "success",
    *       "data": object
    *     }
    */
    @ApiVersion(1)
    @GetMapping("/{version}/version")
    public Object test1(Integer test1){
        System.out.println(1/0);
        return "version 1";
    }

    /**
    * @api {get} /api/v2/version
    * @apiVersion 2.0.0
    * @apiDescription  接口描述
    * @apiGroup  VersionController
    * @apiParam  {String} test2
    * @apiPermission  login
    * @apiSuccessExample Success-Response:
    *     {
    *       "status": "200",
    *       "message": "success",
    *       "data": object
    *     }
    */
	@ApiVersion(2)
    @GetMapping("/{version}/version")
    public Object test2(String test2){
        return "version 2";
    }

    /**
    * @api {get} /api/v3/version
    * @apiVersion 3.0.0
    * @apiDescription  接口描述
    * @apiGroup  VersionController
    * @apiParam  {User} test3
     * @apiParam  {String} name
    * @apiPermission  login
    * @apiSuccessExample Success-Response:
    *     {
    *       "status": "200",
    *       "message": "success",
    *       "data": object
    *     }
    */
    @ApiVersion(3)
    @GetMapping("/{version}/version")
    public Object test3(UserEntity test3, String name){
        return "version 3";
    }
}
