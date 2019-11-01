package com.wang.base.common.utils.image;

import net.coobird.thumbnailator.Thumbnails;

import java.io.*;

/**
 * 图片压缩工具类
 *
 * @author lnj
 * createTime 2018-10-19 15:31
 **/
public class ThumbnailImageUtil {

    // 图片默认缩放比率
    private static final double DEFAULT_SCALE = 0.25d;


    /**
     * 生成缩略图到指定的目录
     *
     * @param scale    图片缩放率
     * @param inputStreams    要生成缩略图的文件列表
     * @throws IOException
     */
    public static OutputStream generateThumbnailDirectory(InputStream inputStreams, Double scale) throws IOException {
        if(null==scale){
            scale = DEFAULT_SCALE;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(inputStreams)
                // 图片缩放率，不能和size()一起使用
                .scale(scale)
                .outputQuality(0.95f)
                .toOutputStream(outputStream);
        return outputStream;
    }
}