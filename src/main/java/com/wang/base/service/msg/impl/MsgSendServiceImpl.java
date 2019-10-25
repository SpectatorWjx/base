package com.wang.base.service.msg.impl;

import com.wang.base.common.result.Result;
import com.wang.base.common.utils.DateUtil;
import com.wang.base.common.utils.RedisUtil;
import com.wang.base.common.utils.ResultUtil;
import com.wang.base.common.utils.msg.MsgJuHeSendUtil;
import com.wang.base.service.msg.MsgSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/***
 * @ClassName: MsgSendServiceImpl
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/24 15:05
 */
@Service
public class MsgSendServiceImpl implements MsgSendService {
    @Value("${phone.msg.expirationSeconds}")
    private Integer expirationSeconds;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public Result sendMsg(String phone) {
        String code = String.valueOf((int)((Math.random()*9+1)*100000));
        redisUtil.setPhoneMsg(phone, code, expirationSeconds);
        MsgJuHeSendUtil.mobileQuery(phone,code);
        return ResultUtil.success();
    }
}
