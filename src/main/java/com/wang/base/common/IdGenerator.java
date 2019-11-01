package com.wang.base.common;

import cn.hutool.core.util.IdUtil;
import com.wang.base.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;

import java.io.Serializable;

/***
 * @ClassName: IdGengerator
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/31 11:19
 */
@Slf4j
public class IdGenerator extends IdentityGenerator {
     @Override
     public Serializable generate(SharedSessionContractImplementor session, Object object){
                Object id =   generateId(object.getClass());
                if (id != null) {
                         return (Serializable) id;
                }
                return super.generate(session, object);
     }

    public static final String generateId(Class clazz) {
        TableIdPrefix codePrefix = (TableIdPrefix) clazz.getDeclaredAnnotation(TableIdPrefix.class);
        if (null == codePrefix) {
            throw new BaseException(300,"表前缀错误");
        } else {
            String prefix = codePrefix.value();
            if (null == prefix) {
                throw new BaseException(300,"表前缀错误");
            } else {
                return prefix.toUpperCase()+ "_" + IdUtil.simpleUUID();
            }
        }
    }
}
