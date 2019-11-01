package com.wang.base.config.aop;

import com.wang.base.common.log.LogsInfo;
import com.wang.base.common.utils.IpUtil;
import com.wang.base.config.exception.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/***
 * @ClassName:
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/24 10:29
 */
@Aspect
@Component
@Order(1)
@Slf4j
public class WebLogAspect {

    @Autowired
    GlobalExceptionHandler exceptionHandler;

    @Pointcut("execution(public * com.wang.base.controller..*Controller.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
    }

    @AfterReturning(value = "webLog()", returning = "ret")
    public void doAfterReturning(Object ret) {
    }

    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        //记录请求信息
        LogsInfo webLog = new LogsInfo();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String classType = joinPoint.getTarget().getClass().getName();
        Class<?> clazz = null;
        try {
            clazz = Class.forName(classType);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String clazzName = clazz.getName();

        //获取参数
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        Object[] args = joinPoint.getArgs();
        Object params = getParameter(method, args);
        if (Optional.ofNullable(params).isPresent()) {
            webLog.setParams(params.toString());
        }
        webLog.setJavaClass(clazzName);
        webLog.setMethod(request.getMethod());
        webLog.setRequestUrl(request.getRequestURL().toString());

        SecurityContext ctx = SecurityContextHolder.getContext();
        String userName = ctx.getAuthentication().getName();
        webLog.setUserName(userName);
        webLog.setUserAgent(IpUtil.getAgentIpAddr(request));
        webLog.setUserIp(IpUtil.getIpAddr(request));

        Object result = null;
        try {
            result = joinPoint.proceed();
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            webLog.setStatus(response.getStatus());
        } catch (Exception e) {
            webLog.setStatus(500);
            webLog.setException(e.toString()+e.getMessage());
            return exceptionHandler.defaultErrorHandler(e);
        }
        log.info("{}", webLog.toString());
        return result;
    }

    /**
     * 根据方法和传入的参数获取请求参数
     */
    private Object getParameter(Method method, Object[] args) {
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
            if (requestBody != null) {
                argList.add(args[i]);
            }
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                Map<String, Object> map = new HashMap<>();
                String key = parameters[i].getName();
                if (!StringUtils.isEmpty(requestParam.value())) {
                    key = requestParam.value();
                }
                map.put(key, args[i]);
                argList.add(map);
            }
        }
        if (argList.size() == 0) {
            return null;
        } else if (argList.size() == 1) {
            return argList.get(0);
        } else {
            return argList;
        }
    }
}