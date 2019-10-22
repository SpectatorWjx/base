package com.wang.base.common.page;

import org.springframework.data.domain.Page;

import java.util.List;

public class BasePageUtils {
    public static<T> BasePage<T> getBasePage(Page page, List<T> list){
        BasePage<T> basePage =  new BasePage<>();
        basePage.setContent(list);
        basePage.setSize(page.getContent().size());
        basePage.setTotal(page.getTotalElements());
        basePage.setTotalPages(page.getTotalPages());
        basePage.setPageNumber(page.getNumber()+1);
        return basePage;
    }
}
