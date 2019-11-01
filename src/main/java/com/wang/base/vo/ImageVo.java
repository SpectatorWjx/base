package com.wang.base.vo;

import lombok.Data;

/***
 * @ClassName: ImageVo
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/31 16:12
 */
@Data
public class ImageVo {
    private String userId;
    private String imageId;
    private Integer type;
    private String masterImageId;
    private String mongoCollectionName;
}
