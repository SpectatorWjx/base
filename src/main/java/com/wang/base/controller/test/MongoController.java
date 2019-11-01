package com.wang.base.controller.test;

import com.wang.base.common.result.Result;
import com.wang.base.common.utils.ResultUtil;
import com.wang.base.model.mongo.FileDocument;
import com.wang.base.service.mongo.MongoService;
import com.wang.base.vo.ImageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/***
 * @ClassName: MongoController
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/28 8:48
 */
@RequestMapping("mongo")
@Controller
public class MongoController {

    @Autowired
    private MongoService mongoService;

    /**
     * 列表数据
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public List<FileDocument> list(Integer pageIndex, Integer pageSize){
        return mongoService.listFilesByPage(pageIndex,pageSize);
    }

    /**
     * 在线显示图片
     * @param id 文件id
     * @return
     */
    @GetMapping("/view/{id}")
    @ResponseBody
    public void serveImageOnline(@PathVariable String id, HttpServletResponse response) {
        mongoService.serveImageOnline(id, response);
    }

    /**
     * 表单上传文件
     * 当数据库中存在该md5值时，可以实现秒传功能
     * @param file 文件
     * @return
     */
    @PostMapping("/upload")
    @ResponseBody
    public Result formUpload(@RequestParam("image") MultipartFile file){
        ImageVo imageVo = mongoService.saveFile(file);
        return ResultUtil.success(imageVo);
    }

}
