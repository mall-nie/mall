package org.linlinjava.litemall.wx.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

/**
 * 控制层拦截器配置
 *
 * @author 卢益
 * @version 1.0.0 on 2017/8/16 9:47
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class InterceptorConifgurer extends WebMvcConfigurerAdapter {
    @Resource(name = "LogUuidInterceptor")
    private HandlerInterceptor logUuidInterceptor;

    /**
     * 注册拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //注册session拦截器
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(logUuidInterceptor);
        // 配置拦截的路径
        interceptorRegistration.addPathPatterns("/**");

        super.addInterceptors(registry);
    }
}
