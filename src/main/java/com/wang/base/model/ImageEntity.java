package com.wang.base.model;

import com.wang.base.common.TableIdPrefix;
import com.wang.base.model.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

/***
 * @ClassName: FileName
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/28 8:41
 */
@Data
@Entity
@Table(name = "image")
@TableIdPrefix("TIMAGE")
@SQLDelete(sql = "update image set del_flag = 1 where id = ?")
@Where(clause = "del_flag = 0")
public class ImageEntity extends BaseEntity {
    private String userId;
    private String imageId;
    private Integer type;
    private String masterImageId;
    private String mongoCollectionName;
}
