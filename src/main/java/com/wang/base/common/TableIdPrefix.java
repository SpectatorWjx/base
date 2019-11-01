package com.wang.base.common;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/***
 * @ClassName: TableIdPrefix
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/28 8:41
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface TableIdPrefix {
    String value();
}
