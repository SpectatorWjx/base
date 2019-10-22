package com.wang.base.common.utils;

import com.wang.base.common.result.Result;

public class ResultUtil {

    /**
     * 请求成功返回
     * @param object
     * @return
     */
    public static Result success(Object object){
        Result Result=new Result();
        Result.setCode(200);
        Result.setMsg("success");
        Result.setData(object);
        return Result;
    }
    public static Result success(){
        return success(null);
    }

    public static Result error(Integer code,String resultResult){
        Result Result=new Result();
        Result.setCode(code);
        Result.setMsg(resultResult);
        return Result;
    }

    public static Result exception(Integer code,String resultResult){
        Result Result=new Result();
        Result.setCode(code);
        Result.setMsg(resultResult);
        return Result;
    }
}