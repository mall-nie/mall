package org.linlinjava.litemall.wx.interceptor;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.linlinjava.litemall.core.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


/**
 * 验证码校验
 * @author niexiang
 */
@Component
@Aspect
public class ControllerAop {
    private final Logger logger = LoggerFactory.getLogger(getClass());


    /**
     * 所有请求拦截，查看ip，参数等
     */
    @Pointcut("execution(* org.linlinjava.litemall.wx.web..*.*(..))")
    public void allIntercept() {
    }

    


    /**
     * 拦截所有
     * @param joinPoint 切点
     * @throws Throwable
     */
    @Order(0)
    @Around("allIntercept()")
    public Object doAllIntercept(ProceedingJoinPoint joinPoint)throws Throwable {
        try {
            HttpServletRequest request = RequestUtils.getHttpRequest();
            String uri = request.getRequestURI();
            String api = uri.substring(request.getRequestURI().lastIndexOf("/") + 1);
            //入参处理
            StringBuffer argsBuffer = new StringBuffer();
            Object[] args = joinPoint.getArgs();
            if (args != null) {
                for (Object arg : args) {
                    try {
                        argsBuffer.append(JSON.toJSONString(joinPoint.getArgs())).append(", ");
                    } catch (Exception e) {
                        argsBuffer.append(arg.toString()).append(", ");
                    }
                }
            }
            logger.info("请求：IP:{},API:{}, param:{}", RequestUtils.getIpAddress(request),api, argsBuffer);
            Object proceed = joinPoint.proceed();
            logger.info("响应：param:{}", JSON.toJSONString(proceed));
            return proceed;
        }catch (Throwable e){
            logger.error("errormsg:{}",e);
            throw e;
        }
    }



}
