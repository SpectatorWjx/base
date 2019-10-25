package com.wang.base.model;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

/***
 * @ClassName: BaseEntity
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/8 11:35
 */
@Data
@MappedSuperclass //表明这是父类，可以将属性映射到子类中使用JPA生成表
@DynamicUpdate //动态赋值
@DynamicInsert
public class BaseEntity implements Serializable {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
}
