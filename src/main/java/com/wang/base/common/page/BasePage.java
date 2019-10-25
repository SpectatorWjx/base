package com.wang.base.common.page;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
/***
 * @ClassName:
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/24 10:29
 */
@Data
@JsonIgnoreProperties
public class BasePage<T> implements Serializable {

    private int pageNumber;

    private int size;

    private long total;

    private int totalPages;

    private List<T> content;
}
