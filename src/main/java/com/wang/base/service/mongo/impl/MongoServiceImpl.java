package com.wang.base.service.mongo.impl;

import cn.hutool.crypto.SecureUtil;
import com.wang.base.common.exception.BaseException;
import com.wang.base.common.utils.SecurityAuthUtil;
import com.wang.base.common.utils.StringUtil;
import com.wang.base.common.utils.image.ThumbnailImageUtil;
import com.wang.base.common.utils.mongo.MongoManageUtils;
import com.wang.base.dao.ImageJpa;
import com.wang.base.model.ImageEntity;
import com.wang.base.model.mongo.FileDocument;
import com.wang.base.service.mongo.MongoService;
import com.wang.base.vo.ImageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.Optional;


/***
 * @ClassName: MongoServiceImpl
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/28 8:52
 */
@Service
public class MongoServiceImpl implements MongoService {


    @Autowired
    ImageJpa imageJpa;

    /**
     * 表单上传图片并返回缩略图信息
     * @param file
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImageVo saveFile(MultipartFile file) {
        if(file == null || file.isEmpty()){
            throw new BaseException("文件不能为空");
        }
        //检查是否是图片
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(bi == null){
           throw new BaseException(300,"请上传图片文件");
        }

        String userId = SecurityAuthUtil.getUserId();
        if(StringUtil.isEmpty(userId)){
            return null;
        }
        ImageEntity imageEntity = saveImageToMongo(file,userId);
        imageJpa.saveAndFlush(imageEntity);

        ImageVo imageVo = new ImageVo();
        BeanUtils.copyProperties(imageEntity,imageVo);
        return imageVo;
    }


    private ImageEntity saveImageToMongo(MultipartFile file, String userId){
        String collectionName = SecureUtil.md5(userId);
         /*
        获取图片参数
         */
        InputStream inputStream = null;
        ByteArrayOutputStream baos = null;
        FileDocument masterDocument = null;
        FileDocument compressDocument = null;
        try {
            inputStream = file.getInputStream();
            FileDocument fileDocument = MongoManageUtils.getDocumentByFile(file);
            String fileName = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."))+"_0.25";
            OutputStream outputStream = ThumbnailImageUtil.generateThumbnailDirectory(inputStream, 0.25);
            baos = (ByteArrayOutputStream) outputStream;
            byte[] bytes = baos.toByteArray();
            InputStream compressStream = new ByteArrayInputStream(bytes);
            MultipartFile compressFile = new MockMultipartFile(file.getName(), fileName, fileDocument.getContentType(), compressStream);
            /*
            原图存入mongo
             */
            masterDocument = MongoManageUtils.saveImage(file, collectionName);
            /*
            压缩图存入mongo
             */
            compressDocument = MongoManageUtils.saveImage(compressFile, collectionName);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setImageId(compressDocument.getId());
        imageEntity.setMasterImageId(masterDocument.getId());
        imageEntity.setMongoCollectionName(collectionName);
        imageEntity.setUserId(userId);
        return imageEntity;
    }

    @Override
    public void serveImageOnline(String id, HttpServletResponse response) {
        String userId = SecurityAuthUtil.getUserId();
        if(StringUtil.isEmpty(userId)){
            return;
        }
        String collectionName = SecureUtil.md5(userId);
        FileDocument file = getById(id, collectionName);
        if(file == null){
            return;
        }
        //读取要下载的文件，保存到文件输入流
        OutputStream out = null;
        try {
            response.setHeader("content-Type", file.getContentType());
            response.setContentType(file.getContentType()+";charset=utf-8");
            out = response.getOutputStream();
            out.write(file.getContent());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 查询附件
     * @param id 文件id
     * @return
     * @throws IOException
     */
    @Override
    public FileDocument getById(String id, String collectionName){
        Optional<FileDocument> fileDocument = MongoManageUtils.getImageById(id, collectionName);
        if(fileDocument.isPresent()){
            return fileDocument.get();
        }
        return null;
    }

    @Override
    public List<FileDocument> listFilesByPage(Integer pageIndex, Integer pageSize) {
//        Query query = new Query().with(new Sort(Sort.Direction.DESC, "uploadDate"));
//        long skip = (pageIndex -1) * pageSize;
//        query.skip(skip);
//        query.limit(pageSize);
//        Field field = query.fields();
//        field.exclude("content");
//        List<FileDocument> files = mongoTemplate.find(query , FileDocument.class , collectionName);
        return null;
    }
}
