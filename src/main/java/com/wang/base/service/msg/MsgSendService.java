package com.wang.base.service.msg;

import com.wang.base.common.result.Result;

/***
 * @ClassName: MsgSendService
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/24 15:05
 */
public interface MsgSendService {
    Result sendMsg(String phone);
}
