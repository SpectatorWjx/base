package com.wang.base.service.mongo;

import com.wang.base.model.mongo.FileDocument;
import com.wang.base.vo.ImageVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

/***
 * @ClassName: MongoService
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/28 8:51
 */
public interface MongoService {
    /**
     * 保存图片
     * @param file
     * @return
     */
    ImageVo saveFile(MultipartFile file);


    /**
     * 根据id获取文件
     * @param id
     * @return
     */
    FileDocument getById(String id, String collectionName);

    /**
     * 分页查询，按上传时间降序
     * @param pageIndex
     * @param pageSize
     * @return
     */
    List<FileDocument> listFilesByPage(Integer pageIndex, Integer pageSize);


    void serveImageOnline(String id, HttpServletResponse response);
}
