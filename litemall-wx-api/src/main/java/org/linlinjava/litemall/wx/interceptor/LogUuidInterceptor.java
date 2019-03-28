package org.linlinjava.litemall.wx.interceptor;

import org.linlinjava.litemall.core.system.CommonConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.UUID;

/**
 * @author 聂祥
 * @version 1.0.0 on 2017/8/25
 */
@Component("LogUuidInterceptor")
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class LogUuidInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogUuidInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
        //设置日志uuid
        setLogUuid(request, response);
        String apiUri = request.getRequestURI();
        //打印request header
        StringBuffer requestHeaderBuffer = new StringBuffer();
        Enumeration<String> headerNames = request.getHeaderNames();
        String headerName = null;
        while (headerNames.hasMoreElements()) {
            headerName = headerNames.nextElement();
            requestHeaderBuffer.append(headerName).append(": ")
                    .append(request.getHeader(headerName)).append("\n");
        }
        LOGGER.info("({}) headers: \n{}", apiUri, requestHeaderBuffer.toString());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        LOGGER.info("移除日志uuid;");
        MDC.remove(CommonConstant.LOG_UUID_MDC_KEY);
    }

    /**
     * 设置日志uuid
     * <p/>
     *
     * @return
     * @throws
     * @author : 卢益
     * @date : 2018/10/24
     * @version : 1.0.0
     */
    private void setLogUuid(final HttpServletRequest request, final HttpServletResponse response) {
        //优先从header中获取uuid
        String uuid = request.getHeader(CommonConstant.LOG_UUID_HEADER_KEY);
        if (uuid != null && !"".equals(uuid.trim())) {
            setLogUuid(uuid, response);
            return;
        }

        uuid = MDC.get(CommonConstant.LOG_UUID_MDC_KEY);
        if (uuid != null && !"".equals(uuid.trim())) {
            //判断线程变量中是否已经存在日志uuid，存在则直接使用
            setLogUuid(uuid, response);
            return;
        }

        uuid = UUID.randomUUID().toString().replaceAll("-", "");
        setLogUuid(uuid, response);
    }

    private void setLogUuid(String uuid, HttpServletResponse response) {
        MDC.put(CommonConstant.LOG_UUID_MDC_KEY, uuid);
        response.addHeader(CommonConstant.LOG_UUID_HEADER_KEY, MDC.get(CommonConstant.LOG_UUID_MDC_KEY));
        LOGGER.debug("设置日志uuid;");
    }
}
